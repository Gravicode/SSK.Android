package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;

/* renamed from: rx.internal.operators.OperatorOnBackpressureLatest */
public final class OperatorOnBackpressureLatest<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorOnBackpressureLatest$Holder */
    static final class Holder {
        static final OperatorOnBackpressureLatest<Object> INSTANCE = new OperatorOnBackpressureLatest<>();

        Holder() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnBackpressureLatest$LatestEmitter */
    static final class LatestEmitter<T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        static final Object EMPTY = new Object();
        static final long NOT_REQUESTED = -4611686018427387904L;
        private static final long serialVersionUID = -1364393685005146274L;
        final Subscriber<? super T> child;
        volatile boolean done;
        boolean emitting;
        boolean missed;
        LatestSubscriber<? super T> parent;
        Throwable terminal;
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY);

        public LatestEmitter(Subscriber<? super T> child2) {
            this.child = child2;
            lazySet(NOT_REQUESTED);
        }

        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE) {
                        if (r == NOT_REQUESTED) {
                            u = n;
                        } else {
                            u = r + n;
                            if (u < 0) {
                                u = Long.MAX_VALUE;
                            }
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                if (r == NOT_REQUESTED) {
                    this.parent.requestMore(Long.MAX_VALUE);
                }
                emit();
            }
        }

        /* access modifiers changed from: 0000 */
        public long produced(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r < 0) {
                    return r;
                }
                u = r - n;
            } while (!compareAndSet(r, u));
            return u;
        }

        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        public void unsubscribe() {
            if (get() >= 0) {
                getAndSet(Long.MIN_VALUE);
            }
        }

        public void onNext(T t) {
            this.value.lazySet(t);
            emit();
        }

        public void onError(Throwable e) {
            this.terminal = e;
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
            r2 = get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
            if (r2 != Long.MIN_VALUE) goto L_0x001d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
            r4 = r8.value.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0027, code lost:
            if (r2 <= 0) goto L_0x0042;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002b, code lost:
            if (r4 == EMPTY) goto L_0x0042;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002d, code lost:
            r8.child.onNext(r4);
            r8.value.compareAndSet(r4, EMPTY);
            produced(1);
            r4 = EMPTY;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
            if (r4 != EMPTY) goto L_0x0059;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
            if (r8.done == false) goto L_0x0059;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
            r5 = r8.terminal;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
            if (r5 == null) goto L_0x0054;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
            r8.child.onError(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0054, code lost:
            r8.child.onCompleted();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0059, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x005c, code lost:
            if (r8.missed != false) goto L_0x006d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005e, code lost:
            r8.emitting = false;
            r1 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0061, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x0062, code lost:
            if (r1 != false) goto L_0x006c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0064, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0067, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x006c, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
            r8.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x006f, code lost:
            monitor-exit(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0074, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0075, code lost:
            if (0 == 0) goto L_0x0077;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0077, code lost:
            monitor-enter(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            r8.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x007f, code lost:
            throw r2;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emit() {
            /*
                r8 = this;
                monitor-enter(r8)
                boolean r0 = r8.emitting     // Catch:{ all -> 0x0080 }
                r1 = 1
                if (r0 == 0) goto L_0x000a
                r8.missed = r1     // Catch:{ all -> 0x0080 }
                monitor-exit(r8)     // Catch:{ all -> 0x0080 }
                return
            L_0x000a:
                r8.emitting = r1     // Catch:{ all -> 0x0080 }
                r0 = 0
                r8.missed = r0     // Catch:{ all -> 0x0080 }
                monitor-exit(r8)     // Catch:{ all -> 0x0080 }
                r1 = 0
            L_0x0011:
                long r2 = r8.get()     // Catch:{ all -> 0x0074 }
                r4 = -9223372036854775808
                int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r4 != 0) goto L_0x001d
                r1 = 1
                goto L_0x0062
            L_0x001d:
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r4 = r8.value     // Catch:{ all -> 0x0074 }
                java.lang.Object r4 = r4.get()     // Catch:{ all -> 0x0074 }
                r5 = 0
                int r5 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
                if (r5 <= 0) goto L_0x0042
                java.lang.Object r5 = EMPTY     // Catch:{ all -> 0x0074 }
                if (r4 == r5) goto L_0x0042
                r5 = r4
                rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x0074 }
                r6.onNext(r5)     // Catch:{ all -> 0x0074 }
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r6 = r8.value     // Catch:{ all -> 0x0074 }
                java.lang.Object r7 = EMPTY     // Catch:{ all -> 0x0074 }
                r6.compareAndSet(r4, r7)     // Catch:{ all -> 0x0074 }
                r6 = 1
                r8.produced(r6)     // Catch:{ all -> 0x0074 }
                java.lang.Object r6 = EMPTY     // Catch:{ all -> 0x0074 }
                r4 = r6
            L_0x0042:
                java.lang.Object r5 = EMPTY     // Catch:{ all -> 0x0074 }
                if (r4 != r5) goto L_0x0059
                boolean r5 = r8.done     // Catch:{ all -> 0x0074 }
                if (r5 == 0) goto L_0x0059
                java.lang.Throwable r5 = r8.terminal     // Catch:{ all -> 0x0074 }
                if (r5 == 0) goto L_0x0054
                rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x0074 }
                r6.onError(r5)     // Catch:{ all -> 0x0074 }
                goto L_0x0059
            L_0x0054:
                rx.Subscriber<? super T> r6 = r8.child     // Catch:{ all -> 0x0074 }
                r6.onCompleted()     // Catch:{ all -> 0x0074 }
            L_0x0059:
                monitor-enter(r8)     // Catch:{ all -> 0x0074 }
                boolean r5 = r8.missed     // Catch:{ all -> 0x0071 }
                if (r5 != 0) goto L_0x006d
                r8.emitting = r0     // Catch:{ all -> 0x0071 }
                r1 = 1
                monitor-exit(r8)     // Catch:{ all -> 0x0071 }
            L_0x0062:
                if (r1 != 0) goto L_0x006c
                monitor-enter(r8)
                r8.emitting = r0     // Catch:{ all -> 0x0069 }
                monitor-exit(r8)     // Catch:{ all -> 0x0069 }
                goto L_0x006c
            L_0x0069:
                r0 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0069 }
                throw r0
            L_0x006c:
                return
            L_0x006d:
                r8.missed = r0     // Catch:{ all -> 0x0071 }
                monitor-exit(r8)     // Catch:{ all -> 0x0071 }
                goto L_0x0011
            L_0x0071:
                r5 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0071 }
                throw r5     // Catch:{ all -> 0x0074 }
            L_0x0074:
                r2 = move-exception
                if (r1 != 0) goto L_0x007f
                monitor-enter(r8)
                r8.emitting = r0     // Catch:{ all -> 0x007c }
                monitor-exit(r8)     // Catch:{ all -> 0x007c }
                goto L_0x007f
            L_0x007c:
                r0 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x007c }
                throw r0
            L_0x007f:
                throw r2
            L_0x0080:
                r0 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0080 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.emit():void");
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnBackpressureLatest$LatestSubscriber */
    static final class LatestSubscriber<T> extends Subscriber<T> {
        private final LatestEmitter<T> producer;

        LatestSubscriber(LatestEmitter<T> producer2) {
            this.producer = producer2;
        }

        public void onStart() {
            request(0);
        }

        public void onNext(T t) {
            this.producer.onNext(t);
        }

        public void onError(Throwable e) {
            this.producer.onError(e);
        }

        public void onCompleted() {
            this.producer.onCompleted();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            request(n);
        }
    }

    public static <T> OperatorOnBackpressureLatest<T> instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        LatestEmitter<T> producer = new LatestEmitter<>(child);
        LatestSubscriber<T> parent = new LatestSubscriber<>(producer);
        producer.parent = parent;
        child.add(parent);
        child.add(producer);
        child.setProducer(producer);
        return parent;
    }
}
