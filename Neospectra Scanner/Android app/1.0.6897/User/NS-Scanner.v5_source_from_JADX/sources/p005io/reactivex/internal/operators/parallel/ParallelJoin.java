package p005io.reactivex.internal.operators.parallel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelJoin */
public final class ParallelJoin<T> extends Flowable<T> {
    final boolean delayErrors;
    final int prefetch;
    final ParallelFlowable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelJoin$JoinInnerSubscriber */
    static final class JoinInnerSubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 8410034718427740355L;
        final int limit;
        final JoinSubscriptionBase<T> parent;
        final int prefetch;
        long produced;
        volatile SimplePlainQueue<T> queue;

        JoinInnerSubscriber(JoinSubscriptionBase<T> parent2, int prefetch2) {
            this.parent = parent2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, (long) this.prefetch);
        }

        public void onNext(T t) {
            this.parent.onNext(this, t);
        }

        public void onError(Throwable t) {
            this.parent.onError(t);
        }

        public void onComplete() {
            this.parent.onComplete();
        }

        public void requestOne() {
            long p = this.produced + 1;
            if (p == ((long) this.limit)) {
                this.produced = 0;
                ((Subscription) get()).request(p);
                return;
            }
            this.produced = p;
        }

        public void request(long n) {
            long p = this.produced + n;
            if (p >= ((long) this.limit)) {
                this.produced = 0;
                ((Subscription) get()).request(p);
                return;
            }
            this.produced = p;
        }

        public boolean cancel() {
            return SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: 0000 */
        public SimplePlainQueue<T> getQueue() {
            SimplePlainQueue<T> q = this.queue;
            if (q != null) {
                return q;
            }
            SimplePlainQueue<T> q2 = new SpscArrayQueue<>(this.prefetch);
            this.queue = q2;
            return q2;
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelJoin$JoinSubscription */
    static final class JoinSubscription<T> extends JoinSubscriptionBase<T> {
        private static final long serialVersionUID = 6312374661811000451L;

        JoinSubscription(Subscriber<? super T> actual, int n, int prefetch) {
            super(actual, n, prefetch);
        }

        public void onNext(JoinInnerSubscriber<T> inner, T value) {
            if (get() == 0 && compareAndSet(0, 1)) {
                if (this.requested.get() != 0) {
                    this.actual.onNext(value);
                    if (this.requested.get() != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.request(1);
                } else if (!inner.getQueue().offer(value)) {
                    cancelAll();
                    Throwable mbe = new MissingBackpressureException("Queue full?!");
                    if (this.errors.compareAndSet(null, mbe)) {
                        this.actual.onError(mbe);
                    } else {
                        RxJavaPlugins.onError(mbe);
                    }
                    return;
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else if (!inner.getQueue().offer(value)) {
                cancelAll();
                onError(new MissingBackpressureException("Queue full?!"));
                return;
            } else if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        public void onError(Throwable e) {
            if (this.errors.compareAndSet(null, e)) {
                cancelAll();
                drain();
            } else if (e != this.errors.get()) {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
            if (r14 == false) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0062, code lost:
            if (r15 == false) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0064, code lost:
            r4.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0067, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0068, code lost:
            if (r15 == false) goto L_0x006e;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r23 = this;
                r0 = r23
                r1 = 1
                io.reactivex.internal.operators.parallel.ParallelJoin$JoinInnerSubscriber[] r2 = r0.subscribers
                int r3 = r2.length
                org.reactivestreams.Subscriber r4 = r0.actual
            L_0x0008:
                java.util.concurrent.atomic.AtomicLong r5 = r0.requested
                long r5 = r5.get()
                r9 = 0
            L_0x0010:
                int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r11 == 0) goto L_0x0072
                boolean r11 = r0.cancelled
                if (r11 == 0) goto L_0x001c
                r23.cleanup()
                return
            L_0x001c:
                io.reactivex.internal.util.AtomicThrowable r11 = r0.errors
                java.lang.Object r11 = r11.get()
                java.lang.Throwable r11 = (java.lang.Throwable) r11
                if (r11 == 0) goto L_0x002d
                r23.cleanup()
                r4.onError(r11)
                return
            L_0x002d:
                java.util.concurrent.atomic.AtomicInteger r14 = r0.done
                int r14 = r14.get()
                if (r14 != 0) goto L_0x0037
                r14 = 1
                goto L_0x0038
            L_0x0037:
                r14 = 0
            L_0x0038:
                r15 = 1
                r16 = r9
                r9 = 0
            L_0x003c:
                int r10 = r2.length
                if (r9 >= r10) goto L_0x0060
                r10 = r2[r9]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r12 = r10.queue
                if (r12 == 0) goto L_0x005d
                java.lang.Object r13 = r12.poll()
                if (r13 == 0) goto L_0x005d
                r15 = 0
                r4.onNext(r13)
                r10.requestOne()
                r20 = 1
                long r20 = r16 + r20
                r16 = r20
                int r20 = (r20 > r5 ? 1 : (r20 == r5 ? 0 : -1))
                if (r20 != 0) goto L_0x005d
                goto L_0x006b
            L_0x005d:
                int r9 = r9 + 1
                goto L_0x003c
            L_0x0060:
                if (r14 == 0) goto L_0x0068
                if (r15 == 0) goto L_0x0068
                r4.onComplete()
                return
            L_0x0068:
                if (r15 == 0) goto L_0x006e
            L_0x006b:
                r9 = r16
                goto L_0x0072
            L_0x006e:
                r9 = r16
                goto L_0x0010
            L_0x0072:
                int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r11 != 0) goto L_0x00be
                boolean r11 = r0.cancelled
                if (r11 == 0) goto L_0x007e
                r23.cleanup()
                return
            L_0x007e:
                io.reactivex.internal.util.AtomicThrowable r11 = r0.errors
                java.lang.Object r11 = r11.get()
                java.lang.Throwable r11 = (java.lang.Throwable) r11
                if (r11 == 0) goto L_0x008f
                r23.cleanup()
                r4.onError(r11)
                return
            L_0x008f:
                java.util.concurrent.atomic.AtomicInteger r12 = r0.done
                int r12 = r12.get()
                if (r12 != 0) goto L_0x009a
                r19 = 1
                goto L_0x009c
            L_0x009a:
                r19 = 0
            L_0x009c:
                r12 = r19
                r13 = 1
                r18 = 0
            L_0x00a1:
                r14 = r18
                if (r14 >= r3) goto L_0x00b6
                r15 = r2[r14]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r7 = r15.queue
                if (r7 == 0) goto L_0x00b3
                boolean r8 = r7.isEmpty()
                if (r8 != 0) goto L_0x00b3
                r13 = 0
                goto L_0x00b6
            L_0x00b3:
                int r18 = r14 + 1
                goto L_0x00a1
            L_0x00b6:
                if (r12 == 0) goto L_0x00be
                if (r13 == 0) goto L_0x00be
                r4.onComplete()
                return
            L_0x00be:
                r7 = 0
                int r7 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x00d3
                r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x00d3
                java.util.concurrent.atomic.AtomicLong r7 = r0.requested
                long r11 = -r9
                r7.addAndGet(r11)
            L_0x00d3:
                int r7 = r23.get()
                if (r7 != r1) goto L_0x00e2
                int r8 = -r1
                int r1 = r0.addAndGet(r8)
                if (r1 != 0) goto L_0x00e3
                return
            L_0x00e2:
                r1 = r7
            L_0x00e3:
                goto L_0x0008
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelJoin.JoinSubscription.drainLoop():void");
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelJoin$JoinSubscriptionBase */
    static abstract class JoinSubscriptionBase<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 3100232009247827843L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final AtomicInteger done = new AtomicInteger();
        final AtomicThrowable errors = new AtomicThrowable();
        final AtomicLong requested = new AtomicLong();
        final JoinInnerSubscriber<T>[] subscribers;

        /* access modifiers changed from: 0000 */
        public abstract void drain();

        /* access modifiers changed from: 0000 */
        public abstract void onComplete();

        /* access modifiers changed from: 0000 */
        public abstract void onError(Throwable th);

        /* access modifiers changed from: 0000 */
        public abstract void onNext(JoinInnerSubscriber<T> joinInnerSubscriber, T t);

        JoinSubscriptionBase(Subscriber<? super T> actual2, int n, int prefetch) {
            this.actual = actual2;
            JoinInnerSubscriber<T>[] a = new JoinInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new JoinInnerSubscriber<>(this, prefetch);
            }
            this.subscribers = a;
            this.done.lazySet(n);
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelAll();
                if (getAndIncrement() == 0) {
                    cleanup();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            for (JoinInnerSubscriber<T> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void cleanup() {
            for (JoinInnerSubscriber<T> s : this.subscribers) {
                s.queue = null;
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelJoin$JoinSubscriptionDelayError */
    static final class JoinSubscriptionDelayError<T> extends JoinSubscriptionBase<T> {
        private static final long serialVersionUID = -5737965195918321883L;

        JoinSubscriptionDelayError(Subscriber<? super T> actual, int n, int prefetch) {
            super(actual, n, prefetch);
        }

        /* access modifiers changed from: 0000 */
        public void onNext(JoinInnerSubscriber<T> inner, T value) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                if (!inner.getQueue().offer(value) && inner.cancel()) {
                    this.errors.addThrowable(new MissingBackpressureException("Queue full?!"));
                    this.done.decrementAndGet();
                }
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                if (this.requested.get() != 0) {
                    this.actual.onNext(value);
                    if (this.requested.get() != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.request(1);
                } else if (!inner.getQueue().offer(value)) {
                    inner.cancel();
                    this.errors.addThrowable(new MissingBackpressureException("Queue full?!"));
                    this.done.decrementAndGet();
                    drainLoop();
                    return;
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable e) {
            this.errors.addThrowable(e);
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onComplete() {
            this.done.decrementAndGet();
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x004e, code lost:
            if (r11 == false) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0050, code lost:
            if (r14 == false) goto L_0x006a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x005a, code lost:
            if (((java.lang.Throwable) r0.errors.get()) == null) goto L_0x0066;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x005c, code lost:
            r4.onError(r0.errors.terminate());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0066, code lost:
            r4.onComplete();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0069, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x006a, code lost:
            if (r14 == false) goto L_0x006f;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r21 = this;
                r0 = r21
                r1 = 1
                io.reactivex.internal.operators.parallel.ParallelJoin$JoinInnerSubscriber[] r2 = r0.subscribers
                int r3 = r2.length
                org.reactivestreams.Subscriber r4 = r0.actual
            L_0x0008:
                java.util.concurrent.atomic.AtomicLong r5 = r0.requested
                long r5 = r5.get()
                r7 = 0
                r9 = r7
            L_0x0011:
                int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r11 == 0) goto L_0x0072
                boolean r11 = r0.cancelled
                if (r11 == 0) goto L_0x001d
                r21.cleanup()
                return
            L_0x001d:
                java.util.concurrent.atomic.AtomicInteger r11 = r0.done
                int r11 = r11.get()
                if (r11 != 0) goto L_0x0027
                r11 = 1
                goto L_0x0028
            L_0x0027:
                r11 = 0
            L_0x0028:
                r14 = 1
                r15 = r9
                r9 = 0
            L_0x002b:
                if (r9 >= r3) goto L_0x004e
                r10 = r2[r9]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r12 = r10.queue
                if (r12 == 0) goto L_0x004b
                java.lang.Object r13 = r12.poll()
                if (r13 == 0) goto L_0x004b
                r14 = 0
                r4.onNext(r13)
                r10.requestOne()
                r19 = 1
                long r19 = r15 + r19
                r15 = r19
                int r19 = (r19 > r5 ? 1 : (r19 == r5 ? 0 : -1))
                if (r19 != 0) goto L_0x004b
                goto L_0x006d
            L_0x004b:
                int r9 = r9 + 1
                goto L_0x002b
            L_0x004e:
                if (r11 == 0) goto L_0x006a
                if (r14 == 0) goto L_0x006a
                io.reactivex.internal.util.AtomicThrowable r7 = r0.errors
                java.lang.Object r7 = r7.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x0066
                io.reactivex.internal.util.AtomicThrowable r8 = r0.errors
                java.lang.Throwable r8 = r8.terminate()
                r4.onError(r8)
                goto L_0x0069
            L_0x0066:
                r4.onComplete()
            L_0x0069:
                return
            L_0x006a:
                if (r14 == 0) goto L_0x006f
            L_0x006d:
                r9 = r15
                goto L_0x0072
            L_0x006f:
                r9 = r15
                goto L_0x0011
            L_0x0072:
                int r11 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
                if (r11 != 0) goto L_0x00c1
                boolean r11 = r0.cancelled
                if (r11 == 0) goto L_0x007e
                r21.cleanup()
                return
            L_0x007e:
                java.util.concurrent.atomic.AtomicInteger r11 = r0.done
                int r11 = r11.get()
                if (r11 != 0) goto L_0x0089
                r18 = 1
                goto L_0x008b
            L_0x0089:
                r18 = 0
            L_0x008b:
                r11 = r18
                r12 = 1
                r17 = 0
            L_0x0090:
                r13 = r17
                if (r13 >= r3) goto L_0x00a5
                r14 = r2[r13]
                io.reactivex.internal.fuseable.SimplePlainQueue<T> r15 = r14.queue
                if (r15 == 0) goto L_0x00a2
                boolean r16 = r15.isEmpty()
                if (r16 != 0) goto L_0x00a2
                r12 = 0
                goto L_0x00a5
            L_0x00a2:
                int r17 = r13 + 1
                goto L_0x0090
            L_0x00a5:
                if (r11 == 0) goto L_0x00c1
                if (r12 == 0) goto L_0x00c1
                io.reactivex.internal.util.AtomicThrowable r7 = r0.errors
                java.lang.Object r7 = r7.get()
                java.lang.Throwable r7 = (java.lang.Throwable) r7
                if (r7 == 0) goto L_0x00bd
                io.reactivex.internal.util.AtomicThrowable r8 = r0.errors
                java.lang.Throwable r8 = r8.terminate()
                r4.onError(r8)
                goto L_0x00c0
            L_0x00bd:
                r4.onComplete()
            L_0x00c0:
                return
            L_0x00c1:
                int r7 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x00d4
                r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r7 == 0) goto L_0x00d4
                java.util.concurrent.atomic.AtomicLong r7 = r0.requested
                long r11 = -r9
                r7.addAndGet(r11)
            L_0x00d4:
                int r7 = r21.get()
                if (r7 != r1) goto L_0x00e3
                int r8 = -r1
                int r1 = r0.addAndGet(r8)
                if (r1 != 0) goto L_0x00e4
                return
            L_0x00e3:
                r1 = r7
            L_0x00e4:
                goto L_0x0008
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelJoin.JoinSubscriptionDelayError.drainLoop():void");
        }
    }

    public ParallelJoin(ParallelFlowable<? extends T> source2, int prefetch2, boolean delayErrors2) {
        this.source = source2;
        this.prefetch = prefetch2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        JoinSubscriptionBase<T> parent;
        if (this.delayErrors) {
            parent = new JoinSubscriptionDelayError<>(s, this.source.parallelism(), this.prefetch);
        } else {
            parent = new JoinSubscription<>(s, this.source.parallelism(), this.prefetch);
        }
        s.onSubscribe(parent);
        this.source.subscribe(parent.subscribers);
    }
}
