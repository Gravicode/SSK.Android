package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableZip */
public final class FlowableZip<T, R> extends Flowable<R> {
    final int bufferSize;
    final boolean delayError;
    final Publisher<? extends T>[] sources;
    final Iterable<? extends Publisher<? extends T>> sourcesIterable;
    final Function<? super Object[], ? extends R> zipper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableZip$ZipCoordinator */
    static final class ZipCoordinator<T, R> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = -2434867452883857743L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        final Object[] current;
        final boolean delayErrors;
        final AtomicThrowable errors;
        final AtomicLong requested;
        final ZipSubscriber<T, R>[] subscribers;
        final Function<? super Object[], ? extends R> zipper;

        ZipCoordinator(Subscriber<? super R> actual2, Function<? super Object[], ? extends R> zipper2, int n, int prefetch, boolean delayErrors2) {
            this.actual = actual2;
            this.zipper = zipper2;
            this.delayErrors = delayErrors2;
            ZipSubscriber<T, R>[] a = new ZipSubscriber[n];
            for (int i = 0; i < n; i++) {
                a[i] = new ZipSubscriber<>(this, prefetch);
            }
            this.current = new Object[n];
            this.subscribers = a;
            this.requested = new AtomicLong();
            this.errors = new AtomicThrowable();
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher<? extends T>[] sources, int n) {
            ZipSubscriber<T, R>[] a = this.subscribers;
            for (int i = 0; i < n && !this.cancelled && (this.delayErrors || this.errors.get() == null); i++) {
                sources[i].subscribe(a[i]);
            }
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
            }
        }

        /* access modifiers changed from: 0000 */
        public void error(ZipSubscriber<T, R> inner, Throwable e) {
            if (this.errors.addThrowable(e)) {
                inner.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            for (ZipSubscriber<T, R> s : this.subscribers) {
                s.cancel();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:98:0x0153, code lost:
            return;
         */
        /* JADX WARNING: Removed duplicated region for block: B:127:0x016e A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:129:0x00a1 A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:141:0x017d A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x00ae  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r22 = this;
                r1 = r22
                int r2 = r22.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                org.reactivestreams.Subscriber<? super R> r2 = r1.actual
                io.reactivex.internal.operators.flowable.FlowableZip$ZipSubscriber<T, R>[] r3 = r1.subscribers
                int r4 = r3.length
                java.lang.Object[] r5 = r1.current
                r7 = 1
            L_0x0011:
                java.util.concurrent.atomic.AtomicLong r8 = r1.requested
                long r8 = r8.get()
                r12 = 0
            L_0x0019:
                int r14 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
                r16 = 0
                if (r14 == 0) goto L_0x00ec
                boolean r14 = r1.cancelled
                if (r14 == 0) goto L_0x0024
                return
            L_0x0024:
                boolean r14 = r1.delayErrors
                if (r14 != 0) goto L_0x003d
                io.reactivex.internal.util.AtomicThrowable r14 = r1.errors
                java.lang.Object r14 = r14.get()
                if (r14 == 0) goto L_0x003d
                r22.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r6 = r1.errors
                java.lang.Throwable r6 = r6.terminate()
                r2.onError(r6)
                return
            L_0x003d:
                r14 = 0
                r17 = r14
                r14 = 0
            L_0x0041:
                if (r14 >= r4) goto L_0x00b4
                r6 = r3[r14]
                r18 = r5[r14]
                if (r18 != 0) goto L_0x00b1
                boolean r10 = r6.done     // Catch:{ Throwable -> 0x0091 }
                io.reactivex.internal.fuseable.SimpleQueue<T> r11 = r6.queue     // Catch:{ Throwable -> 0x0091 }
                if (r11 == 0) goto L_0x0059
                java.lang.Object r18 = r11.poll()     // Catch:{ Throwable -> 0x0054 }
                goto L_0x005b
            L_0x0054:
                r0 = move-exception
                r20 = r6
            L_0x0057:
                r6 = r0
                goto L_0x0095
            L_0x0059:
                r18 = 0
            L_0x005b:
                if (r18 != 0) goto L_0x0060
                r19 = 1
                goto L_0x0062
            L_0x0060:
                r19 = 0
            L_0x0062:
                if (r10 == 0) goto L_0x0085
                if (r19 == 0) goto L_0x0085
                r22.cancelAll()     // Catch:{ Throwable -> 0x0091 }
                io.reactivex.internal.util.AtomicThrowable r15 = r1.errors     // Catch:{ Throwable -> 0x0091 }
                java.lang.Object r15 = r15.get()     // Catch:{ Throwable -> 0x0091 }
                java.lang.Throwable r15 = (java.lang.Throwable) r15     // Catch:{ Throwable -> 0x0091 }
                if (r15 == 0) goto L_0x007f
                r20 = r6
                io.reactivex.internal.util.AtomicThrowable r6 = r1.errors     // Catch:{ Throwable -> 0x008c }
                java.lang.Throwable r6 = r6.terminate()     // Catch:{ Throwable -> 0x008c }
                r2.onError(r6)     // Catch:{ Throwable -> 0x008c }
                goto L_0x0084
            L_0x007f:
                r20 = r6
                r2.onComplete()     // Catch:{ Throwable -> 0x008c }
            L_0x0084:
                return
            L_0x0085:
                r20 = r6
                if (r19 != 0) goto L_0x008e
                r5[r14] = r18     // Catch:{ Throwable -> 0x008c }
                goto L_0x0090
            L_0x008c:
                r0 = move-exception
                goto L_0x0057
            L_0x008e:
                r17 = 1
            L_0x0090:
                goto L_0x00b1
            L_0x0091:
                r0 = move-exception
                r20 = r6
                r6 = r0
            L_0x0095:
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r6)
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                r10.addThrowable(r6)
                boolean r10 = r1.delayErrors
                if (r10 != 0) goto L_0x00ae
                r22.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                java.lang.Throwable r10 = r10.terminate()
                r2.onError(r10)
                return
            L_0x00ae:
                r6 = 1
                r17 = r6
            L_0x00b1:
                int r14 = r14 + 1
                goto L_0x0041
            L_0x00b4:
                if (r17 == 0) goto L_0x00b7
                goto L_0x00ec
            L_0x00b7:
                io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r6 = r1.zipper     // Catch:{ Throwable -> 0x00d5 }
                java.lang.Object r10 = r5.clone()     // Catch:{ Throwable -> 0x00d5 }
                java.lang.Object r6 = r6.apply(r10)     // Catch:{ Throwable -> 0x00d5 }
                java.lang.String r10 = "The zipper returned a null value"
                java.lang.Object r6 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r6, r10)     // Catch:{ Throwable -> 0x00d5 }
                r2.onNext(r6)
                r10 = 1
                long r12 = r12 + r10
                r10 = 0
                java.util.Arrays.fill(r5, r10)
                goto L_0x0019
            L_0x00d5:
                r0 = move-exception
                r6 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r6)
                r22.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                r10.addThrowable(r6)
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                java.lang.Throwable r10 = r10.terminate()
                r2.onError(r10)
                return
            L_0x00ec:
                r10 = 0
                int r6 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
                if (r6 != 0) goto L_0x0183
                boolean r6 = r1.cancelled
                if (r6 == 0) goto L_0x00f6
                return
            L_0x00f6:
                boolean r6 = r1.delayErrors
                if (r6 != 0) goto L_0x010f
                io.reactivex.internal.util.AtomicThrowable r6 = r1.errors
                java.lang.Object r6 = r6.get()
                if (r6 == 0) goto L_0x010f
                r22.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r6 = r1.errors
                java.lang.Throwable r6 = r6.terminate()
                r2.onError(r6)
                return
            L_0x010f:
                r6 = 0
            L_0x0110:
                if (r6 >= r4) goto L_0x0183
                r11 = r3[r6]
                r14 = r5[r6]
                if (r14 != 0) goto L_0x017b
                boolean r14 = r11.done     // Catch:{ Throwable -> 0x015e }
                io.reactivex.internal.fuseable.SimpleQueue<T> r15 = r11.queue     // Catch:{ Throwable -> 0x015e }
                if (r15 == 0) goto L_0x0128
                java.lang.Object r17 = r15.poll()     // Catch:{ Throwable -> 0x0123 }
                goto L_0x012a
            L_0x0123:
                r0 = move-exception
                r21 = r4
            L_0x0126:
                r4 = r0
                goto L_0x0162
            L_0x0128:
                r17 = r10
            L_0x012a:
                if (r17 != 0) goto L_0x012f
                r18 = 1
                goto L_0x0131
            L_0x012f:
                r18 = 0
            L_0x0131:
                if (r14 == 0) goto L_0x0154
                if (r18 == 0) goto L_0x0154
                r22.cancelAll()     // Catch:{ Throwable -> 0x015e }
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors     // Catch:{ Throwable -> 0x015e }
                java.lang.Object r10 = r10.get()     // Catch:{ Throwable -> 0x015e }
                java.lang.Throwable r10 = (java.lang.Throwable) r10     // Catch:{ Throwable -> 0x015e }
                if (r10 == 0) goto L_0x014e
                r21 = r4
                io.reactivex.internal.util.AtomicThrowable r4 = r1.errors     // Catch:{ Throwable -> 0x015b }
                java.lang.Throwable r4 = r4.terminate()     // Catch:{ Throwable -> 0x015b }
                r2.onError(r4)     // Catch:{ Throwable -> 0x015b }
                goto L_0x0153
            L_0x014e:
                r21 = r4
                r2.onComplete()     // Catch:{ Throwable -> 0x015b }
            L_0x0153:
                return
            L_0x0154:
                r21 = r4
                if (r18 != 0) goto L_0x015d
                r5[r6] = r17     // Catch:{ Throwable -> 0x015b }
                goto L_0x015d
            L_0x015b:
                r0 = move-exception
                goto L_0x0126
            L_0x015d:
                goto L_0x017d
            L_0x015e:
                r0 = move-exception
                r21 = r4
                r4 = r0
            L_0x0162:
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                r10.addThrowable(r4)
                boolean r10 = r1.delayErrors
                if (r10 != 0) goto L_0x017d
                r22.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                java.lang.Throwable r10 = r10.terminate()
                r2.onError(r10)
                return
            L_0x017b:
                r21 = r4
            L_0x017d:
                int r6 = r6 + 1
                r4 = r21
                r10 = 0
                goto L_0x0110
            L_0x0183:
                r21 = r4
                r10 = 0
                int r4 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
                if (r4 == 0) goto L_0x01a6
                int r4 = r3.length
                r6 = 0
            L_0x018d:
                if (r6 >= r4) goto L_0x0197
                r10 = r3[r6]
                r10.request(r12)
                int r6 = r6 + 1
                goto L_0x018d
            L_0x0197:
                r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r4 == 0) goto L_0x01a6
                java.util.concurrent.atomic.AtomicLong r4 = r1.requested
                long r10 = -r12
                r4.addAndGet(r10)
            L_0x01a6:
                int r4 = -r7
                int r7 = r1.addAndGet(r4)
                if (r7 != 0) goto L_0x01af
                return
            L_0x01af:
                r4 = r21
                goto L_0x0011
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableZip.ZipCoordinator.drain():void");
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableZip$ZipSubscriber */
    static final class ZipSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -4627193790118206028L;
        volatile boolean done;
        final int limit;
        final ZipCoordinator<T, R> parent;
        final int prefetch;
        long produced;
        SimpleQueue<T> queue;
        int sourceMode;

        ZipSubscriber(ZipCoordinator<T, R> parent2, int prefetch2) {
            this.parent = parent2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> f = (QueueSubscription) s;
                    int m = f.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = f;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = f;
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                s.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 2) {
                this.queue.offer(t);
            }
            this.parent.drain();
        }

        public void onError(Throwable t) {
            this.parent.error(this, t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }

        public void request(long n) {
            if (this.sourceMode != 1) {
                long p = this.produced + n;
                if (p >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(p);
                    return;
                }
                this.produced = p;
            }
        }
    }

    public FlowableZip(Publisher<? extends T>[] sources2, Iterable<? extends Publisher<? extends T>> sourcesIterable2, Function<? super Object[], ? extends R> zipper2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.zipper = zipper2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        Publisher<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new Publisher[8];
            for (Publisher<? extends T> p : this.sourcesIterable) {
                if (count == sources2.length) {
                    Publisher<? extends T>[] b = new Publisher[((count >> 2) + count)];
                    System.arraycopy(sources2, 0, b, 0, count);
                    sources2 = b;
                }
                int count2 = count + 1;
                sources2[count] = p;
                count = count2;
            }
        } else {
            count = sources2.length;
        }
        if (count == 0) {
            EmptySubscription.complete(s);
            return;
        }
        ZipCoordinator<T, R> coordinator = new ZipCoordinator<>(s, this.zipper, count, this.bufferSize, this.delayError);
        s.onSubscribe(coordinator);
        coordinator.subscribe(sources2, count);
    }
}
