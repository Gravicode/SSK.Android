package p008rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.functions.Func2;
import p008rx.internal.util.atomic.SpscLinkedAtomicQueue;
import p008rx.internal.util.unsafe.SpscLinkedQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;

/* renamed from: rx.internal.operators.OperatorScan */
public final class OperatorScan<R, T> implements Operator<R, T> {
    private static final Object NO_INITIAL_VALUE = new Object();
    final Func2<R, ? super T, R> accumulator;
    private final Func0<R> initialValueFactory;

    /* renamed from: rx.internal.operators.OperatorScan$InitialProducer */
    static final class InitialProducer<R> implements Producer, Observer<R> {
        final Subscriber<? super R> child;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        long missedRequested;
        volatile Producer producer;
        final Queue<Object> queue;
        final AtomicLong requested;

        public InitialProducer(R initialValue, Subscriber<? super R> child2) {
            Queue<Object> q;
            this.child = child2;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscLinkedQueue<>();
            } else {
                q = new SpscLinkedAtomicQueue<>();
            }
            this.queue = q;
            q.offer(NotificationLite.next(initialValue));
            this.requested = new AtomicLong();
        }

        public void onNext(R t) {
            this.queue.offer(NotificationLite.next(t));
            emit();
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<? super R> child2) {
            if (child2.isUnsubscribed()) {
                return true;
            }
            if (d) {
                Throwable err = this.error;
                if (err != null) {
                    child2.onError(err);
                    return true;
                } else if (empty) {
                    child2.onCompleted();
                    return true;
                }
            }
            return false;
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                Producer p = this.producer;
                if (p == null) {
                    synchronized (this.requested) {
                        p = this.producer;
                        if (p == null) {
                            this.missedRequested = BackpressureUtils.addCap(this.missedRequested, n);
                        }
                    }
                }
                if (p != null) {
                    p.request(n);
                }
                emit();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x002e, code lost:
            if (r3 <= 0) goto L_0x0033;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0030, code lost:
            r10.request(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0033, code lost:
            emit();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0036, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setProducer(p008rx.Producer r10) {
            /*
                r9 = this;
                if (r10 != 0) goto L_0x0008
                java.lang.NullPointerException r0 = new java.lang.NullPointerException
                r0.<init>()
                throw r0
            L_0x0008:
                java.util.concurrent.atomic.AtomicLong r0 = r9.requested
                monitor-enter(r0)
                r1 = 0
                rx.Producer r3 = r9.producer     // Catch:{ all -> 0x003c }
                if (r3 == 0) goto L_0x0019
                java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x003c }
                java.lang.String r4 = "Can't set more than one Producer!"
                r3.<init>(r4)     // Catch:{ all -> 0x003c }
                throw r3     // Catch:{ all -> 0x003c }
            L_0x0019:
                long r3 = r9.missedRequested     // Catch:{ all -> 0x003c }
                r5 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r5 == 0) goto L_0x0027
                r5 = 1
                long r3 = r3 - r5
            L_0x0027:
                r9.missedRequested = r1     // Catch:{ all -> 0x0037 }
                r9.producer = r10     // Catch:{ all -> 0x0037 }
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                int r0 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
                if (r0 <= 0) goto L_0x0033
                r10.request(r3)
            L_0x0033:
                r9.emit()
                return
            L_0x0037:
                r1 = move-exception
                r7 = r3
                r3 = r1
                r1 = r7
                goto L_0x003d
            L_0x003c:
                r3 = move-exception
            L_0x003d:
                monitor-exit(r0)     // Catch:{ all -> 0x003c }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorScan.InitialProducer.setProducer(rx.Producer):void");
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void emitLoop() {
            Subscriber<? super R> child2 = this.child;
            Queue<Object> queue2 = this.queue;
            AtomicLong requested2 = this.requested;
            long r = requested2.get();
            while (true) {
                boolean d = this.done;
                boolean empty = queue2.isEmpty();
                if (!checkTerminated(d, empty, child2)) {
                    boolean z = d;
                    boolean z2 = empty;
                    long e = 0;
                    while (e != r) {
                        boolean d2 = this.done;
                        Object o = queue2.poll();
                        boolean empty2 = o == null;
                        if (!checkTerminated(d2, empty2, child2)) {
                            if (empty2) {
                                break;
                            }
                            R v = NotificationLite.getValue(o);
                            try {
                                child2.onNext(v);
                                e++;
                            } catch (Throwable ex) {
                                Exceptions.throwOrReport(ex, (Observer<?>) child2, (Object) v);
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    if (!(e == 0 || r == Long.MAX_VALUE)) {
                        r = BackpressureUtils.produced(requested2, e);
                    }
                    synchronized (this) {
                        if (!this.missed) {
                            this.emitting = false;
                            return;
                        }
                        this.missed = false;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public OperatorScan(final R initialValue, Func2<R, ? super T, R> accumulator2) {
        this((Func0<R>) new Func0<R>() {
            public R call() {
                return initialValue;
            }
        }, accumulator2);
    }

    public OperatorScan(Func0<R> initialValueFactory2, Func2<R, ? super T, R> accumulator2) {
        this.initialValueFactory = initialValueFactory2;
        this.accumulator = accumulator2;
    }

    public OperatorScan(Func2<R, ? super T, R> accumulator2) {
        this((R) NO_INITIAL_VALUE, accumulator2);
    }

    public Subscriber<? super T> call(final Subscriber<? super R> child) {
        final R initialValue = this.initialValueFactory.call();
        if (initialValue == NO_INITIAL_VALUE) {
            return new Subscriber<T>(child) {
                boolean once;
                R value;

                /* JADX WARNING: Incorrect type for immutable var: ssa=T, code=java.lang.Object, for r4v0, types: [T, java.lang.Object] */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onNext(java.lang.Object r4) {
                    /*
                        r3 = this;
                        boolean r0 = r3.once
                        if (r0 != 0) goto L_0x0009
                        r0 = 1
                        r3.once = r0
                        r0 = r4
                        goto L_0x0015
                    L_0x0009:
                        R r0 = r3.value
                        rx.internal.operators.OperatorScan r1 = p008rx.internal.operators.OperatorScan.this     // Catch:{ Throwable -> 0x001d }
                        rx.functions.Func2<R, ? super T, R> r1 = r1.accumulator     // Catch:{ Throwable -> 0x001d }
                        java.lang.Object r1 = r1.call(r0, r4)     // Catch:{ Throwable -> 0x001d }
                        r0 = r1
                    L_0x0015:
                        r3.value = r0
                        rx.Subscriber r1 = r4
                        r1.onNext(r0)
                        return
                    L_0x001d:
                        r1 = move-exception
                        rx.Subscriber r2 = r4
                        p008rx.exceptions.Exceptions.throwOrReport(r1, r2, r4)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorScan.C16432.onNext(java.lang.Object):void");
                }

                public void onError(Throwable e) {
                    child.onError(e);
                }

                public void onCompleted() {
                    child.onCompleted();
                }
            };
        }
        final InitialProducer<R> ip = new InitialProducer<>(initialValue, child);
        Subscriber<T> parent = new Subscriber<T>() {
            private R value = initialValue;

            public void onNext(T currentValue) {
                try {
                    Object call = OperatorScan.this.accumulator.call(this.value, currentValue);
                    this.value = call;
                    ip.onNext(call);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this, (Object) currentValue);
                }
            }

            public void onError(Throwable e) {
                ip.onError(e);
            }

            public void onCompleted() {
                ip.onCompleted();
            }

            public void setProducer(Producer producer) {
                ip.setProducer(producer);
            }
        };
        child.add(parent);
        child.setProducer(ip);
        return parent;
    }
}
