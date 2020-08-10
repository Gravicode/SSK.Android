package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.util.atomic.SpscAtomicArrayQueue;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorEagerConcatMap */
public final class OperatorEagerConcatMap<T, R> implements Operator<R, T> {
    final int bufferSize;
    final Func1<? super T, ? extends Observable<? extends R>> mapper;
    private final int maxConcurrent;

    /* renamed from: rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber */
    static final class EagerInnerSubscriber<T> extends Subscriber<T> {
        volatile boolean done;
        Throwable error;
        final EagerOuterSubscriber<?, T> parent;
        final Queue<Object> queue;

        public EagerInnerSubscriber(EagerOuterSubscriber<?, T> parent2, int bufferSize) {
            Queue<Object> q;
            this.parent = parent2;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscArrayQueue<>(bufferSize);
            } else {
                q = new SpscAtomicArrayQueue<>(bufferSize);
            }
            this.queue = q;
            request((long) bufferSize);
        }

        public void onNext(T t) {
            this.queue.offer(NotificationLite.next(t));
            this.parent.drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            this.parent.drain();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.drain();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            request(n);
        }
    }

    /* renamed from: rx.internal.operators.OperatorEagerConcatMap$EagerOuterProducer */
    static final class EagerOuterProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = -657299606803478389L;
        final EagerOuterSubscriber<?, ?> parent;

        public EagerOuterProducer(EagerOuterSubscriber<?, ?> parent2) {
            this.parent = parent2;
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalStateException(sb.toString());
            } else if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorEagerConcatMap$EagerOuterSubscriber */
    static final class EagerOuterSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final Func1<? super T, ? extends Observable<? extends R>> mapper;
        private EagerOuterProducer sharedProducer;
        final Queue<EagerInnerSubscriber<R>> subscribers = new LinkedList();
        final AtomicInteger wip = new AtomicInteger();

        public EagerOuterSubscriber(Func1<? super T, ? extends Observable<? extends R>> mapper2, int bufferSize2, int maxConcurrent, Subscriber<? super R> actual2) {
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.actual = actual2;
            request(maxConcurrent == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) maxConcurrent);
        }

        /* access modifiers changed from: 0000 */
        public void init() {
            this.sharedProducer = new EagerOuterProducer(this);
            add(Subscriptions.create(new Action0() {
                public void call() {
                    EagerOuterSubscriber.this.cancelled = true;
                    if (EagerOuterSubscriber.this.wip.getAndIncrement() == 0) {
                        EagerOuterSubscriber.this.cleanup();
                    }
                }
            }));
            this.actual.add(this);
            this.actual.setProducer(this.sharedProducer);
        }

        /* access modifiers changed from: 0000 */
        public void cleanup() {
            List<Subscription> list;
            synchronized (this.subscribers) {
                list = new ArrayList<>(this.subscribers);
                this.subscribers.clear();
            }
            for (Subscription s : list) {
                s.unsubscribe();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
            if (r4.cancelled == false) goto L_0x0029;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0029, code lost:
            r0.unsafeSubscribe(r1);
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002f, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onNext(T r5) {
            /*
                r4 = this;
                rx.functions.Func1<? super T, ? extends rx.Observable<? extends R>> r0 = r4.mapper     // Catch:{ Throwable -> 0x0033 }
                java.lang.Object r0 = r0.call(r5)     // Catch:{ Throwable -> 0x0033 }
                rx.Observable r0 = (p008rx.Observable) r0     // Catch:{ Throwable -> 0x0033 }
                boolean r1 = r4.cancelled
                if (r1 == 0) goto L_0x000e
                return
            L_0x000e:
                rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber r1 = new rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber
                int r2 = r4.bufferSize
                r1.<init>(r4, r2)
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r2 = r4.subscribers
                monitor-enter(r2)
                boolean r3 = r4.cancelled     // Catch:{ all -> 0x0030 }
                if (r3 == 0) goto L_0x001e
                monitor-exit(r2)     // Catch:{ all -> 0x0030 }
                return
            L_0x001e:
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r3 = r4.subscribers     // Catch:{ all -> 0x0030 }
                r3.add(r1)     // Catch:{ all -> 0x0030 }
                monitor-exit(r2)     // Catch:{ all -> 0x0030 }
                boolean r2 = r4.cancelled
                if (r2 == 0) goto L_0x0029
                return
            L_0x0029:
                r0.unsafeSubscribe(r1)
                r4.drain()
                return
            L_0x0030:
                r3 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0030 }
                throw r3
            L_0x0033:
                r0 = move-exception
                rx.Subscriber<? super R> r1 = r4.actual
                p008rx.exceptions.Exceptions.throwOrReport(r0, r1, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.onNext(java.lang.Object):void");
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x00db, code lost:
            r0 = th;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r20 = this;
                r1 = r20
                java.util.concurrent.atomic.AtomicInteger r2 = r1.wip
                int r2 = r2.getAndIncrement()
                if (r2 == 0) goto L_0x000b
                return
            L_0x000b:
                r2 = 1
                rx.internal.operators.OperatorEagerConcatMap$EagerOuterProducer r3 = r1.sharedProducer
                rx.Subscriber<? super R> r4 = r1.actual
                r5 = 0
            L_0x0011:
                boolean r6 = r1.cancelled
                if (r6 == 0) goto L_0x0019
                r20.cleanup()
                return
            L_0x0019:
                boolean r6 = r1.done
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r7 = r1.subscribers
                monitor-enter(r7)
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r8 = r1.subscribers     // Catch:{ all -> 0x00d6 }
                java.lang.Object r8 = r8.peek()     // Catch:{ all -> 0x00d6 }
                rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber r8 = (p008rx.internal.operators.OperatorEagerConcatMap.EagerInnerSubscriber) r8     // Catch:{ all -> 0x00d6 }
                r5 = r8
                monitor-exit(r7)     // Catch:{ all -> 0x00d6 }
                if (r5 != 0) goto L_0x002c
                r9 = 1
                goto L_0x002d
            L_0x002c:
                r9 = 0
            L_0x002d:
                if (r6 == 0) goto L_0x0040
                java.lang.Throwable r10 = r1.error
                if (r10 == 0) goto L_0x003a
                r20.cleanup()
                r4.onError(r10)
                return
            L_0x003a:
                if (r9 == 0) goto L_0x0040
                r4.onCompleted()
                return
            L_0x0040:
                if (r9 != 0) goto L_0x00ca
                long r10 = r3.get()
                r12 = 0
                java.util.Queue<java.lang.Object> r14 = r5.queue
                r7 = r12
                r12 = r9
                r9 = r6
                r6 = 0
            L_0x004e:
                boolean r9 = r5.done
                java.lang.Object r13 = r14.peek()
                if (r13 != 0) goto L_0x0059
                r16 = 1
                goto L_0x005b
            L_0x0059:
                r16 = 0
            L_0x005b:
                r12 = r16
                r18 = r13
                r17 = r14
                if (r9 == 0) goto L_0x0087
                java.lang.Throwable r15 = r5.error
                if (r15 == 0) goto L_0x006e
                r20.cleanup()
                r4.onError(r15)
                return
            L_0x006e:
                if (r12 == 0) goto L_0x0087
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r13 = r1.subscribers
                monitor-enter(r13)
                java.util.Queue<rx.internal.operators.OperatorEagerConcatMap$EagerInnerSubscriber<R>> r14 = r1.subscribers     // Catch:{ all -> 0x0083 }
                r14.poll()     // Catch:{ all -> 0x0083 }
                monitor-exit(r13)     // Catch:{ all -> 0x0083 }
                r5.unsubscribe()
                r6 = 1
                r13 = 1
                r1.request(r13)
                goto L_0x008f
            L_0x0083:
                r0 = move-exception
                r14 = r0
                monitor-exit(r13)     // Catch:{ all -> 0x0083 }
                throw r14
            L_0x0087:
                if (r12 == 0) goto L_0x008a
                goto L_0x008f
            L_0x008a:
                int r13 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
                if (r13 != 0) goto L_0x00ad
            L_0x008f:
                r13 = 0
                int r13 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1))
                if (r13 == 0) goto L_0x00a6
                r13 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r13 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
                if (r13 == 0) goto L_0x00a1
                p008rx.internal.operators.BackpressureUtils.produced(r3, r7)
            L_0x00a1:
                if (r6 != 0) goto L_0x00a6
                r5.requestMore(r7)
            L_0x00a6:
                if (r6 == 0) goto L_0x00aa
                goto L_0x0011
            L_0x00aa:
                r6 = r9
                r9 = r12
                goto L_0x00ca
            L_0x00ad:
                r13 = r17
                r13.poll()
                r14 = r18
                java.lang.Object r15 = p008rx.internal.operators.NotificationLite.getValue(r14)     // Catch:{ Throwable -> 0x00c4 }
                r4.onNext(r15)     // Catch:{ Throwable -> 0x00c4 }
                r15 = 0
                r16 = 1
                long r7 = r7 + r16
                r14 = r13
                goto L_0x004e
            L_0x00c4:
                r0 = move-exception
                r15 = r0
                p008rx.exceptions.Exceptions.throwOrReport(r15, r4, r14)
                return
            L_0x00ca:
                java.util.concurrent.atomic.AtomicInteger r7 = r1.wip
                int r8 = -r2
                int r2 = r7.addAndGet(r8)
                if (r2 != 0) goto L_0x00d4
                return
            L_0x00d4:
                goto L_0x0011
            L_0x00d6:
                r0 = move-exception
                r8 = r5
            L_0x00d8:
                r5 = r0
                monitor-exit(r7)     // Catch:{ all -> 0x00db }
                throw r5
            L_0x00db:
                r0 = move-exception
                goto L_0x00d8
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.drain():void");
        }
    }

    public OperatorEagerConcatMap(Func1<? super T, ? extends Observable<? extends R>> mapper2, int bufferSize2, int maxConcurrent2) {
        this.mapper = mapper2;
        this.bufferSize = bufferSize2;
        this.maxConcurrent = maxConcurrent2;
    }

    public Subscriber<? super T> call(Subscriber<? super R> t) {
        EagerOuterSubscriber<T, R> outer = new EagerOuterSubscriber<>(this.mapper, this.bufferSize, this.maxConcurrent, t);
        outer.init();
        return outer;
    }
}
