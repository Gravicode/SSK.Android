package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap */
public final class FlowableFlatMap<T, U> extends AbstractFlowableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends Publisher<? extends U>> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap$InnerSubscriber */
    static final class InnerSubscriber<T, U> extends AtomicReference<Subscription> implements FlowableSubscriber<U>, Disposable {
        private static final long serialVersionUID = -4606175640614850599L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;

        /* renamed from: id */
        final long f157id;
        final int limit = (this.bufferSize >> 2);
        final MergeSubscriber<T, U> parent;
        long produced;
        volatile SimpleQueue<U> queue;

        InnerSubscriber(MergeSubscriber<T, U> parent2, long id) {
            this.f157id = id;
            this.parent = parent2;
            this.bufferSize = parent2.bufferSize;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<U> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(7);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qs;
                    }
                }
                s.request((long) this.bufferSize);
            }
        }

        public void onNext(U t) {
            if (this.fusionMode != 2) {
                this.parent.tryEmit(t, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable t) {
            lazySet(SubscriptionHelper.CANCELLED);
            this.parent.innerError(this, t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            if (this.fusionMode != 1) {
                long p = this.produced + n;
                if (p >= ((long) this.limit)) {
                    this.produced = 0;
                    ((Subscription) get()).request(p);
                    return;
                }
                this.produced = p;
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return get() == SubscriptionHelper.CANCELLED;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMap$MergeSubscriber */
    static final class MergeSubscriber<T, U> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        static final InnerSubscriber<?, ?>[] CANCELLED = new InnerSubscriber[0];
        static final InnerSubscriber<?, ?>[] EMPTY = new InnerSubscriber[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Subscriber<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errs = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends Publisher<? extends U>> mapper;
        final int maxConcurrency;
        volatile SimplePlainQueue<U> queue;
        final AtomicLong requested = new AtomicLong();
        int scalarEmitted;
        final int scalarLimit;
        final AtomicReference<InnerSubscriber<?, ?>[]> subscribers = new AtomicReference<>();
        long uniqueId;
        Subscription upstream;

        MergeSubscriber(Subscriber<? super U> actual2, Function<? super T, ? extends Publisher<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
            this.bufferSize = bufferSize2;
            this.scalarLimit = Math.max(1, maxConcurrency2 >> 1);
            this.subscribers.lazySet(EMPTY);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.actual.onSubscribe(this);
                if (this.cancelled) {
                    return;
                }
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    s.request(Long.MAX_VALUE);
                } else {
                    s.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    Publisher<? extends U> p = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                    if (p instanceof Callable) {
                        try {
                            U u = ((Callable) p).call();
                            if (u != null) {
                                tryEmitScalar(u);
                            } else if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                                int i = this.scalarEmitted + 1;
                                this.scalarEmitted = i;
                                if (i == this.scalarLimit) {
                                    this.scalarEmitted = 0;
                                    this.upstream.request((long) this.scalarLimit);
                                }
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            this.errs.addThrowable(ex);
                            drain();
                        }
                    } else {
                        long j = this.uniqueId;
                        this.uniqueId = 1 + j;
                        InnerSubscriber<T, U> inner = new InnerSubscriber<>(this, j);
                        if (addInner(inner)) {
                            p.subscribe(inner);
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.upstream.cancel();
                    onError(e);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean addInner(InnerSubscriber<T, U> inner) {
            InnerSubscriber<?, ?>[] a;
            InnerSubscriber<?, ?>[] b;
            do {
                a = (InnerSubscriber[]) this.subscribers.get();
                if (a == CANCELLED) {
                    inner.dispose();
                    return false;
                }
                int n = a.length;
                b = new InnerSubscriber[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
            } while (!this.subscribers.compareAndSet(a, b));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerSubscriber<T, U> inner) {
            InnerSubscriber<?, ?>[] a;
            InnerSubscriber<?, ?>[] b;
            do {
                a = (InnerSubscriber[]) this.subscribers.get();
                int n = a.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i] == inner) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (n == 1) {
                            b = EMPTY;
                        } else {
                            InnerSubscriber<?, ?>[] b2 = new InnerSubscriber[(n - 1)];
                            System.arraycopy(a, 0, b2, 0, j);
                            System.arraycopy(a, j + 1, b2, j, (n - j) - 1);
                            b = b2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(a, b));
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue<U> getMainQueue() {
            SimplePlainQueue<U> q = this.queue;
            if (q == null) {
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    q = new SpscLinkedArrayQueue<>(this.bufferSize);
                } else {
                    q = new SpscArrayQueue<>(this.maxConcurrency);
                }
                this.queue = q;
            }
            return q;
        }

        /* access modifiers changed from: 0000 */
        public void tryEmitScalar(U value) {
            if (get() == 0 && compareAndSet(0, 1)) {
                long r = this.requested.get();
                SimpleQueue<U> q = this.queue;
                if (r == 0 || (q != null && !q.isEmpty())) {
                    if (q == null) {
                        q = getMainQueue();
                    }
                    if (!q.offer(value)) {
                        onError(new IllegalStateException("Scalar queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(value);
                    if (r != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled) {
                        int i = this.scalarEmitted + 1;
                        this.scalarEmitted = i;
                        if (i == this.scalarLimit) {
                            this.scalarEmitted = 0;
                            this.upstream.request((long) this.scalarLimit);
                        }
                    }
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            } else if (!getMainQueue().offer(value)) {
                onError(new IllegalStateException("Scalar queue full?!"));
                return;
            } else if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public SimpleQueue<U> getInnerQueue(InnerSubscriber<T, U> inner) {
            SimpleQueue<U> q = inner.queue;
            if (q != null) {
                return q;
            }
            SimpleQueue<U> q2 = new SpscArrayQueue<>(this.bufferSize);
            inner.queue = q2;
            return q2;
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(U value, InnerSubscriber<T, U> inner) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue<U> q = inner.queue;
                if (q == null) {
                    q = new SpscArrayQueue<>(this.bufferSize);
                    inner.queue = q;
                }
                if (!q.offer(value)) {
                    onError(new MissingBackpressureException("Inner queue full?!"));
                    return;
                } else if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                long r = this.requested.get();
                SimpleQueue<U> q2 = inner.queue;
                if (r == 0 || (q2 != null && !q2.isEmpty())) {
                    if (q2 == null) {
                        q2 = getInnerQueue(inner);
                    }
                    if (!q2.offer(value)) {
                        onError(new MissingBackpressureException("Inner queue full?!"));
                        return;
                    }
                } else {
                    this.actual.onNext(value);
                    if (r != Long.MAX_VALUE) {
                        this.requested.decrementAndGet();
                    }
                    inner.requestMore(1);
                }
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (this.errs.addThrowable(t)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
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
                this.upstream.cancel();
                disposeAll();
                if (getAndIncrement() == 0) {
                    SimpleQueue<U> q = this.queue;
                    if (q != null) {
                        q.clear();
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:160:0x020a A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x00f7  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r35 = this;
                r1 = r35
                org.reactivestreams.Subscriber<? super U> r2 = r1.actual
                r4 = 1
            L_0x0005:
                boolean r5 = r35.checkTerminate()
                if (r5 == 0) goto L_0x000c
                return
            L_0x000c:
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r5 = r1.queue
                java.util.concurrent.atomic.AtomicLong r6 = r1.requested
                long r6 = r6.get()
                r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r8 != 0) goto L_0x001f
                r8 = 1
                goto L_0x0020
            L_0x001f:
                r8 = 0
            L_0x0020:
                r10 = 0
                r13 = 1
                r15 = 0
                if (r5 == 0) goto L_0x007a
            L_0x0028:
                r17 = 0
                r19 = r4
                r3 = r17
                r17 = r10
                r10 = r6
                r6 = 0
            L_0x0032:
                int r7 = (r10 > r15 ? 1 : (r10 == r15 ? 0 : -1))
                if (r7 == 0) goto L_0x004c
                java.lang.Object r6 = r5.poll()
                boolean r7 = r35.checkTerminate()
                if (r7 == 0) goto L_0x0041
                return
            L_0x0041:
                if (r6 != 0) goto L_0x0044
                goto L_0x004c
            L_0x0044:
                r2.onNext(r6)
                long r17 = r17 + r13
                long r3 = r3 + r13
                long r10 = r10 - r13
                goto L_0x0032
            L_0x004c:
                int r7 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
                if (r7 == 0) goto L_0x0066
                if (r8 == 0) goto L_0x005a
                r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                r21 = r10
                goto L_0x0068
            L_0x005a:
                java.util.concurrent.atomic.AtomicLong r7 = r1.requested
                r21 = r10
                long r9 = -r3
                long r9 = r7.addAndGet(r9)
                r21 = r9
                goto L_0x0068
            L_0x0066:
                r21 = r10
            L_0x0068:
                int r7 = (r21 > r15 ? 1 : (r21 == r15 ? 0 : -1))
                if (r7 == 0) goto L_0x0077
                if (r6 != 0) goto L_0x006f
                goto L_0x0077
            L_0x006f:
                r10 = r17
                r4 = r19
                r6 = r21
                goto L_0x0028
            L_0x0077:
                r6 = r21
                goto L_0x007e
            L_0x007a:
                r19 = r4
                r17 = r10
            L_0x007e:
                boolean r3 = r1.done
                io.reactivex.internal.fuseable.SimplePlainQueue<U> r4 = r1.queue
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableFlatMap$InnerSubscriber<?, ?>[]> r5 = r1.subscribers
                java.lang.Object r5 = r5.get()
                io.reactivex.internal.operators.flowable.FlowableFlatMap$InnerSubscriber[] r5 = (p005io.reactivex.internal.operators.flowable.FlowableFlatMap.InnerSubscriber[]) r5
                int r9 = r5.length
                if (r3 == 0) goto L_0x00ab
                if (r4 == 0) goto L_0x0095
                boolean r10 = r4.isEmpty()
                if (r10 == 0) goto L_0x00ab
            L_0x0095:
                if (r9 != 0) goto L_0x00ab
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errs
                java.lang.Throwable r10 = r10.terminate()
                java.lang.Throwable r11 = p005io.reactivex.internal.util.ExceptionHelper.TERMINATED
                if (r10 == r11) goto L_0x00aa
                if (r10 != 0) goto L_0x00a7
                r2.onComplete()
                goto L_0x00aa
            L_0x00a7:
                r2.onError(r10)
            L_0x00aa:
                return
            L_0x00ab:
                r10 = 0
                if (r9 == 0) goto L_0x0222
                long r12 = r1.lastId
                int r11 = r1.lastIndex
                if (r9 <= r11) goto L_0x00c4
                r14 = r5[r11]
                r23 = r3
                r24 = r4
                long r3 = r14.f157id
                int r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1))
                if (r3 == 0) goto L_0x00c1
                goto L_0x00c8
            L_0x00c1:
                r25 = r6
                goto L_0x00f0
            L_0x00c4:
                r23 = r3
                r24 = r4
            L_0x00c8:
                if (r9 > r11) goto L_0x00cb
                r11 = 0
            L_0x00cb:
                r3 = r11
                r4 = r3
                r3 = 0
            L_0x00ce:
                if (r3 >= r9) goto L_0x00e5
                r14 = r5[r4]
                r25 = r6
                long r6 = r14.f157id
                int r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r6 != 0) goto L_0x00db
                goto L_0x00e7
            L_0x00db:
                int r4 = r4 + 1
                if (r4 != r9) goto L_0x00e0
                r4 = 0
            L_0x00e0:
                int r3 = r3 + 1
                r6 = r25
                goto L_0x00ce
            L_0x00e5:
                r25 = r6
            L_0x00e7:
                r11 = r4
                r1.lastIndex = r4
                r3 = r5[r4]
                long r6 = r3.f157id
                r1.lastId = r6
            L_0x00f0:
                r3 = r11
                r20 = 0
            L_0x00f3:
                r4 = r20
                if (r4 >= r9) goto L_0x020a
                boolean r6 = r35.checkTerminate()
                if (r6 == 0) goto L_0x00fe
                return
            L_0x00fe:
                r6 = r5[r3]
                r7 = 0
            L_0x0101:
                boolean r14 = r35.checkTerminate()
                if (r14 == 0) goto L_0x0108
                return
            L_0x0108:
                io.reactivex.internal.fuseable.SimpleQueue<U> r14 = r6.queue
                if (r14 != 0) goto L_0x0117
                r32 = r2
                r30 = r10
                r29 = r11
                r33 = r12
                goto L_0x01c7
            L_0x0117:
                r20 = r15
            L_0x0119:
                r27 = r20
                int r20 = (r25 > r15 ? 1 : (r25 == r15 ? 0 : -1))
                if (r20 == 0) goto L_0x0187
                java.lang.Object r20 = r14.poll()     // Catch:{ Throwable -> 0x0151 }
                r7 = r20
                if (r7 != 0) goto L_0x0132
                r32 = r2
                r30 = r10
                r29 = r11
                r10 = r27
                goto L_0x018f
            L_0x0132:
                r2.onNext(r7)
                boolean r20 = r35.checkTerminate()
                if (r20 == 0) goto L_0x013c
                return
            L_0x013c:
                r20 = 0
                r20 = 1
                long r25 = r25 - r20
                r30 = r10
                r29 = r11
                r10 = r27
                long r10 = r10 + r20
                r20 = r10
                r11 = r29
                r10 = r30
                goto L_0x0119
            L_0x0151:
                r0 = move-exception
                r30 = r10
                r29 = r11
                r10 = r27
                r31 = r0
                r15 = r31
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r15)
                r6.dispose()
                r32 = r2
                io.reactivex.internal.util.AtomicThrowable r2 = r1.errs
                r2.addThrowable(r15)
                boolean r2 = r1.delayErrors
                if (r2 != 0) goto L_0x0172
                org.reactivestreams.Subscription r2 = r1.upstream
                r2.cancel()
            L_0x0172:
                boolean r2 = r35.checkTerminate()
                if (r2 == 0) goto L_0x0179
                return
            L_0x0179:
                r1.removeInner(r6)
                r2 = 1
                int r4 = r4 + 1
                r10 = r2
                r33 = r12
                r11 = 1
                goto L_0x01fd
            L_0x0187:
                r32 = r2
                r30 = r10
                r29 = r11
                r10 = r27
            L_0x018f:
                r15 = 0
                int r2 = (r10 > r15 ? 1 : (r10 == r15 ? 0 : -1))
                if (r2 == 0) goto L_0x01af
                if (r8 != 0) goto L_0x01a1
                java.util.concurrent.atomic.AtomicLong r2 = r1.requested
                r33 = r12
                long r12 = -r10
                long r12 = r2.addAndGet(r12)
            L_0x01a0:
                goto L_0x01a9
            L_0x01a1:
                r33 = r12
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                goto L_0x01a0
            L_0x01a9:
                r6.requestMore(r10)
                r25 = r12
                goto L_0x01b1
            L_0x01af:
                r33 = r12
            L_0x01b1:
                r12 = 0
                int r2 = (r25 > r12 ? 1 : (r25 == r12 ? 0 : -1))
                if (r2 == 0) goto L_0x01c7
                if (r7 != 0) goto L_0x01ba
                goto L_0x01c7
            L_0x01ba:
                r11 = r29
                r10 = r30
                r2 = r32
                r12 = r33
                r15 = 0
                goto L_0x0101
            L_0x01c7:
                boolean r2 = r6.done
                io.reactivex.internal.fuseable.SimpleQueue<U> r10 = r6.queue
                if (r2 == 0) goto L_0x01e6
                if (r10 == 0) goto L_0x01d5
                boolean r11 = r10.isEmpty()
                if (r11 == 0) goto L_0x01e6
            L_0x01d5:
                r1.removeInner(r6)
                boolean r11 = r35.checkTerminate()
                if (r11 == 0) goto L_0x01df
                return
            L_0x01df:
                r11 = 0
                r11 = 1
                long r17 = r17 + r11
                r13 = 1
                goto L_0x01ea
            L_0x01e6:
                r11 = 1
                r13 = r30
            L_0x01ea:
                r14 = 0
                int r16 = (r25 > r14 ? 1 : (r25 == r14 ? 0 : -1))
                if (r16 != 0) goto L_0x01f6
                r10 = r13
                r6 = r25
                r2 = 1
                goto L_0x0215
            L_0x01f6:
                int r3 = r3 + 1
                if (r3 != r9) goto L_0x01fc
                r2 = 0
                r3 = r2
            L_0x01fc:
                r10 = r13
            L_0x01fd:
                r2 = 1
                int r20 = r4 + 1
                r11 = r29
                r2 = r32
                r12 = r33
                r15 = 0
                goto L_0x00f3
            L_0x020a:
                r32 = r2
                r30 = r10
                r29 = r11
                r33 = r12
                r2 = 1
                r6 = r25
            L_0x0215:
                r1.lastIndex = r3
                r4 = r5[r3]
                long r11 = r4.f157id
                r1.lastId = r11
                r25 = r6
                r3 = r17
                goto L_0x022d
            L_0x0222:
                r32 = r2
                r23 = r3
                r24 = r4
                r25 = r6
                r2 = 1
                r3 = r17
            L_0x022d:
                r6 = 0
                int r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
                if (r6 == 0) goto L_0x023c
                boolean r6 = r1.cancelled
                if (r6 != 0) goto L_0x023c
                org.reactivestreams.Subscription r6 = r1.upstream
                r6.request(r3)
            L_0x023c:
                if (r10 == 0) goto L_0x0245
                r4 = r19
            L_0x0241:
                r2 = r32
                goto L_0x0005
            L_0x0245:
                r6 = r19
                int r7 = -r6
                int r6 = r1.addAndGet(r7)
                if (r6 != 0) goto L_0x0250
                return
            L_0x0250:
                r4 = r6
                goto L_0x0241
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableFlatMap.MergeSubscriber.drainLoop():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.cancelled) {
                clearScalarQueue();
                return true;
            } else if (this.delayErrors || this.errs.get() == null) {
                return false;
            } else {
                clearScalarQueue();
                Throwable ex = this.errs.terminate();
                if (ex != ExceptionHelper.TERMINATED) {
                    this.actual.onError(ex);
                }
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public void clearScalarQueue() {
            SimpleQueue<U> q = this.queue;
            if (q != null) {
                q.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeAll() {
            if (((InnerSubscriber[]) this.subscribers.get()) != CANCELLED) {
                InnerSubscriber<?, ?>[] a = (InnerSubscriber[]) this.subscribers.getAndSet(CANCELLED);
                if (a != CANCELLED) {
                    for (InnerSubscriber<?, ?> inner : a) {
                        inner.dispose();
                    }
                    Throwable ex = this.errs.terminate();
                    if (ex != null && ex != ExceptionHelper.TERMINATED) {
                        RxJavaPlugins.onError(ex);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerSubscriber<T, U> inner, Throwable t) {
            if (this.errs.addThrowable(t)) {
                inner.done = true;
                if (!this.delayErrors) {
                    this.upstream.cancel();
                    for (InnerSubscriber<?, ?> a : (InnerSubscriber[]) this.subscribers.getAndSet(CANCELLED)) {
                        a.dispose();
                    }
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }
    }

    public FlowableFlatMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.maxConcurrency = maxConcurrency2;
        this.bufferSize = bufferSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> s) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
            this.source.subscribe(subscribe(s, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
        }
    }

    public static <T, U> FlowableSubscriber<T> subscribe(Subscriber<? super U> s, Function<? super T, ? extends Publisher<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
        MergeSubscriber mergeSubscriber = new MergeSubscriber(s, mapper2, delayErrors2, maxConcurrency2, bufferSize2);
        return mergeSubscriber;
    }
}
