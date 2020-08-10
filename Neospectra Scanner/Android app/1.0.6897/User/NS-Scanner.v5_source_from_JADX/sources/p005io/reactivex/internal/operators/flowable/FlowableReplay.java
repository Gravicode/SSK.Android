package p005io.reactivex.internal.operators.flowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.flowables.ConnectableFlowable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamPublisher;
import p005io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay */
public final class FlowableReplay<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, Disposable {
    static final Callable DEFAULT_UNBOUNDED_FACTORY = new DefaultUnboundedFactory();
    final Callable<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$BoundedReplayBuffer */
    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        int size;
        Node tail;

        BoundedReplayBuffer() {
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
            Object o = enterTransform(NotificationLite.complete());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        /* access modifiers changed from: 0000 */
        public final void trimHead() {
            Node head = (Node) get();
            if (head.value != null) {
                Node n = new Node(null, 0);
                n.lazySet(head.get());
                set(n);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            if (r14.isDisposed() == false) goto L_0x0014;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
            r2 = r14.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            if (r2 != Long.MAX_VALUE) goto L_0x0024;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
            r0 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0024, code lost:
            r0 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
            r5 = 0;
            r7 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.Node) r14.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            if (r7 != null) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
            r7 = getHead();
            r14.index = r7;
            p005io.reactivex.internal.util.BackpressureHelper.add(r14.totalRequested, r7.index);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0040, code lost:
            if (r2 == 0) goto L_0x0085;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0042, code lost:
            r10 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.Node) r7.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
            if (r10 == null) goto L_0x0085;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
            r8 = leaveTransform(r10.value);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0057, code lost:
            if (p005io.reactivex.internal.util.NotificationLite.accept(r8, r14.child) == false) goto L_0x005c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0059, code lost:
            r14.index = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x005b, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x005c, code lost:
            r5 = r5 + 1;
            r2 = r2 - 1;
            r7 = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0066, code lost:
            if (r14.isDisposed() == false) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0068, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x006a, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x006b, code lost:
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1);
            r14.index = null;
            r14.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x007f, code lost:
            r14.child.onError(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0084, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0087, code lost:
            if (r5 == 0) goto L_0x0090;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0089, code lost:
            r14.index = r7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x008b, code lost:
            if (r0 != false) goto L_0x0090;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x008d, code lost:
            r14.produced(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0090, code lost:
            monitor-enter(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0093, code lost:
            if (r14.missed != false) goto L_0x0099;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0095, code lost:
            r14.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0097, code lost:
            monitor-exit(r14);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0098, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0099, code lost:
            r14.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x009b, code lost:
            monitor-exit(r14);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(p005io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r14) {
            /*
                r13 = this;
                monitor-enter(r14)
                boolean r0 = r14.emitting     // Catch:{ all -> 0x00a1 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r14.missed = r1     // Catch:{ all -> 0x00a1 }
                monitor-exit(r14)     // Catch:{ all -> 0x00a1 }
                return
            L_0x000a:
                r14.emitting = r1     // Catch:{ all -> 0x00a1 }
                monitor-exit(r14)     // Catch:{ all -> 0x00a1 }
            L_0x000d:
                boolean r0 = r14.isDisposed()
                if (r0 == 0) goto L_0x0014
                return
            L_0x0014:
                long r2 = r14.get()
                r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                r4 = 0
                if (r0 != 0) goto L_0x0024
                r0 = 1
                goto L_0x0025
            L_0x0024:
                r0 = 0
            L_0x0025:
                r5 = 0
                java.lang.Object r7 = r14.index()
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r7 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.Node) r7
                if (r7 != 0) goto L_0x003c
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r7 = r13.getHead()
                r14.index = r7
                java.util.concurrent.atomic.AtomicLong r8 = r14.totalRequested
                long r9 = r7.index
                p005io.reactivex.internal.util.BackpressureHelper.add(r8, r9)
            L_0x003c:
                r8 = 0
                int r10 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r10 == 0) goto L_0x0085
                java.lang.Object r10 = r7.get()
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r10 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.Node) r10
                if (r10 == 0) goto L_0x0085
                java.lang.Object r8 = r10.value
                java.lang.Object r8 = r13.leaveTransform(r8)
                r9 = 0
                org.reactivestreams.Subscriber<? super T> r11 = r14.child     // Catch:{ Throwable -> 0x006a }
                boolean r11 = p005io.reactivex.internal.util.NotificationLite.accept(r8, r11)     // Catch:{ Throwable -> 0x006a }
                if (r11 == 0) goto L_0x005c
                r14.index = r9     // Catch:{ Throwable -> 0x006a }
                return
            L_0x005c:
                r11 = 1
                long r5 = r5 + r11
                long r2 = r2 - r11
                r7 = r10
                boolean r8 = r14.isDisposed()
                if (r8 == 0) goto L_0x0069
                return
            L_0x0069:
                goto L_0x003c
            L_0x006a:
                r1 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                r14.index = r9
                r14.dispose()
                boolean r4 = p005io.reactivex.internal.util.NotificationLite.isError(r8)
                if (r4 != 0) goto L_0x0084
                boolean r4 = p005io.reactivex.internal.util.NotificationLite.isComplete(r8)
                if (r4 != 0) goto L_0x0084
                org.reactivestreams.Subscriber<? super T> r4 = r14.child
                r4.onError(r1)
            L_0x0084:
                return
            L_0x0085:
                int r8 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
                if (r8 == 0) goto L_0x0090
                r14.index = r7
                if (r0 != 0) goto L_0x0090
                r14.produced(r5)
            L_0x0090:
                monitor-enter(r14)
                boolean r8 = r14.missed     // Catch:{ all -> 0x009e }
                if (r8 != 0) goto L_0x0099
                r14.emitting = r4     // Catch:{ all -> 0x009e }
                monitor-exit(r14)     // Catch:{ all -> 0x009e }
                return
            L_0x0099:
                r14.missed = r4     // Catch:{ all -> 0x009e }
                monitor-exit(r14)     // Catch:{ all -> 0x009e }
                goto L_0x000d
            L_0x009e:
                r1 = move-exception
                monitor-exit(r14)     // Catch:{ all -> 0x009e }
                throw r1
            L_0x00a1:
                r0 = move-exception
                monitor-exit(r14)     // Catch:{ all -> 0x00a1 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableReplay.BoundedReplayBuffer.replay(io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
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
            trimHead();
        }

        /* access modifiers changed from: 0000 */
        public final void collect(Collection<? super T> output) {
            Node n = getHead();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!NotificationLite.isComplete(v) && !NotificationLite.isError(v)) {
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
            return this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: 0000 */
        public Node getHead() {
            return (Node) get();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ConnectableFlowableReplay */
    static final class ConnectableFlowableReplay<T> extends ConnectableFlowable<T> {

        /* renamed from: cf */
        private final ConnectableFlowable<T> f189cf;
        private final Flowable<T> observable;

        ConnectableFlowableReplay(ConnectableFlowable<T> cf, Flowable<T> observable2) {
            this.f189cf = cf;
            this.observable = observable2;
        }

        public void connect(Consumer<? super Disposable> connection) {
            this.f189cf.connect(connection);
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super T> s) {
            this.observable.subscribe(s);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$DefaultUnboundedFactory */
    static final class DefaultUnboundedFactory implements Callable<Object> {
        DefaultUnboundedFactory() {
        }

        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription */
    static final class InnerSubscription<T> extends AtomicLong implements Subscription, Disposable {
        static final long CANCELLED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        InnerSubscription(ReplaySubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n) && BackpressureHelper.addCancel(this, n) != CANCELLED) {
                BackpressureHelper.add(this.totalRequested, n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        public long produced(long n) {
            return BackpressureHelper.producedCancel(this, n);
        }

        public boolean isDisposed() {
            return get() == CANCELLED;
        }

        public void cancel() {
            dispose();
        }

        public void dispose() {
            if (getAndSet(CANCELLED) != CANCELLED) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        /* access modifiers changed from: 0000 */
        public <U> U index() {
            return this.index;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$MulticastFlowable */
    static final class MulticastFlowable<R, U> extends Flowable<R> {
        private final Callable<? extends ConnectableFlowable<U>> connectableFactory;
        private final Function<? super Flowable<U>, ? extends Publisher<R>> selector;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$MulticastFlowable$DisposableConsumer */
        final class DisposableConsumer implements Consumer<Disposable> {
            private final SubscriberResourceWrapper<R> srw;

            DisposableConsumer(SubscriberResourceWrapper<R> srw2) {
                this.srw = srw2;
            }

            public void accept(Disposable r) {
                this.srw.setResource(r);
            }
        }

        MulticastFlowable(Callable<? extends ConnectableFlowable<U>> connectableFactory2, Function<? super Flowable<U>, ? extends Publisher<R>> selector2) {
            this.connectableFactory = connectableFactory2;
            this.selector = selector2;
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super R> child) {
            try {
                ConnectableFlowable<U> cf = (ConnectableFlowable) ObjectHelper.requireNonNull(this.connectableFactory.call(), "The connectableFactory returned null");
                try {
                    Publisher<R> observable = (Publisher) ObjectHelper.requireNonNull(this.selector.apply(cf), "The selector returned a null Publisher");
                    SubscriberResourceWrapper<R> srw = new SubscriberResourceWrapper<>(child);
                    observable.subscribe(srw);
                    cf.connect(new DisposableConsumer(srw));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    EmptySubscription.error(e, child);
                }
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                EmptySubscription.error(e2, child);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$Node */
    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        Node(Object value2, long index2) {
            this.value = value2;
            this.index = index2;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer */
    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerSubscription<T> innerSubscription);
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBufferTask */
    static final class ReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
        private final int bufferSize;

        ReplayBufferTask(int bufferSize2) {
            this.bufferSize = bufferSize2;
        }

        public ReplayBuffer<T> call() {
            return new SizeBoundReplayBuffer(this.bufferSize);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ReplayPublisher */
    static final class ReplayPublisher<T> implements Publisher<T> {
        private final Callable<? extends ReplayBuffer<T>> bufferFactory;
        private final AtomicReference<ReplaySubscriber<T>> curr;

        ReplayPublisher(AtomicReference<ReplaySubscriber<T>> curr2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
            this.curr = curr2;
            this.bufferFactory = bufferFactory2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void subscribe(org.reactivestreams.Subscriber<? super T> r6) {
            /*
                r5 = this;
            L_0x0000:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r0 = r5.curr
                java.lang.Object r0 = r0.get()
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r0 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.ReplaySubscriber) r0
                if (r0 != 0) goto L_0x002d
                java.util.concurrent.Callable<? extends io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer<T>> r1 = r5.bufferFactory     // Catch:{ Throwable -> 0x0025 }
                java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x0025 }
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer r1 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.ReplayBuffer) r1     // Catch:{ Throwable -> 0x0025 }
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r2 = new io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber
                r2.<init>(r1)
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r3 = r5.curr
                r4 = 0
                boolean r3 = r3.compareAndSet(r4, r2)
                if (r3 != 0) goto L_0x0023
                goto L_0x0000
            L_0x0023:
                r0 = r2
                goto L_0x002d
            L_0x0025:
                r1 = move-exception
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                p005io.reactivex.internal.subscriptions.EmptySubscription.error(r1, r6)
                return
            L_0x002d:
                io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription r1 = new io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription
                r1.<init>(r0, r6)
                r6.onSubscribe(r1)
                r0.add(r1)
                boolean r2 = r1.isDisposed()
                if (r2 == 0) goto L_0x0042
                r0.remove(r1)
                return
            L_0x0042:
                r0.manageRequests()
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer<T> r2 = r0.buffer
                r2.replay(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableReplay.ReplayPublisher.subscribe(org.reactivestreams.Subscriber):void");
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber */
    static final class ReplaySubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscription[] EMPTY = new InnerSubscription[0];
        static final InnerSubscription[] TERMINATED = new InnerSubscription[0];
        private static final long serialVersionUID = 7224554242710036740L;
        final ReplayBuffer<T> buffer;
        boolean done;
        final AtomicInteger management = new AtomicInteger();
        long maxChildRequested;
        long maxUpstreamRequested;
        final AtomicBoolean shouldConnect = new AtomicBoolean();
        final AtomicReference<InnerSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

        ReplaySubscriber(ReplayBuffer<T> buffer2) {
            this.buffer = buffer2;
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void dispose() {
            this.subscribers.set(TERMINATED);
            SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerSubscription<T> producer) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscription[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerSubscription<T> p) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                int len = c.length;
                if (len != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        } else if (c[i].equals(p)) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (len == 1) {
                            u = EMPTY;
                        } else {
                            InnerSubscription<T>[] u2 = new InnerSubscription[(len - 1)];
                            System.arraycopy(c, 0, u2, 0, j);
                            System.arraycopy(c, j + 1, u2, j, (len - j) - 1);
                            u = u2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(c, u));
        }

        public void onSubscribe(Subscription p) {
            if (SubscriptionHelper.setOnce(this, p)) {
                manageRequests();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                this.buffer.error(e);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.buffer.complete();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void manageRequests() {
            if (this.management.getAndIncrement() == 0) {
                int missed = 1;
                while (!isDisposed()) {
                    InnerSubscription<T>[] a = (InnerSubscription[]) this.subscribers.get();
                    long ri = this.maxChildRequested;
                    long maxTotalRequests = ri;
                    for (InnerSubscription<T> rp : a) {
                        maxTotalRequests = Math.max(maxTotalRequests, rp.totalRequested.get());
                    }
                    long ur = this.maxUpstreamRequested;
                    Subscription p = (Subscription) get();
                    long diff = maxTotalRequests - ri;
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
                    } else if (!(ur == 0 || p == null)) {
                        this.maxUpstreamRequested = 0;
                        p.request(ur);
                    }
                    missed = this.management.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$ScheduledReplayBufferTask */
    static final class ScheduledReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
        private final int bufferSize;
        private final long maxAge;
        private final Scheduler scheduler;
        private final TimeUnit unit;

        ScheduledReplayBufferTask(int bufferSize2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.bufferSize = bufferSize2;
            this.maxAge = maxAge2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public ReplayBuffer<T> call() {
            SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(this.bufferSize, this.maxAge, this.unit, this.scheduler);
            return sizeAndTimeBoundReplayBuffer;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$SizeAndTimeBoundReplayBuffer */
    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAge;
        final Scheduler scheduler;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int limit2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = limit2;
            this.maxAge = maxAge2;
            this.unit = unit2;
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object value) {
            return new Timed(value, this.scheduler.now(this.unit), this.unit);
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object value) {
            return ((Timed) value).value();
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null) {
                if (this.size <= this.limit) {
                    if (((Timed) next.value).time() > timeLimit) {
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
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null && this.size > 1 && ((Timed) next.value).time() <= timeLimit) {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        /* access modifiers changed from: 0000 */
        public Node getHead() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            for (Node next = (Node) prev.get(); next != null; next = (Node) next.get()) {
                Timed<?> v = (Timed) next.value;
                if (NotificationLite.isComplete(v.value()) || NotificationLite.isError(v.value()) || v.time() > timeLimit) {
                    break;
                }
                prev = next;
            }
            return prev;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$SizeBoundReplayBuffer */
    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        SizeBoundReplayBuffer(int limit2) {
            this.limit = limit2;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReplay$UnboundedReplayBuffer */
    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
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
            add(NotificationLite.complete());
            this.size++;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
            r3 = r2.child;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
            if (r20.isDisposed() == false) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
            r4 = r1.size;
            r5 = (java.lang.Integer) r20.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0022, code lost:
            if (r5 == null) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
            r7 = r5.intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
            r7 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
            r8 = r20.get();
            r10 = r8;
            r12 = 0;
            r14 = r8;
            r9 = r7;
            r7 = r12;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0036, code lost:
            if (r14 == r12) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0038, code lost:
            if (r9 >= r4) goto L_0x0072;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x003a, code lost:
            r6 = get(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0046, code lost:
            if (p005io.reactivex.internal.util.NotificationLite.accept(r6, r3) == false) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0048, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x004e, code lost:
            if (r20.isDisposed() == false) goto L_0x0051;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0050, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0051, code lost:
            r9 = r9 + 1;
            r14 = r14 - 1;
            r7 = r7 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005a, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005b, code lost:
            r12 = r0;
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r12);
            r20.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x006e, code lost:
            r3.onError(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0071, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0074, code lost:
            if (r7 == r12) goto L_0x0088;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x0076, code lost:
            r2.index = java.lang.Integer.valueOf(r9);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0083, code lost:
            if (r10 == Long.MAX_VALUE) goto L_0x0088;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0085, code lost:
            r2.produced(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0088, code lost:
            monitor-enter(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x008b, code lost:
            if (r2.missed != false) goto L_0x0092;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x008d, code lost:
            r2.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0090, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0091, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0092, code lost:
            r2.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0095, code lost:
            monitor-exit(r20);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0098, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x009b, code lost:
            throw r0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(p005io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r20) {
            /*
                r19 = this;
                r1 = r19
                r2 = r20
                monitor-enter(r20)
                boolean r3 = r2.emitting     // Catch:{ all -> 0x009c }
                r4 = 1
                if (r3 == 0) goto L_0x000e
                r2.missed = r4     // Catch:{ all -> 0x009c }
                monitor-exit(r20)     // Catch:{ all -> 0x009c }
                return
            L_0x000e:
                r2.emitting = r4     // Catch:{ all -> 0x009c }
                monitor-exit(r20)     // Catch:{ all -> 0x009c }
                org.reactivestreams.Subscriber<? super T> r3 = r2.child
            L_0x0013:
                boolean r4 = r20.isDisposed()
                if (r4 == 0) goto L_0x001a
                return
            L_0x001a:
                int r4 = r1.size
                java.lang.Object r5 = r20.index()
                java.lang.Integer r5 = (java.lang.Integer) r5
                if (r5 == 0) goto L_0x0029
                int r7 = r5.intValue()
                goto L_0x002a
            L_0x0029:
                r7 = 0
            L_0x002a:
                long r8 = r20.get()
                r10 = r8
                r12 = 0
                r14 = r8
                r9 = r7
                r7 = r12
            L_0x0034:
                int r16 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
                if (r16 == 0) goto L_0x0072
                if (r9 >= r4) goto L_0x0072
                java.lang.Object r16 = r1.get(r9)
                r17 = r16
                r6 = r17
                boolean r16 = p005io.reactivex.internal.util.NotificationLite.accept(r6, r3)     // Catch:{ Throwable -> 0x005a }
                if (r16 == 0) goto L_0x0049
                return
            L_0x0049:
                boolean r16 = r20.isDisposed()
                if (r16 == 0) goto L_0x0051
                return
            L_0x0051:
                int r9 = r9 + 1
                r16 = 1
                long r14 = r14 - r16
                long r7 = r7 + r16
                goto L_0x0034
            L_0x005a:
                r0 = move-exception
                r12 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r12)
                r20.dispose()
                boolean r13 = p005io.reactivex.internal.util.NotificationLite.isError(r6)
                if (r13 != 0) goto L_0x0071
                boolean r13 = p005io.reactivex.internal.util.NotificationLite.isComplete(r6)
                if (r13 != 0) goto L_0x0071
                r3.onError(r12)
            L_0x0071:
                return
            L_0x0072:
                int r6 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1))
                if (r6 == 0) goto L_0x0088
                java.lang.Integer r6 = java.lang.Integer.valueOf(r9)
                r2.index = r6
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r6 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r6 == 0) goto L_0x0088
                r2.produced(r7)
            L_0x0088:
                monitor-enter(r20)
                boolean r6 = r2.missed     // Catch:{ all -> 0x0098 }
                if (r6 != 0) goto L_0x0092
                r6 = 0
                r2.emitting = r6     // Catch:{ all -> 0x0098 }
                monitor-exit(r20)     // Catch:{ all -> 0x0098 }
                return
            L_0x0092:
                r6 = 0
                r2.missed = r6     // Catch:{ all -> 0x0098 }
                monitor-exit(r20)     // Catch:{ all -> 0x0098 }
                goto L_0x0013
            L_0x0098:
                r0 = move-exception
                r6 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x0098 }
                throw r6
            L_0x009c:
                r0 = move-exception
                r3 = r0
                monitor-exit(r20)     // Catch:{ all -> 0x009c }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableReplay.UnboundedReplayBuffer.replay(io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
        }
    }

    public static <U, R> Flowable<R> multicastSelector(Callable<? extends ConnectableFlowable<U>> connectableFactory, Function<? super Flowable<U>, ? extends Publisher<R>> selector) {
        return new MulticastFlowable(connectableFactory, selector);
    }

    public static <T> ConnectableFlowable<T> observeOn(ConnectableFlowable<T> cf, Scheduler scheduler) {
        return RxJavaPlugins.onAssembly((ConnectableFlowable<T>) new ConnectableFlowableReplay<T>(cf, cf.observeOn(scheduler)));
    }

    public static <T> ConnectableFlowable<T> createFrom(Flowable<? extends T> source2) {
        return create(source2, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, int bufferSize) {
        if (bufferSize == Integer.MAX_VALUE) {
            return createFrom(source2);
        }
        return create(source2, (Callable<? extends ReplayBuffer<T>>) new ReplayBufferTask<Object>(bufferSize));
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source2, maxAge, unit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        ScheduledReplayBufferTask scheduledReplayBufferTask = new ScheduledReplayBufferTask(bufferSize, maxAge, unit, scheduler);
        return create(source2, (Callable<? extends ReplayBuffer<T>>) scheduledReplayBufferTask);
    }

    static <T> ConnectableFlowable<T> create(Flowable<T> source2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
        AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly((ConnectableFlowable<T>) new FlowableReplay<T>(new ReplayPublisher<>(curr, bufferFactory2), source2, curr, bufferFactory2));
    }

    private FlowableReplay(Publisher<T> onSubscribe2, Flowable<T> source2, AtomicReference<ReplaySubscriber<T>> current2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferFactory = bufferFactory2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.onSubscribe.subscribe(s);
    }

    public void dispose() {
        this.current.lazySet(null);
    }

    public boolean isDisposed() {
        Disposable d = (Disposable) this.current.get();
        return d == null || d.isDisposed();
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p005io.reactivex.functions.Consumer<? super p005io.reactivex.disposables.Disposable> r7) {
        /*
            r6 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r0 = r6.current
            java.lang.Object r0 = r0.get()
            io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r0 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.ReplaySubscriber) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isDisposed()
            if (r1 == 0) goto L_0x0029
        L_0x0010:
            java.util.concurrent.Callable<? extends io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer<T>> r1 = r6.bufferFactory     // Catch:{ Throwable -> 0x005b }
            java.lang.Object r1 = r1.call()     // Catch:{ Throwable -> 0x005b }
            io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer r1 = (p005io.reactivex.internal.operators.flowable.FlowableReplay.ReplayBuffer) r1     // Catch:{ Throwable -> 0x005b }
            io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r2 = new io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber
            r2.<init>(r1)
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r3 = r6.current
            boolean r3 = r3.compareAndSet(r0, r2)
            if (r3 != 0) goto L_0x0028
            goto L_0x0000
        L_0x0028:
            r0 = r2
        L_0x0029:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x003d
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x003d
            r1 = 1
            goto L_0x003e
        L_0x003d:
            r1 = 0
        L_0x003e:
            r7.accept(r0)     // Catch:{ Throwable -> 0x004b }
            if (r1 == 0) goto L_0x004a
            io.reactivex.Flowable<T> r2 = r6.source
            r2.subscribe(r0)
        L_0x004a:
            return
        L_0x004b:
            r4 = move-exception
            if (r1 == 0) goto L_0x0053
            java.util.concurrent.atomic.AtomicBoolean r5 = r0.shouldConnect
            r5.compareAndSet(r2, r3)
        L_0x0053:
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
            java.lang.RuntimeException r2 = p005io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r4)
            throw r2
        L_0x005b:
            r1 = move-exception
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
            java.lang.RuntimeException r2 = p005io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableReplay.connect(io.reactivex.functions.Consumer):void");
    }
}
