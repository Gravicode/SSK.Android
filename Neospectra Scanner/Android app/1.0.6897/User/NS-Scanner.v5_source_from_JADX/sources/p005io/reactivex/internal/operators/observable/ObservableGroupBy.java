package p005io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.observables.GroupedObservable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableGroupBy */
public final class ObservableGroupBy<T, K, V> extends AbstractObservableWithUpstream<T, GroupedObservable<K, V>> {
    final int bufferSize;
    final boolean delayError;
    final Function<? super T, ? extends K> keySelector;
    final Function<? super T, ? extends V> valueSelector;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupBy$GroupByObserver */
    public static final class GroupByObserver<T, K, V> extends AtomicInteger implements Observer<T>, Disposable {
        static final Object NULL_KEY = new Object();
        private static final long serialVersionUID = -3688291656102519502L;
        final Observer<? super GroupedObservable<K, V>> actual;
        final int bufferSize;
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        final Map<Object, GroupedUnicast<K, V>> groups;
        final Function<? super T, ? extends K> keySelector;

        /* renamed from: s */
        Disposable f317s;
        final Function<? super T, ? extends V> valueSelector;

        public GroupByObserver(Observer<? super GroupedObservable<K, V>> actual2, Function<? super T, ? extends K> keySelector2, Function<? super T, ? extends V> valueSelector2, int bufferSize2, boolean delayError2) {
            this.actual = actual2;
            this.keySelector = keySelector2;
            this.valueSelector = valueSelector2;
            this.bufferSize = bufferSize2;
            this.delayError = delayError2;
            this.groups = new ConcurrentHashMap();
            lazySet(1);
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f317s, s)) {
                this.f317s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            try {
                K key = this.keySelector.apply(t);
                Object mapKey = key != null ? key : NULL_KEY;
                GroupedUnicast<K, V> group = (GroupedUnicast) this.groups.get(mapKey);
                if (group == null) {
                    if (!this.cancelled.get()) {
                        group = GroupedUnicast.createWith(key, this.bufferSize, this, this.delayError);
                        this.groups.put(mapKey, group);
                        getAndIncrement();
                        this.actual.onNext(group);
                    } else {
                        return;
                    }
                }
                try {
                    group.onNext(ObjectHelper.requireNonNull(this.valueSelector.apply(t), "The value supplied is null"));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f317s.dispose();
                    onError(e);
                }
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                this.f317s.dispose();
                onError(e2);
            }
        }

        public void onError(Throwable t) {
            List<GroupedUnicast<K, V>> list = new ArrayList<>(this.groups.values());
            this.groups.clear();
            for (GroupedUnicast<K, V> e : list) {
                e.onError(t);
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            List<GroupedUnicast<K, V>> list = new ArrayList<>(this.groups.values());
            this.groups.clear();
            for (GroupedUnicast<K, V> e : list) {
                e.onComplete();
            }
            this.actual.onComplete();
        }

        public void dispose() {
            if (this.cancelled.compareAndSet(false, true) && decrementAndGet() == 0) {
                this.f317s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled.get();
        }

        public void cancel(K key) {
            this.groups.remove(key != null ? key : NULL_KEY);
            if (decrementAndGet() == 0) {
                this.f317s.dispose();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupBy$GroupedUnicast */
    static final class GroupedUnicast<K, T> extends GroupedObservable<K, T> {
        final State<T, K> state;

        public static <T, K> GroupedUnicast<K, T> createWith(K key, int bufferSize, GroupByObserver<?, K, T> parent, boolean delayError) {
            return new GroupedUnicast<>(key, new State<>(bufferSize, parent, key, delayError));
        }

        protected GroupedUnicast(K key, State<T, K> state2) {
            super(key);
            this.state = state2;
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Observer<? super T> observer) {
            this.state.subscribe(observer);
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

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGroupBy$State */
    static final class State<T, K> extends AtomicInteger implements Disposable, ObservableSource<T> {
        private static final long serialVersionUID = -3852313036005250360L;
        final AtomicReference<Observer<? super T>> actual = new AtomicReference<>();
        final AtomicBoolean cancelled = new AtomicBoolean();
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final K key;
        final AtomicBoolean once = new AtomicBoolean();
        final GroupByObserver<?, K, T> parent;
        final SpscLinkedArrayQueue<T> queue;

        State(int bufferSize, GroupByObserver<?, K, T> parent2, K key2, boolean delayError2) {
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
            this.parent = parent2;
            this.key = key2;
            this.delayError = delayError2;
        }

        public void dispose() {
            if (this.cancelled.compareAndSet(false, true) && getAndIncrement() == 0) {
                this.actual.lazySet(null);
                this.parent.cancel(this.key);
            }
        }

        public boolean isDisposed() {
            return this.cancelled.get();
        }

        public void subscribe(Observer<? super T> s) {
            if (this.once.compareAndSet(false, true)) {
                s.onSubscribe(this);
                this.actual.lazySet(s);
                if (this.cancelled.get()) {
                    this.actual.lazySet(null);
                } else {
                    drain();
                }
            } else {
                EmptyDisposable.error((Throwable) new IllegalStateException("Only one Observer allowed!"), s);
            }
        }

        public void onNext(T t) {
            this.queue.offer(t);
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
                SpscLinkedArrayQueue<T> q = this.queue;
                boolean delayError2 = this.delayError;
                Observer<? super T> a = (Observer) this.actual.get();
                while (true) {
                    if (a != null) {
                        while (true) {
                            boolean d = this.done;
                            T v = q.poll();
                            boolean empty = v == null;
                            if (!checkTerminated(d, empty, a, delayError2)) {
                                if (empty) {
                                    break;
                                }
                                a.onNext(v);
                            } else {
                                return;
                            }
                        }
                    }
                    missed = addAndGet(-missed);
                    if (missed != 0) {
                        if (a == null) {
                            a = (Observer) this.actual.get();
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a, boolean delayError2) {
            if (this.cancelled.get()) {
                this.queue.clear();
                this.parent.cancel(this.key);
                this.actual.lazySet(null);
                return true;
            }
            if (d) {
                if (!delayError2) {
                    Throwable e = this.error;
                    if (e != null) {
                        this.queue.clear();
                        this.actual.lazySet(null);
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        this.actual.lazySet(null);
                        a.onComplete();
                        return true;
                    }
                } else if (empty) {
                    Throwable e2 = this.error;
                    this.actual.lazySet(null);
                    if (e2 != null) {
                        a.onError(e2);
                    } else {
                        a.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public ObservableGroupBy(ObservableSource<T> source, Function<? super T, ? extends K> keySelector2, Function<? super T, ? extends V> valueSelector2, int bufferSize2, boolean delayError2) {
        super(source);
        this.keySelector = keySelector2;
        this.valueSelector = valueSelector2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super GroupedObservable<K, V>> t) {
        ObservableSource observableSource = this.source;
        GroupByObserver groupByObserver = new GroupByObserver(t, this.keySelector, this.valueSelector, this.bufferSize, this.delayError);
        observableSource.subscribe(groupByObserver);
    }
}
