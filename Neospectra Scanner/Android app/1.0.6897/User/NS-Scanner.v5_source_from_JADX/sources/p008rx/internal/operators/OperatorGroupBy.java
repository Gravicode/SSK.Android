package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.OnSubscribe;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Func1;
import p008rx.internal.producers.ProducerArbiter;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.UtilityFunctions;
import p008rx.observables.GroupedObservable;
import p008rx.observers.Subscribers;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorGroupBy */
public final class OperatorGroupBy<T, K, V> implements Operator<GroupedObservable<K, V>, T> {
    final int bufferSize;
    final boolean delayError;
    final Func1<? super T, ? extends K> keySelector;
    final Func1<Action1<K>, Map<K, Object>> mapFactory;
    final Func1<? super T, ? extends V> valueSelector;

    /* renamed from: rx.internal.operators.OperatorGroupBy$GroupByProducer */
    public static final class GroupByProducer implements Producer {
        final GroupBySubscriber<?, ?, ?> parent;

        public GroupByProducer(GroupBySubscriber<?, ?, ?> parent2) {
            this.parent = parent2;
        }

        public void request(long n) {
            this.parent.requestMore(n);
        }
    }

    /* renamed from: rx.internal.operators.OperatorGroupBy$GroupBySubscriber */
    public static final class GroupBySubscriber<T, K, V> extends Subscriber<T> {
        static final Object NULL_KEY = new Object();
        final Subscriber<? super GroupedObservable<K, V>> actual;
        final int bufferSize;
        final AtomicBoolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final Queue<K> evictedKeys;
        final AtomicInteger groupCount;
        final Map<Object, GroupedUnicast<K, V>> groups;
        final Func1<? super T, ? extends K> keySelector;
        final GroupByProducer producer;
        final Queue<GroupedObservable<K, V>> queue = new ConcurrentLinkedQueue();
        final AtomicLong requested;

        /* renamed from: s */
        final ProducerArbiter f907s = new ProducerArbiter();
        final Func1<? super T, ? extends V> valueSelector;
        final AtomicInteger wip;

        /* renamed from: rx.internal.operators.OperatorGroupBy$GroupBySubscriber$EvictionAction */
        static class EvictionAction<K> implements Action1<K> {
            final Queue<K> evictedKeys;

            EvictionAction(Queue<K> evictedKeys2) {
                this.evictedKeys = evictedKeys2;
            }

            public void call(K key) {
                this.evictedKeys.offer(key);
            }
        }

        public GroupBySubscriber(Subscriber<? super GroupedObservable<K, V>> actual2, Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, int bufferSize2, boolean delayError2, Func1<Action1<K>, Map<K, Object>> mapFactory) {
            this.actual = actual2;
            this.keySelector = keySelector2;
            this.valueSelector = valueSelector2;
            this.bufferSize = bufferSize2;
            this.delayError = delayError2;
            this.f907s.request((long) bufferSize2);
            this.producer = new GroupByProducer(this);
            this.cancelled = new AtomicBoolean();
            this.requested = new AtomicLong();
            this.groupCount = new AtomicInteger(1);
            this.wip = new AtomicInteger();
            if (mapFactory == null) {
                this.groups = new ConcurrentHashMap();
                this.evictedKeys = null;
                return;
            }
            this.evictedKeys = new ConcurrentLinkedQueue();
            this.groups = createMap(mapFactory, new EvictionAction(this.evictedKeys));
        }

        private Map<Object, GroupedUnicast<K, V>> createMap(Func1<Action1<K>, Map<K, Object>> mapFactory, Action1<K> evictionAction) {
            return (Map) mapFactory.call(evictionAction);
        }

        public void setProducer(Producer s) {
            this.f907s.setProducer(s);
        }

