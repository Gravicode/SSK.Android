package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Scheduler;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.internal.util.OpenHashSet;
import p008rx.observables.ConnectableObservable;
import p008rx.schedulers.Timestamped;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorReplay */
public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY = new Func0() {
        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OperatorReplay$BoundedReplayBuffer */
    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        int size;
        Node tail;

        public BoundedReplayBuffer() {
            Node n = new Node(null, 0);
            this.tail = n;
            set(n);
        }

        /* access modifiers changed from: 0000 */
        public final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        /* access modifiers changed from: 0000 */
        public final void removeFirst() {
            Node next = (Node) ((Node) get()).get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        /* access modifiers changed from: 0000 */
        public final void removeSome(int n) {
            Node head = (Node) get();
            while (n > 0) {
                head = (Node) head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        /* access modifiers changed from: 0000 */
        public final void setFirst(Node n) {
            set(n);
        }

        /* access modifiers changed from: 0000 */
        public Node getInitialHead() {
            return (Node) get();
        }

        public final void next(T value) {
            Object o = enterTransform(NotificationLite.next(value));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncate();
        }

        public final void error(Throwable e) {
            Object o = enterTransform(NotificationLite.error(e));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        public final void complete() {
            Object o = enterTransform(NotificationLite.completed());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            if (r13.isUnsubscribed() == false) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            r0 = (p008rx.internal.operators.OperatorReplay.Node) r13.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001a, code lost:
            if (r0 != null) goto L_0x0027;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001c, code lost:
            r0 = getInitialHead();
            r13.index = r0;
            r13.addTotalRequested(r0.index);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002b, code lost:
            if (r13.isUnsubscribed() == false) goto L_0x002e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002e, code lost:
            r1 = r13.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0030, code lost:
            if (r1 != null) goto L_0x0033;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
            r2 = r13.get();
            r6 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003c, code lost:
            if (r6 == r2) goto L_0x0084;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
            r8 = (p008rx.internal.operators.OperatorReplay.Node) r0.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0044, code lost:
            if (r8 == null) goto L_0x0084;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0046, code lost:
            r9 = leaveTransform(r8.value);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0051, code lost:
            if (p008rx.internal.operators.NotificationLite.accept(r1, r9) == false) goto L_0x0056;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0053, code lost:
            r13.index = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0055, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0056, code lost:
            r6 = r6 + 1;
            r0 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x005f, code lost:
            if (r13.isUnsubscribed() == false) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0061, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0063, code lost:
            r4 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0064, code lost:
            r13.index = null;
            p008rx.exceptions.Exceptions.throwIfFatal(r4);
            r13.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0078, code lost:
            r1.onError(p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, p008rx.internal.operators.NotificationLite.getValue(r9)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0083, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0086, code lost:
            if (r6 == 0) goto L_0x0096;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0088, code lost:
            r13.index = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0091, code lost:
            if (r2 == Long.MAX_VALUE) goto L_0x0096;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0093, code lost:
            r13.produced(r6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0096, code lost:
            monitor-enter(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x009a, code lost:
            if (r13.missed != false) goto L_0x00a0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x009c, code lost:
            r13.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x009e, code lost:
            monitor-exit(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x009f, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a0, code lost:
            r13.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a2, code lost:
            monitor-exit(r13);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(p008rx.internal.operators.OperatorReplay.InnerProducer<T> r13) {
            /*
                r12 = this;
                monitor-enter(r13)
                boolean r0 = r13.emitting     // Catch:{ all -> 0x00a8 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r13.missed = r1     // Catch:{ all -> 0x00a8 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a8 }
                return
            L_0x000a:
                r13.emitting = r1     // Catch:{ all -> 0x00a8 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a8 }
            L_0x000d:
                boolean r0 = r13.isUnsubscribed()
                if (r0 == 0) goto L_0x0014
                return
            L_0x0014:
                java.lang.Object r0 = r13.index()
                rx.internal.operators.OperatorReplay$Node r0 = (p008rx.internal.operators.OperatorReplay.Node) r0
                if (r0 != 0) goto L_0x0027
                rx.internal.operators.OperatorReplay$Node r0 = r12.getInitialHead()
                r13.index = r0
                long r1 = r0.index
                r13.addTotalRequested(r1)
            L_0x0027:
                boolean r1 = r13.isUnsubscribed()
                if (r1 == 0) goto L_0x002e
                return
            L_0x002e:
                rx.Subscriber<? super T> r1 = r13.child
                if (r1 != 0) goto L_0x0033
                return
            L_0x0033:
                long r2 = r13.get()
                r4 = 0
                r6 = r4
            L_0x003a:
                int r8 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                if (r8 == 0) goto L_0x0084
                java.lang.Object r8 = r0.get()
                rx.internal.operators.OperatorReplay$Node r8 = (p008rx.internal.operators.OperatorReplay.Node) r8
                if (r8 == 0) goto L_0x0084
                java.lang.Object r9 = r8.value
                java.lang.Object r9 = r12.leaveTransform(r9)
                r10 = 0
                boolean r11 = p008rx.internal.operators.NotificationLite.accept(r1, r9)     // Catch:{ Throwable -> 0x0063 }
                if (r11 == 0) goto L_0x0056
                r13.index = r10     // Catch:{ Throwable -> 0x0063 }
                return
            L_0x0056:
                r10 = 1
                long r6 = r6 + r10
                r0 = r8
                boolean r9 = r13.isUnsubscribed()
                if (r9 == 0) goto L_0x0062
                return
            L_0x0062:
                goto L_0x003a
            L_0x0063:
                r4 = move-exception
                r13.index = r10
                p008rx.exceptions.Exceptions.throwIfFatal(r4)
                r13.unsubscribe()
                boolean r5 = p008rx.internal.operators.NotificationLite.isError(r9)
                if (r5 != 0) goto L_0x0083
                boolean r5 = p008rx.internal.operators.NotificationLite.isCompleted(r9)
                if (r5 != 0) goto L_0x0083
                java.lang.Object r5 = p008rx.internal.operators.NotificationLite.getValue(r9)
                java.lang.Throwable r5 = p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r4, r5)
                r1.onError(r5)
            L_0x0083:
                return
            L_0x0084:
                int r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
                if (r4 == 0) goto L_0x0096
                r13.index = r0
                r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r4 == 0) goto L_0x0096
                r13.produced(r6)
            L_0x0096:
                monitor-enter(r13)
                boolean r4 = r13.missed     // Catch:{ all -> 0x00a5 }
                r5 = 0
                if (r4 != 0) goto L_0x00a0
                r13.emitting = r5     // Catch:{ all -> 0x00a5 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a5 }
                return
            L_0x00a0:
                r13.missed = r5     // Catch:{ all -> 0x00a5 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a5 }
                goto L_0x000d
            L_0x00a5:
                r4 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x00a5 }
                throw r4
            L_0x00a8:
                r0 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x00a8 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorReplay.BoundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
        }

        /* access modifiers changed from: 0000 */
        public void truncateFinal() {
        }

        /* access modifiers changed from: 0000 */
        public final void collect(Collection<? super T> output) {
            Node n = getInitialHead();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!NotificationLite.isCompleted(v) && !NotificationLite.isError(v)) {
                        output.add(NotificationLite.getValue(v));
                        n = next;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean hasError() {
            return this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: 0000 */
        public boolean hasCompleted() {
            return this.tail.value != null && NotificationLite.isCompleted(leaveTransform(this.tail.value));
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$InnerProducer */
    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        public InnerProducer(ReplaySubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
        }

        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r != UNSUBSCRIBED) {
                        if (r < 0 || n != 0) {
                            u = r + n;
                            if (u < 0) {
                                u = Long.MAX_VALUE;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                addTotalRequested(n);
                this.parent.manageRequests(this);
                this.parent.buffer.replay(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void addTotalRequested(long n) {
            long r;
            long u;
            do {
                r = this.totalRequested.get();
                u = r + n;
                if (u < 0) {
                    u = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(r, u));
        }

        public long produced(long n) {
            long r;
            long u;
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            do {
                r = get();
                if (r == UNSUBSCRIBED) {
                    return UNSUBSCRIBED;
                }
                u = r - n;
                if (u < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("More produced (");
                    sb.append(n);
                    sb.append(") than requested (");
                    sb.append(r);
                    sb.append(")");
                    throw new IllegalStateException(sb.toString());
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == UNSUBSCRIBED;
        }

        public void unsubscribe() {
            if (get() != UNSUBSCRIBED && getAndSet(UNSUBSCRIBED) != UNSUBSCRIBED) {
                this.parent.remove(this);
                this.parent.manageRequests(this);
                this.child = null;
            }
        }

        /* access modifiers changed from: 0000 */
        public <U> U index() {
            return this.index;
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$Node */
    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        public Node(Object value2, long index2) {
            this.value = value2;
            this.index = index2;
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$ReplayBuffer */
    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    /* renamed from: rx.internal.operators.OperatorReplay$ReplaySubscriber */
    static final class ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final ReplayBuffer<T> buffer;
        boolean coordinateAll;
        List<InnerProducer<T>> coordinationQueue;
        boolean done;
        boolean emitting;
        long maxChildRequested;
        long maxUpstreamRequested;
        boolean missed;
        volatile Producer producer;
        final OpenHashSet<InnerProducer<T>> producers = new OpenHashSet<>();
        InnerProducer<T>[] producersCache = EMPTY;
        long producersCacheVersion;
        volatile long producersVersion;
        final AtomicBoolean shouldConnect = new AtomicBoolean();
        volatile boolean terminated;

        public ReplaySubscriber(ReplayBuffer<T> buffer2) {
            this.buffer = buffer2;
            request(0);
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            add(Subscriptions.create(new Action0() {
                public void call() {
                    if (!ReplaySubscriber.this.terminated) {
                        synchronized (ReplaySubscriber.this.producers) {
                            if (!ReplaySubscriber.this.terminated) {
                                ReplaySubscriber.this.producers.terminate();
                                ReplaySubscriber.this.producersVersion++;
                                ReplaySubscriber.this.terminated = true;
                            }
                        }
                    }
                }
            }));
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerProducer<T> producer2) {
            if (producer2 == null) {
                throw new NullPointerException();
            } else if (this.terminated) {
                return false;
            } else {
                synchronized (this.producers) {
                    if (this.terminated) {
                        return false;
                    }
                    this.producers.add(producer2);
                    this.producersVersion++;
                    return true;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerProducer<T> producer2) {
            if (!this.terminated) {
                synchronized (this.producers) {
                    if (!this.terminated) {
                        this.producers.remove(producer2);
                        if (this.producers.isEmpty()) {
                            this.producersCache = EMPTY;
                        }
                        this.producersVersion++;
                    }
                }
            }
        }

        public void setProducer(Producer p) {
            if (this.producer != null) {
                throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
            }
            this.producer = p;
            manageRequests(null);
            replay();
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.error(e);
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.complete();
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002c, code lost:
            r3 = r1.maxChildRequested;
            r5 = null;
            r6 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0030, code lost:
            if (r2 == null) goto L_0x003f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
            r12 = java.lang.Math.max(r3, r2.totalRequested.get());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003f, code lost:
            r7 = r3;
            r10 = copyProducers();
            r11 = r10.length;
            r12 = r7;
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
            if (r7 >= r11) goto L_0x005b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
            r8 = r10[r7];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
            if (r8 == null) goto L_0x0058;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
            r12 = java.lang.Math.max(r12, r8.totalRequested.get());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0058, code lost:
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x005b, code lost:
            makeRequest(r12, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0063, code lost:
            if (isUnsubscribed() == false) goto L_0x0066;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0065, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0066, code lost:
            monitor-enter(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0069, code lost:
            if (r1.missed != false) goto L_0x006f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x006b, code lost:
            r1.emitting = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x006d, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x006e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006f, code lost:
            r1.missed = r6;
            r9 = r1.coordinationQueue;
            r1.coordinationQueue = r5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0078, code lost:
            r7 = r1.coordinateAll;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
            r1.coordinateAll = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x007b, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x007c, code lost:
            r3 = r1.maxChildRequested;
            r10 = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x007f, code lost:
            if (r9 == null) goto L_0x009c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0081, code lost:
            r8 = r9.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0089, code lost:
            if (r8.hasNext() == false) goto L_0x009c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x008b, code lost:
            r10 = java.lang.Math.max(r10, ((p008rx.internal.operators.OperatorReplay.InnerProducer) r8.next()).totalRequested.get());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x009c, code lost:
            if (r7 == false) goto L_0x00c7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x009e, code lost:
            r12 = copyProducers();
            r13 = r12.length;
            r14 = r10;
            r10 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a6, code lost:
            if (r10 >= r13) goto L_0x00c3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a8, code lost:
            r11 = r12[r10];
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x00aa, code lost:
            if (r11 == null) goto L_0x00ba;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ac, code lost:
            r16 = r7;
            r14 = java.lang.Math.max(r14, r11.totalRequested.get());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ba, code lost:
            r16 = r7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00bc, code lost:
            r10 = r10 + 1;
            r7 = r16;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c3, code lost:
            r16 = r7;
            r12 = r14;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00c7, code lost:
            r16 = r7;
            r12 = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00ca, code lost:
            makeRequest(r12, r3);
            r7 = r16;
            r5 = null;
            r6 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00d3, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00d4, code lost:
            r16 = r7;
            r5 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x00d8, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d9, code lost:
            r5 = r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
            monitor-exit(r17);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x00db, code lost:
            throw r5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void manageRequests(p008rx.internal.operators.OperatorReplay.InnerProducer<T> r18) {
            /*
                r17 = this;
                r1 = r17
                r2 = r18
                boolean r3 = r17.isUnsubscribed()
                if (r3 == 0) goto L_0x000b
                return
            L_0x000b:
                monitor-enter(r17)
                boolean r3 = r1.emitting     // Catch:{ all -> 0x00dc }
                r4 = 1
                if (r3 == 0) goto L_0x0029
                if (r2 == 0) goto L_0x0023
                java.util.List<rx.internal.operators.OperatorReplay$InnerProducer<T>> r3 = r1.coordinationQueue     // Catch:{ all -> 0x00dc }
                if (r3 != 0) goto L_0x001f
                java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ all -> 0x00dc }
                r5.<init>()     // Catch:{ all -> 0x00dc }
                r3 = r5
                r1.coordinationQueue = r3     // Catch:{ all -> 0x00dc }
            L_0x001f:
                r3.add(r2)     // Catch:{ all -> 0x00dc }
                goto L_0x0025
            L_0x0023:
                r1.coordinateAll = r4     // Catch:{ all -> 0x00dc }
            L_0x0025:
                r1.missed = r4     // Catch:{ all -> 0x00dc }
                monitor-exit(r17)     // Catch:{ all -> 0x00dc }
                return
            L_0x0029:
                r1.emitting = r4     // Catch:{ all -> 0x00dc }
                monitor-exit(r17)     // Catch:{ all -> 0x00dc }
                long r3 = r1.maxChildRequested
                r5 = 0
                r6 = 0
                if (r2 == 0) goto L_0x003f
                java.util.concurrent.atomic.AtomicLong r7 = r2.totalRequested
                long r7 = r7.get()
                long r7 = java.lang.Math.max(r3, r7)
                r9 = r5
                r12 = r7
                goto L_0x005b
            L_0x003f:
                r7 = r3
                rx.internal.operators.OperatorReplay$InnerProducer[] r9 = r17.copyProducers()
                r10 = r9
                int r11 = r10.length
                r12 = r7
                r7 = 0
            L_0x0048:
                if (r7 >= r11) goto L_0x005b
                r8 = r10[r7]
                if (r8 == 0) goto L_0x0058
                java.util.concurrent.atomic.AtomicLong r14 = r8.totalRequested
                long r14 = r14.get()
                long r12 = java.lang.Math.max(r12, r14)
            L_0x0058:
                int r7 = r7 + 1
                goto L_0x0048
            L_0x005b:
                r1.makeRequest(r12, r3)
                r7 = 0
            L_0x005f:
                boolean r8 = r17.isUnsubscribed()
                if (r8 == 0) goto L_0x0066
                return
            L_0x0066:
                monitor-enter(r17)
                boolean r8 = r1.missed     // Catch:{ all -> 0x00d8 }
                if (r8 != 0) goto L_0x006f
                r1.emitting = r6     // Catch:{ all -> 0x00d8 }
                monitor-exit(r17)     // Catch:{ all -> 0x00d8 }
                return
            L_0x006f:
                r1.missed = r6     // Catch:{ all -> 0x00d8 }
                java.util.List<rx.internal.operators.OperatorReplay$InnerProducer<T>> r8 = r1.coordinationQueue     // Catch:{ all -> 0x00d8 }
                r9 = r8
                r1.coordinationQueue = r5     // Catch:{ all -> 0x00d8 }
                boolean r8 = r1.coordinateAll     // Catch:{ all -> 0x00d8 }
                r7 = r8
                r1.coordinateAll = r6     // Catch:{ all -> 0x00d3 }
                monitor-exit(r17)     // Catch:{ all -> 0x00d3 }
                long r3 = r1.maxChildRequested
                r10 = r3
                if (r9 == 0) goto L_0x009c
                java.util.Iterator r8 = r9.iterator()
            L_0x0085:
                boolean r12 = r8.hasNext()
                if (r12 == 0) goto L_0x009c
                java.lang.Object r12 = r8.next()
                rx.internal.operators.OperatorReplay$InnerProducer r12 = (p008rx.internal.operators.OperatorReplay.InnerProducer) r12
                java.util.concurrent.atomic.AtomicLong r13 = r12.totalRequested
                long r13 = r13.get()
                long r10 = java.lang.Math.max(r10, r13)
                goto L_0x0085
            L_0x009c:
                if (r7 == 0) goto L_0x00c7
                rx.internal.operators.OperatorReplay$InnerProducer[] r8 = r17.copyProducers()
                r12 = r8
                int r13 = r12.length
                r14 = r10
                r10 = 0
            L_0x00a6:
                if (r10 >= r13) goto L_0x00c3
                r11 = r12[r10]
                if (r11 == 0) goto L_0x00ba
                java.util.concurrent.atomic.AtomicLong r5 = r11.totalRequested
                r16 = r7
                long r6 = r5.get()
                long r5 = java.lang.Math.max(r14, r6)
                r14 = r5
                goto L_0x00bc
            L_0x00ba:
                r16 = r7
            L_0x00bc:
                int r10 = r10 + 1
                r7 = r16
                r5 = 0
                r6 = 0
                goto L_0x00a6
            L_0x00c3:
                r16 = r7
                r12 = r14
                goto L_0x00ca
            L_0x00c7:
                r16 = r7
                r12 = r10
            L_0x00ca:
                r1.makeRequest(r12, r3)
                r7 = r16
                r5 = 0
                r6 = 0
                goto L_0x005f
            L_0x00d3:
                r0 = move-exception
                r16 = r7
                r5 = r0
                goto L_0x00da
            L_0x00d8:
                r0 = move-exception
                r5 = r0
            L_0x00da:
                monitor-exit(r17)     // Catch:{ all -> 0x00d8 }
                throw r5
            L_0x00dc:
                r0 = move-exception
                r3 = r0
                monitor-exit(r17)     // Catch:{ all -> 0x00dc }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorReplay.ReplaySubscriber.manageRequests(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }

        /* access modifiers changed from: 0000 */
        public InnerProducer<T>[] copyProducers() {
            InnerProducer<T>[] result;
            synchronized (this.producers) {
                Object[] a = this.producers.values();
                int n = a.length;
                result = new InnerProducer[n];
                System.arraycopy(a, 0, result, 0, n);
            }
            return result;
        }

        /* access modifiers changed from: 0000 */
        public void makeRequest(long maxTotalRequests, long previousTotalRequests) {
            long ur = this.maxUpstreamRequested;
            Producer p = this.producer;
            long diff = maxTotalRequests - previousTotalRequests;
            if (diff != 0) {
                this.maxChildRequested = maxTotalRequests;
                if (p == null) {
                    long u = ur + diff;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                    this.maxUpstreamRequested = u;
                } else if (ur != 0) {
                    this.maxUpstreamRequested = 0;
                    p.request(ur + diff);
                } else {
                    p.request(diff);
                }
            } else if (ur != 0 && p != null) {
                this.maxUpstreamRequested = 0;
                p.request(ur);
            }
        }

        /* access modifiers changed from: 0000 */
        public void replay() {
            InnerProducer<T>[] arr$;
            InnerProducer[] pc = this.producersCache;
            if (this.producersCacheVersion != this.producersVersion) {
                synchronized (this.producers) {
                    pc = this.producersCache;
                    Object[] a = this.producers.values();
                    int n = a.length;
                    if (pc.length != n) {
                        pc = new InnerProducer[n];
                        this.producersCache = pc;
                    }
                    System.arraycopy(a, 0, pc, 0, n);
                    this.producersCacheVersion = this.producersVersion;
                }
            }
            ReplayBuffer<T> b = this.buffer;
            for (InnerProducer<T> rp : pc) {
                if (rp != null) {
                    b.replay(rp);
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$SizeAndTimeBoundReplayBuffer */
    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAgeInMillis;
        final Scheduler scheduler;

        public SizeAndTimeBoundReplayBuffer(int limit2, long maxAgeInMillis2, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = limit2;
            this.maxAgeInMillis = maxAgeInMillis2;
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object value) {
            return new Timestamped(this.scheduler.now(), value);
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object value) {
            return ((Timestamped) value).getValue();
        }

        /* access modifiers changed from: 0000 */
        public Node getInitialHead() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            while (next != null && ((Timestamped) next.value).getTimestampMillis() <= timeLimit) {
                prev = next;
                next = (Node) next.get();
            }
            return prev;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null) {
                if (this.size <= this.limit) {
                    if (((Timestamped) next.value).getTimestampMillis() > timeLimit) {
                        break;
                    }
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                } else {
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                }
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        /* access modifiers changed from: 0000 */
        public void truncateFinal() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null && this.size > 1 && ((Timestamped) next.value).getTimestampMillis() <= timeLimit) {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
            if (e != 0) {
                setFirst(prev);
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$SizeBoundReplayBuffer */
    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        public SizeBoundReplayBuffer(int limit2) {
            this.limit = limit2;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorReplay$UnboundedReplayBuffer */
    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        public UnboundedReplayBuffer(int capacityHint) {
            super(capacityHint);
        }

        public void next(T value) {
            add(NotificationLite.next(value));
            this.size++;
        }

        public void error(Throwable e) {
            add(NotificationLite.error(e));
            this.size++;
        }

        public void complete() {
            add(NotificationLite.completed());
            this.size++;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            if (r15.isUnsubscribed() == false) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            r0 = r14.size;
            r1 = (java.lang.Integer) r15.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
            if (r1 == null) goto L_0x0024;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001f, code lost:
            r3 = r1.intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
            r3 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
            r4 = r15.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
            if (r4 != null) goto L_0x002a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0029, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002a, code lost:
            r5 = r15.get();
            r9 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
            if (r9 == r5) goto L_0x006f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
            if (r3 >= r0) goto L_0x006f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0037, code lost:
            r11 = get(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x003f, code lost:
            if (p008rx.internal.operators.NotificationLite.accept(r4, r11) == false) goto L_0x0042;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0041, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0047, code lost:
            if (r15.isUnsubscribed() == false) goto L_0x004a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0049, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x004a, code lost:
            r3 = r3 + 1;
            r9 = r9 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0050, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0051, code lost:
            p008rx.exceptions.Exceptions.throwIfFatal(r2);
            r15.unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0063, code lost:
            r4.onError(p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r2, p008rx.internal.operators.NotificationLite.getValue(r11)));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0071, code lost:
            if (r9 == 0) goto L_0x0085;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0073, code lost:
            r15.index = java.lang.Integer.valueOf(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0080, code lost:
            if (r5 == Long.MAX_VALUE) goto L_0x0085;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0082, code lost:
            r15.produced(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0085, code lost:
            monitor-enter(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0088, code lost:
            if (r15.missed != false) goto L_0x008e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x008a, code lost:
            r15.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x008c, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x008d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x008e, code lost:
            r15.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0090, code lost:
            monitor-exit(r15);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(p008rx.internal.operators.OperatorReplay.InnerProducer<T> r15) {
            /*
                r14 = this;
                monitor-enter(r15)
                boolean r0 = r15.emitting     // Catch:{ all -> 0x0096 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r15.missed = r1     // Catch:{ all -> 0x0096 }
                monitor-exit(r15)     // Catch:{ all -> 0x0096 }
                return
            L_0x000a:
                r15.emitting = r1     // Catch:{ all -> 0x0096 }
                monitor-exit(r15)     // Catch:{ all -> 0x0096 }
            L_0x000d:
                boolean r0 = r15.isUnsubscribed()
                if (r0 == 0) goto L_0x0014
                return
            L_0x0014:
                int r0 = r14.size
                java.lang.Object r1 = r15.index()
                java.lang.Integer r1 = (java.lang.Integer) r1
                r2 = 0
                if (r1 == 0) goto L_0x0024
                int r3 = r1.intValue()
                goto L_0x0025
            L_0x0024:
                r3 = 0
            L_0x0025:
                rx.Subscriber<? super T> r4 = r15.child
                if (r4 != 0) goto L_0x002a
                return
            L_0x002a:
                long r5 = r15.get()
                r7 = 0
                r9 = r7
            L_0x0031:
                int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r11 == 0) goto L_0x006f
                if (r3 >= r0) goto L_0x006f
                java.lang.Object r11 = r14.get(r3)
                boolean r12 = p008rx.internal.operators.NotificationLite.accept(r4, r11)     // Catch:{ Throwable -> 0x0050 }
                if (r12 == 0) goto L_0x0042
                return
            L_0x0042:
                boolean r12 = r15.isUnsubscribed()
                if (r12 == 0) goto L_0x004a
                return
            L_0x004a:
                int r3 = r3 + 1
                r12 = 1
                long r9 = r9 + r12
                goto L_0x0031
            L_0x0050:
                r2 = move-exception
                p008rx.exceptions.Exceptions.throwIfFatal(r2)
                r15.unsubscribe()
                boolean r7 = p008rx.internal.operators.NotificationLite.isError(r11)
                if (r7 != 0) goto L_0x006e
                boolean r7 = p008rx.internal.operators.NotificationLite.isCompleted(r11)
                if (r7 != 0) goto L_0x006e
                java.lang.Object r7 = p008rx.internal.operators.NotificationLite.getValue(r11)
                java.lang.Throwable r7 = p008rx.exceptions.OnErrorThrowable.addValueAsLastCause(r2, r7)
                r4.onError(r7)
            L_0x006e:
                return
            L_0x006f:
                int r7 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x0085
                java.lang.Integer r7 = java.lang.Integer.valueOf(r3)
                r15.index = r7
                r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x0085
                r15.produced(r9)
            L_0x0085:
                monitor-enter(r15)
                boolean r7 = r15.missed     // Catch:{ all -> 0x0093 }
                if (r7 != 0) goto L_0x008e
                r15.emitting = r2     // Catch:{ all -> 0x0093 }
                monitor-exit(r15)     // Catch:{ all -> 0x0093 }
                return
            L_0x008e:
                r15.missed = r2     // Catch:{ all -> 0x0093 }
                monitor-exit(r15)     // Catch:{ all -> 0x0093 }
                goto L_0x000d
            L_0x0093:
                r2 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x0093 }
                throw r2
            L_0x0096:
                r0 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x0096 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorReplay.UnboundedReplayBuffer.replay(rx.internal.operators.OperatorReplay$InnerProducer):void");
        }
    }

    public static <T, U, R> Observable<R> multicastSelector(final Func0<? extends ConnectableObservable<U>> connectableFactory, final Func1<? super Observable<U>, ? extends Observable<R>> selector) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<R>() {
            public void call(final Subscriber<? super R> child) {
                try {
                    ConnectableObservable connectableObservable = (ConnectableObservable) connectableFactory.call();
                    ((Observable) selector.call(connectableObservable)).subscribe(child);
                    connectableObservable.connect(new Action1<Subscription>() {
                        public void call(Subscription t) {
                            child.add(t);
                        }
                    });
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) child);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> co, Scheduler scheduler) {
        final Observable<T> observable = co.observeOn(scheduler);
        return new ConnectableObservable<T>(new OnSubscribe<T>() {
            public void call(final Subscriber<? super T> child) {
                observable.unsafeSubscribe(new Subscriber<T>(child) {
                    public void onNext(T t) {
                        child.onNext(t);
                    }

                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    public void onCompleted() {
                        child.onCompleted();
                    }
                });
            }
        }) {
            public void connect(Action1<? super Subscription> connection) {
                co.connect(connection);
            }
        };
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source2) {
        return create(source2, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source2, final int bufferSize) {
        if (bufferSize == Integer.MAX_VALUE) {
            return create(source2);
        }
        return create(source2, (Func0<? extends ReplayBuffer<T>>) new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(bufferSize);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source2, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source2, maxAge, unit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source2, long maxAge, TimeUnit unit, final Scheduler scheduler, final int bufferSize) {
        final long maxAgeInMillis = unit.toMillis(maxAge);
        return create(source2, (Func0<? extends ReplayBuffer<T>>) new Func0<ReplayBuffer<T>>() {
            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(bufferSize, maxAgeInMillis, scheduler);
            }
        });
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> source2, final Func0<? extends ReplayBuffer<T>> bufferFactory2) {
        final AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<>();
        return new OperatorReplay(new OnSubscribe<T>() {
            /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(p008rx.Subscriber<? super T> r4) {
                /*
                    r3 = this;
                L_0x0000:
                    java.util.concurrent.atomic.AtomicReference r0 = r0
                    java.lang.Object r0 = r0.get()
                    rx.internal.operators.OperatorReplay$ReplaySubscriber r0 = (p008rx.internal.operators.OperatorReplay.ReplaySubscriber) r0
                    if (r0 != 0) goto L_0x0024
                    rx.internal.operators.OperatorReplay$ReplaySubscriber r1 = new rx.internal.operators.OperatorReplay$ReplaySubscriber
                    rx.functions.Func0 r2 = r4
                    java.lang.Object r2 = r2.call()
                    rx.internal.operators.OperatorReplay$ReplayBuffer r2 = (p008rx.internal.operators.OperatorReplay.ReplayBuffer) r2
                    r1.<init>(r2)
                    r1.init()
                    java.util.concurrent.atomic.AtomicReference r2 = r0
                    boolean r2 = r2.compareAndSet(r0, r1)
                    if (r2 != 0) goto L_0x0023
                    goto L_0x0000
                L_0x0023:
                    r0 = r1
                L_0x0024:
                    rx.internal.operators.OperatorReplay$InnerProducer r1 = new rx.internal.operators.OperatorReplay$InnerProducer
                    r1.<init>(r0, r4)
                    r0.add(r1)
                    r4.add(r1)
                    rx.internal.operators.OperatorReplay$ReplayBuffer<T> r2 = r0.buffer
                    r2.replay(r1)
                    r4.setProducer(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorReplay.C16367.call(rx.Subscriber):void");
            }
        }, source2, curr, bufferFactory2);
    }

    private OperatorReplay(OnSubscribe<T> onSubscribe, Observable<? extends T> source2, AtomicReference<ReplaySubscriber<T>> current2, Func0<? extends ReplayBuffer<T>> bufferFactory2) {
        super(onSubscribe);
        this.source = source2;
        this.current = current2;
        this.bufferFactory = bufferFactory2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p008rx.functions.Action1<? super p008rx.Subscription> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$ReplaySubscriber<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            rx.internal.operators.OperatorReplay$ReplaySubscriber r0 = (p008rx.internal.operators.OperatorReplay.ReplaySubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isUnsubscribed()
            if (r1 == 0) goto L_0x002a
        L_0x0010:
            rx.internal.operators.OperatorReplay$ReplaySubscriber r1 = new rx.internal.operators.OperatorReplay$ReplaySubscriber
            rx.functions.Func0<? extends rx.internal.operators.OperatorReplay$ReplayBuffer<T>> r2 = r4.bufferFactory
            java.lang.Object r2 = r2.call()
            rx.internal.operators.OperatorReplay$ReplayBuffer r2 = (p008rx.internal.operators.OperatorReplay.ReplayBuffer) r2
            r1.<init>(r2)
            r1.init()
            java.util.concurrent.atomic.AtomicReference<rx.internal.operators.OperatorReplay$ReplaySubscriber<T>> r2 = r4.current
            boolean r2 = r2.compareAndSet(r0, r1)
            if (r2 != 0) goto L_0x0029
            goto L_0x0000
        L_0x0029:
            r0 = r1
        L_0x002a:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x003d
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r2 = 0
        L_0x003e:
            r1 = r2
            r5.call(r0)
            if (r1 == 0) goto L_0x004a
            rx.Observable<? extends T> r2 = r4.source
            r2.unsafeSubscribe(r0)
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorReplay.connect(rx.functions.Action1):void");
    }
}
