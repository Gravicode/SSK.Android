package p008rx.internal.operators;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.functions.Func1;
import p008rx.internal.util.ExceptionsUtils;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.ScalarSynchronousObservable;
import p008rx.internal.util.atomic.SpscAtomicArrayQueue;
import p008rx.internal.util.atomic.SpscLinkedArrayQueue;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeFlattenIterable */
public final class OnSubscribeFlattenIterable<T, R> implements OnSubscribe<R> {
    final Func1<? super T, ? extends Iterable<? extends R>> mapper;
    final int prefetch;
    final Observable<? extends T> source;

    /* renamed from: rx.internal.operators.OnSubscribeFlattenIterable$FlattenIterableSubscriber */
    static final class FlattenIterableSubscriber<T, R> extends Subscriber<T> {
        Iterator<? extends R> active;
        final Subscriber<? super R> actual;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final long limit;
        final Func1<? super T, ? extends Iterable<? extends R>> mapper;
        long produced;
        final Queue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final AtomicInteger wip = new AtomicInteger();

        public FlattenIterableSubscriber(Subscriber<? super R> actual2, Func1<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch) {
            this.actual = actual2;
            this.mapper = mapper2;
            if (prefetch == Integer.MAX_VALUE) {
                this.limit = Long.MAX_VALUE;
                this.queue = new SpscLinkedArrayQueue(RxRingBuffer.SIZE);
            } else {
                this.limit = (long) (prefetch - (prefetch >> 2));
                if (UnsafeAccess.isUnsafeAvailable()) {
                    this.queue = new SpscArrayQueue(prefetch);
                } else {
                    this.queue = new SpscAtomicArrayQueue(prefetch);
                }
            }
            request((long) prefetch);
        }

        public void onNext(T t) {
            if (!this.queue.offer(NotificationLite.next(t))) {
                unsubscribe();
                onError(new MissingBackpressureException());
                return;
            }
            drain();
        }