        public void onNext(T t) {
            if (!this.done) {
                Queue<GroupedObservable<K, V>> q = this.queue;
                Subscriber<? super GroupedObservable<K, V>> a = this.actual;
                try {
                    K key = this.keySelector.call(t);
                    boolean notNew = true;
                    Object mapKey = key != null ? key : NULL_KEY;
                    GroupedUnicast<K, V> group = (GroupedUnicast) this.groups.get(mapKey);
                    if (group == null) {
                        if (!this.cancelled.get()) {
                            group = GroupedUnicast.createWith(key, this.bufferSize, this, this.delayError);
                            this.groups.put(mapKey, group);
                            this.groupCount.getAndIncrement();
                            notNew = false;
                            q.offer(group);
                            drain();
                        } else {
                            return;
                        }
                    }
                    try {
                        group.onNext(this.valueSelector.call(t));
                        if (this.evictedKeys != null) {
                            while (true) {
                                Object poll = this.evictedKeys.poll();
                                Object obj = poll;
                                if (poll == null) {
                                    break;
                                }
                                GroupedUnicast<K, V> g = (GroupedUnicast) this.groups.get(obj);
                                if (g != null) {
                                    g.onComplete();
                                }
                            }
                        }
                        if (notNew) {
                            this.f907s.request(1);
                        }
                    } catch (Throwable ex) {
                        unsubscribe();
                        errorAll(a, q, ex);
                    }
                } catch (Throwable ex2) {
                    unsubscribe();
                    errorAll(a, q, ex2);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaHooks.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            this.groupCount.decrementAndGet();
            drain();
        }

        public void onCompleted() {
            if (!this.done) {
                for (GroupedUnicast<K, V> e : this.groups.values()) {
                    e.onComplete();
                }
                this.groups.clear();
                if (this.evictedKeys != null) {
                    this.evictedKeys.clear();
                }
                this.done = true;
                this.groupCount.decrementAndGet();
                drain();
            }
        }

        public void requestMore(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            BackpressureUtils.getAndAddRequest(this.requested, n);
            drain();
        }

        public void cancel() {
            if (this.cancelled.compareAndSet(false, true) && this.groupCount.decrementAndGet() == 0) {
                unsubscribe();
            }
        }

        public void cancel(K key) {
            if (this.groups.remove(key != null ? key : NULL_KEY) != null && this.groupCount.decrementAndGet() == 0) {
                unsubscribe();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                Queue<GroupedObservable<K, V>> q = this.queue;
                Subscriber<? super GroupedObservable<K, V>> a = this.actual;
                while (!checkTerminated(this.done, q.isEmpty(), a, q)) {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        boolean d = this.done;
                        GroupedObservable<K, V> t = (GroupedObservable) q.poll();
                        boolean empty = t == null;
                        if (!checkTerminated(d, empty, a, q)) {
                            if (empty) {
                                break;
                            }
                            a.onNext(t);
                            e++;
                        } else {
                            return;
                        }
                    }
                    if (e != 0) {
                        if (r != Long.MAX_VALUE) {
                            BackpressureUtils.produced(this.requested, e);
                        }
                        this.f907s.request(e);
                    }
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void errorAll(Subscriber<? super GroupedObservable<K, V>> a, Queue<?> q, Throwable ex) {
            q.clear();
            List<GroupedUnicast<K, V>> list = new ArrayList<>(this.groups.values());
            this.groups.clear();
            if (this.evictedKeys != null) {
                this.evictedKeys.clear();
            }
            for (GroupedUnicast<K, V> e : list) {
                e.onError(ex);
            }
            a.onError(ex);
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<? super GroupedObservable<K, V>> a, Queue<?> q) {
            if (d) {
                Throwable err = this.error;
                if (err != null) {
                    errorAll(a, q, err);
                    return true;
                } else if (empty) {
                    this.actual.onCompleted();
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: rx.internal.operators.OperatorGroupBy$GroupedUnicast */
    static final class GroupedUnicast<K, T> extends GroupedObservable<K, T> {
        final State<T, K> state;

        public static <T, K> GroupedUnicast<K, T> createWith(K key, int bufferSize, GroupBySubscriber<?, K, T> parent, boolean delayError) {
            return new GroupedUnicast<>(key, new State<>(bufferSize, parent, key, delayError));
        }

        protected GroupedUnicast(K key, State<T, K> state2) {
            super(key, state2);
            this.state = state2;
        }

        public void onNext(T t) {
            this.state.onNext(t);
        }

        public void onError(Throwable e) {
            this.state.onError(e);
        }

        public void onComplete() {
            this.state.onComplete();
        }
    }

    /* renamed from: rx.internal.operators.OperatorGroupBy$State */
    static final class State<T, K> extends AtomicInteger implements Producer, Subscription, OnSubscribe<T> {
        private static final long serialVersionUID = -3852313036005250360L;
        final AtomicReference<Subscriber<? super T>> actual;
        final AtomicBoolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final K key;
        final AtomicBoolean once;
        final GroupBySubscriber<?, K, T> parent;
        final Queue<Object> queue = new ConcurrentLinkedQueue();
        final AtomicLong requested;

        public State(int bufferSize, GroupBySubscriber<?, K, T> parent2, K key2, boolean delayError2) {
            this.parent = parent2;
            this.key = key2;
            this.delayError = delayError2;
            this.cancelled = new AtomicBoolean();
            this.actual = new AtomicReference<>();
            this.once = new AtomicBoolean();
            this.requested = new AtomicLong();
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                drain();
            }
        }

        public boolean isUnsubscribed() {
            return this.cancelled.get();
        }

        public void unsubscribe() {
            if (this.cancelled.compareAndSet(false, true) && getAndIncrement() == 0) {
                this.parent.cancel(this.key);
            }
        }

        public void call(Subscriber<? super T> s) {
            if (this.once.compareAndSet(false, true)) {
                s.add(this);
                s.setProducer(this);
                this.actual.lazySet(s);
                drain();
                return;
            }
            s.onError(new IllegalStateException("Only one Subscriber allowed!"));
        }

        public void onNext(T t) {
            if (t == null) {
                this.error = new NullPointerException();
                this.done = true;
            } else {
                this.queue.offer(NotificationLite.next(t));
            }
            drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Queue<Object> q = this.queue;
                boolean delayError2 = this.delayError;
                Subscriber<? super T> a = (Subscriber) this.actual.get();
                while (true) {
                    if (a != null) {
                        if (!checkTerminated(this.done, q.isEmpty(), a, delayError2)) {
                            long r = this.requested.get();
                            long e = 0;
                            while (e != r) {
                                boolean d = this.done;
                                Object v = q.poll();
                                boolean empty = v == null;
                                if (!checkTerminated(d, empty, a, delayError2)) {
                                    if (empty) {
                                        break;
                                    }
                                    a.onNext(NotificationLite.getValue(v));
                                    e++;
                                } else {
                                    return;
                                }
                            }
                            if (e != 0) {
                                if (r != Long.MAX_VALUE) {
                                    BackpressureUtils.produced(this.requested, e);
                                }
                                this.parent.f907s.request(e);
                            }
                        } else {
                            return;
                        }
                    }
                    missed = addAndGet(-missed);
                    if (missed != 0) {
                        if (a == null) {
                            a = (Subscriber) this.actual.get();
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a, boolean delayError2) {
            if (this.cancelled.get()) {
                this.queue.clear();
                this.parent.cancel(this.key);
                return true;
            }
            if (d) {
                if (!delayError2) {
                    Throwable e = this.error;
                    if (e != null) {
                        this.queue.clear();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        a.onCompleted();
                        return true;
                    }
                } else if (empty) {
                    Throwable e2 = this.error;
                    if (e2 != null) {
                        a.onError(e2);
                    } else {
                        a.onCompleted();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector2) {
        this(keySelector2, UtilityFunctions.identity(), RxRingBuffer.SIZE, false, null);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2) {
        this(keySelector2, valueSelector2, RxRingBuffer.SIZE, false, null);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, Func1<Action1<K>, Map<K, Object>> mapFactory2) {
        this(keySelector2, valueSelector2, RxRingBuffer.SIZE, false, mapFactory2);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector2, Func1<? super T, ? extends V> valueSelector2, int bufferSize2, boolean delayError2, Func1<Action1<K>, Map<K, Object>> mapFactory2) {
        this.keySelector = keySelector2;
        this.valueSelector = valueSelector2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
        this.mapFactory = mapFactory2;
    }

    public Subscriber<? super T> call(Subscriber<? super GroupedObservable<K, V>> child) {
        try {
            GroupBySubscriber groupBySubscriber = new GroupBySubscriber(child, this.keySelector, this.valueSelector, this.bufferSize, this.delayError, this.mapFactory);
            final GroupBySubscriber groupBySubscriber2 = groupBySubscriber;
            child.add(Subscriptions.create(new Action0() {
                public void call() {
                    groupBySubscriber2.cancel();
                }
            }));
            child.setProducer(groupBySubscriber2.producer);
            return groupBySubscriber2;
        } catch (Throwable ex) {
            Exceptions.throwOrReport(ex, (Observer<?>) child);
            Subscriber<? super T> parent2 = Subscribers.empty();
            parent2.unsubscribe();
            return parent2;
        }
    }
}