        public void onError(Throwable e) {
            if (ExceptionsUtils.addThrowable(this.error, e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaHooks.onError(e);
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            if (n > 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                drain();
            } else if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalStateException(sb.toString());
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ec, code lost:
            if (r10 == null) goto L_0x00ef;
         */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0074  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x00f3  */
        /* JADX WARNING: Removed duplicated region for block: B:73:0x0100 A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:75:0x0012 A[SYNTHETIC] */
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
                rx.Subscriber<? super R> r2 = r1.actual
                java.util.Queue<java.lang.Object> r3 = r1.queue
                r7 = 1
                r8 = 0
                r9 = 0
            L_0x0012:
                java.util.Iterator<? extends R> r10 = r1.active
                r11 = 1
                r13 = 0
                if (r10 != 0) goto L_0x0070
                boolean r15 = r1.done
                java.lang.Object r4 = r3.poll()
                if (r4 != 0) goto L_0x0025
                r16 = 1
                goto L_0x0027
            L_0x0025:
                r16 = 0
            L_0x0027:
                r17 = r16
                r5 = r17
                boolean r16 = r1.checkTerminated(r15, r5, r2, r3)
                if (r16 == 0) goto L_0x0032
                return
            L_0x0032:
                if (r5 != 0) goto L_0x0070
                r18 = r7
                long r6 = r1.produced
                long r6 = r6 + r11
                long r11 = r1.limit
                int r11 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
                if (r11 != 0) goto L_0x0045
                r1.produced = r13
                r1.request(r6)
                goto L_0x0047
            L_0x0045:
                r1.produced = r6
            L_0x0047:
                rx.functions.Func1<? super T, ? extends java.lang.Iterable<? extends R>> r11 = r1.mapper     // Catch:{ Throwable -> 0x0065 }
                java.lang.Object r12 = p008rx.internal.operators.NotificationLite.getValue(r4)     // Catch:{ Throwable -> 0x0065 }
                java.lang.Object r11 = r11.call(r12)     // Catch:{ Throwable -> 0x0065 }
                java.lang.Iterable r11 = (java.lang.Iterable) r11     // Catch:{ Throwable -> 0x0065 }
                java.util.Iterator r12 = r11.iterator()     // Catch:{ Throwable -> 0x0065 }
                r10 = r12
                boolean r12 = r10.hasNext()     // Catch:{ Throwable -> 0x0065 }
                r8 = r12
                if (r8 != 0) goto L_0x0062
                goto L_0x00ef
            L_0x0062:
                r1.active = r10
                goto L_0x0072
            L_0x0065:
                r0 = move-exception
                r11 = r10
                r10 = r0
                p008rx.exceptions.Exceptions.throwIfFatal(r10)
                r1.onError(r10)
                goto L_0x00ef
            L_0x0070:
                r18 = r7
            L_0x0072:
                if (r10 == 0) goto L_0x00f3
                java.util.concurrent.atomic.AtomicLong r4 = r1.requested
                long r4 = r4.get()
                r6 = r13
            L_0x007b:
                int r11 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
                if (r11 == 0) goto L_0x00ca
                boolean r11 = r1.done
                r12 = 0
                boolean r11 = r1.checkTerminated(r11, r12, r2, r3)
                if (r11 == 0) goto L_0x0089
                return
            L_0x0089:
                java.lang.Object r11 = r10.next()     // Catch:{ Throwable -> 0x00bc }
                r9 = r11
                r2.onNext(r9)
                boolean r11 = r1.done
                r12 = 0
                boolean r11 = r1.checkTerminated(r11, r12, r2, r3)
                if (r11 == 0) goto L_0x009c
                return
            L_0x009c:
                r11 = 0
                r16 = 1
                long r6 = r6 + r16
                boolean r11 = r10.hasNext()     // Catch:{ Throwable -> 0x00af }
                r8 = r11
                if (r8 != 0) goto L_0x00ae
                r10 = 0
                r11 = 0
                r1.active = r11
                goto L_0x00cb
            L_0x00ae:
                goto L_0x007b
            L_0x00af:
                r0 = move-exception
                r11 = r0
                p008rx.exceptions.Exceptions.throwIfFatal(r11)
                r10 = 0
                r15 = 0
                r1.active = r15
                r1.onError(r11)
                goto L_0x00cb
            L_0x00bc:
                r0 = move-exception
                r12 = 0
                r11 = r0
                p008rx.exceptions.Exceptions.throwIfFatal(r11)
                r10 = 0
                r15 = 0
                r1.active = r15
                r1.onError(r11)
                goto L_0x00cc
            L_0x00ca:
                r12 = 0
            L_0x00cb:
                r15 = 0
            L_0x00cc:
                int r11 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
                if (r11 != 0) goto L_0x00e3
                boolean r11 = r1.done
                boolean r16 = r3.isEmpty()
                if (r16 == 0) goto L_0x00dc
                if (r10 != 0) goto L_0x00dc
                r12 = 1
            L_0x00dc:
                boolean r11 = r1.checkTerminated(r11, r12, r2, r3)
                if (r11 == 0) goto L_0x00e3
                return
            L_0x00e3:
                int r11 = (r6 > r13 ? 1 : (r6 == r13 ? 0 : -1))
                if (r11 == 0) goto L_0x00ec
                java.util.concurrent.atomic.AtomicLong r11 = r1.requested
                p008rx.internal.operators.BackpressureUtils.produced(r11, r6)
            L_0x00ec:
                if (r10 != 0) goto L_0x00f4
            L_0x00ef:
                r7 = r18
                goto L_0x0012
            L_0x00f3:
                r15 = 0
            L_0x00f4:
                java.util.concurrent.atomic.AtomicInteger r4 = r1.wip
                r5 = r18
                int r6 = -r5
                int r7 = r4.addAndGet(r6)
                if (r7 != 0) goto L_0x0101
                return
            L_0x0101:
                goto L_0x0012
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OnSubscribeFlattenIterable.FlattenIterableSubscriber.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, Queue<?> q) {
            if (a.isUnsubscribed()) {
                q.clear();
                this.active = null;
                return true;
            }
            if (d) {
                if (((Throwable) this.error.get()) != null) {
                    Throwable ex = ExceptionsUtils.terminate(this.error);
                    unsubscribe();
                    q.clear();
                    this.active = null;
                    a.onError(ex);
                    return true;
                } else if (empty) {
                    a.onCompleted();
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFlattenIterable$OnSubscribeScalarFlattenIterable */
    static final class OnSubscribeScalarFlattenIterable<T, R> implements OnSubscribe<R> {
        final Func1<? super T, ? extends Iterable<? extends R>> mapper;
        final T value;

        public OnSubscribeScalarFlattenIterable(T value2, Func1<? super T, ? extends Iterable<? extends R>> mapper2) {
            this.value = value2;
            this.mapper = mapper2;
        }

        public void call(Subscriber<? super R> t) {
            try {
                Iterator it = ((Iterable) this.mapper.call(this.value)).iterator();
                if (!it.hasNext()) {
                    t.onCompleted();
                } else {
                    t.setProducer(new IterableProducer(t, it));
                }
            } catch (Throwable ex) {
                Exceptions.throwOrReport(ex, (Observer<?>) t, (Object) this.value);
            }
        }
    }

    protected OnSubscribeFlattenIterable(Observable<? extends T> source2, Func1<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
        this.source = source2;
        this.mapper = mapper2;
        this.prefetch = prefetch2;
    }

    public void call(Subscriber<? super R> t) {
        final FlattenIterableSubscriber<T, R> parent = new FlattenIterableSubscriber<>(t, this.mapper, this.prefetch);
        t.add(parent);
        t.setProducer(new Producer() {
            public void request(long n) {
                parent.requestMore(n);
            }
        });
        this.source.unsafeSubscribe(parent);
    }

    public static <T, R> Observable<R> createFrom(Observable<? extends T> source2, Func1<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
        if (source2 instanceof ScalarSynchronousObservable) {
            return Observable.create((OnSubscribe<T>) new OnSubscribeScalarFlattenIterable<T>(((ScalarSynchronousObservable) source2).get(), mapper2));
        }
        return Observable.create((OnSubscribe<T>) new OnSubscribeFlattenIterable<T>(source2, mapper2, prefetch2));
    }
}
