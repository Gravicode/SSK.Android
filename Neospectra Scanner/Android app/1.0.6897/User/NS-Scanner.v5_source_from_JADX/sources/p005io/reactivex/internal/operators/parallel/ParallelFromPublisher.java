package p005io.reactivex.internal.operators.parallel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.parallel.ParallelFlowable;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelFromPublisher */
public final class ParallelFromPublisher<T> extends ParallelFlowable<T> {
    final int parallelism;
    final int prefetch;
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.parallel.ParallelFromPublisher$ParallelDispatcher */
    static final class ParallelDispatcher<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4470634016609963609L;
        volatile boolean cancelled;
        volatile boolean done;
        final long[] emissions;
        Throwable error;
        int index;
        final int limit;
        final int prefetch;
        int produced;
        SimpleQueue<T> queue;
        final AtomicLongArray requests;

        /* renamed from: s */
        Subscription f390s;
        int sourceMode;
        final AtomicInteger subscriberCount = new AtomicInteger();
        final Subscriber<? super T>[] subscribers;

        /* renamed from: io.reactivex.internal.operators.parallel.ParallelFromPublisher$ParallelDispatcher$RailSubscription */
        final class RailSubscription implements Subscription {

            /* renamed from: j */
            final int f391j;

            /* renamed from: m */
            final int f392m;

            RailSubscription(int j, int m) {
                this.f391j = j;
                this.f392m = m;
            }

            public void request(long n) {
                long r;
                if (SubscriptionHelper.validate(n)) {
                    AtomicLongArray ra = ParallelDispatcher.this.requests;
                    do {
                        r = ra.get(this.f391j);
                        if (r != Long.MAX_VALUE) {
                        } else {
                            return;
                        }
                    } while (!ra.compareAndSet(this.f391j, r, BackpressureHelper.addCap(r, n)));
                    if (ParallelDispatcher.this.subscriberCount.get() == this.f392m) {
                        ParallelDispatcher.this.drain();
                    }
                }
            }

            public void cancel() {
                if (ParallelDispatcher.this.requests.compareAndSet(this.f391j + this.f392m, 0, 1)) {
                    ParallelDispatcher.this.cancel(this.f392m + this.f392m);
                }
            }
        }

        ParallelDispatcher(Subscriber<? super T>[] subscribers2, int prefetch2) {
            this.subscribers = subscribers2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            int m = subscribers2.length;
            this.requests = new AtomicLongArray(m + m + 1);
            this.requests.lazySet(m + m, (long) m);
            this.emissions = new long[m];
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f390s, s)) {
                this.f390s = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(7);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.done = true;
                        setupSubscribers();
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        setupSubscribers();
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                setupSubscribers();
                s.request((long) this.prefetch);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setupSubscribers() {
            Subscriber<? super T>[] subs = this.subscribers;
            int m = subs.length;
            for (int i = 0; i < m && !this.cancelled; i++) {
                this.subscriberCount.lazySet(i + 1);
                subs[i].onSubscribe(new RailSubscription(i, m));
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                drain();
                return;
            }
            this.f390s.cancel();
            onError(new MissingBackpressureException("Queue is full?"));
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void cancel(int m) {
            if (this.requests.decrementAndGet(m) == 0) {
                this.cancelled = true;
                this.f390s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00d0  */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x00dd  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainAsync() {
            /*
                r28 = this;
                r1 = r28
                r2 = 1
                io.reactivex.internal.fuseable.SimpleQueue<T> r3 = r1.queue
                org.reactivestreams.Subscriber<? super T>[] r4 = r1.subscribers
                java.util.concurrent.atomic.AtomicLongArray r5 = r1.requests
                long[] r6 = r1.emissions
                int r7 = r6.length
                int r8 = r1.index
                int r9 = r1.produced
            L_0x0010:
                r11 = r9
                r9 = r8
                r8 = 0
            L_0x0013:
                boolean r12 = r1.cancelled
                if (r12 == 0) goto L_0x001b
                r3.clear()
                return
            L_0x001b:
                boolean r12 = r1.done
                if (r12 == 0) goto L_0x0033
                java.lang.Throwable r13 = r1.error
                if (r13 == 0) goto L_0x0033
                r3.clear()
                int r14 = r4.length
                r10 = 0
            L_0x0028:
                if (r10 >= r14) goto L_0x0032
                r15 = r4[r10]
                r15.onError(r13)
                int r10 = r10 + 1
                goto L_0x0028
            L_0x0032:
                return
            L_0x0033:
                boolean r13 = r3.isEmpty()
                if (r12 == 0) goto L_0x0048
                if (r13 == 0) goto L_0x0048
                int r14 = r4.length
                r10 = 0
            L_0x003d:
                if (r10 >= r14) goto L_0x0047
                r15 = r4[r10]
                r15.onComplete()
                int r10 = r10 + 1
                goto L_0x003d
            L_0x0047:
                return
            L_0x0048:
                if (r13 == 0) goto L_0x004b
                goto L_0x006a
            L_0x004b:
                long r14 = r5.get(r9)
                r16 = r6[r9]
                int r18 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
                if (r18 == 0) goto L_0x00bb
                int r10 = r7 + r9
                long r19 = r5.get(r10)
                r21 = 0
                int r10 = (r19 > r21 ? 1 : (r19 == r21 ? 0 : -1))
                if (r10 != 0) goto L_0x00bb
                java.lang.Object r10 = r3.poll()     // Catch:{ Throwable -> 0x009c }
                if (r10 != 0) goto L_0x0072
            L_0x006a:
                r23 = r3
                r24 = r5
            L_0x006e:
                r3 = r8
                r8 = r9
                r9 = r11
                goto L_0x00ca
            L_0x0072:
                r23 = r3
                r3 = r4[r9]
                r3.onNext(r10)
                r19 = 1
                long r19 = r16 + r19
                r6[r9] = r19
                int r11 = r11 + 1
                r3 = r11
                r24 = r5
                int r5 = r1.limit
                if (r3 != r5) goto L_0x0096
                r11 = 0
                org.reactivestreams.Subscription r5 = r1.f390s
                r25 = r10
                r26 = r11
                long r10 = (long) r3
                r5.request(r10)
                r11 = r26
                goto L_0x0098
            L_0x0096:
                r25 = r10
            L_0x0098:
                r3 = 0
                r8 = r3
                goto L_0x00c1
            L_0x009c:
                r0 = move-exception
                r23 = r3
                r24 = r5
                r3 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r3)
                org.reactivestreams.Subscription r5 = r1.f390s
                r5.cancel()
                int r5 = r4.length
                r10 = 0
            L_0x00ac:
                if (r10 >= r5) goto L_0x00ba
                r27 = r5
                r5 = r4[r10]
                r5.onError(r3)
                int r10 = r10 + 1
                r5 = r27
                goto L_0x00ac
            L_0x00ba:
                return
            L_0x00bb:
                r23 = r3
                r24 = r5
                int r8 = r8 + 1
            L_0x00c1:
                int r9 = r9 + 1
                if (r9 != r7) goto L_0x00c7
                r3 = 0
                r9 = r3
            L_0x00c7:
                if (r8 != r7) goto L_0x00e5
                goto L_0x006e
            L_0x00ca:
                int r5 = r28.get()
                if (r5 != r2) goto L_0x00dd
                r1.index = r8
                r1.produced = r9
                int r10 = -r2
                int r2 = r1.addAndGet(r10)
                if (r2 != 0) goto L_0x00de
                return
            L_0x00dd:
                r2 = r5
            L_0x00de:
                r3 = r23
                r5 = r24
                goto L_0x0010
            L_0x00e5:
                r3 = r23
                r5 = r24
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.parallel.ParallelFromPublisher.ParallelDispatcher.drainAsync():void");
        }

        /* access modifiers changed from: 0000 */
        public void drainSync() {
            AtomicLongArray r;
            SimpleQueue<T> q;
            int missed = 1;
            SimpleQueue<T> q2 = this.queue;
            Subscriber<? super T>[] a = this.subscribers;
            AtomicLongArray r2 = this.requests;
            long[] e = this.emissions;
            int n = e.length;
            int idx = this.index;
            while (true) {
                int idx2 = idx;
                int notReady = 0;
                while (!this.cancelled) {
                    if (q2.isEmpty()) {
                        for (Subscriber<? super T> s : a) {
                            s.onComplete();
                        }
                        return;
                    }
                    long requestAtIndex = r2.get(idx2);
                    long emissionAtIndex = e[idx2];
                    if (requestAtIndex == emissionAtIndex || r2.get(n + idx2) != 0) {
                        q = q2;
                        r = r2;
                        notReady++;
                    } else {
                        try {
                            T v = q2.poll();
                            if (v == null) {
                                SimpleQueue<T> simpleQueue = q2;
                                int length = a.length;
                                AtomicLongArray atomicLongArray = r2;
                                int i = 0;
                                while (i < length) {
                                    int i2 = length;
                                    a[i].onComplete();
                                    i++;
                                    length = i2;
                                }
                                return;
                            }
                            q = q2;
                            r = r2;
                            a[idx2].onNext(v);
                            e[idx2] = emissionAtIndex + 1;
                            notReady = 0;
                        } catch (Throwable th) {
                            SimpleQueue<T> simpleQueue2 = q2;
                            AtomicLongArray atomicLongArray2 = r2;
                            Throwable ex = th;
                            Exceptions.throwIfFatal(ex);
                            this.f390s.cancel();
                            int length2 = a.length;
                            int i3 = 0;
                            while (i3 < length2) {
                                int i4 = length2;
                                a[i3].onError(ex);
                                i3++;
                                length2 = i4;
                            }
                            return;
                        }
                    }
                    idx2++;
                    if (idx2 == n) {
                        idx2 = 0;
                    }
                    if (notReady == n) {
                        int w = get();
                        if (w == missed) {
                            this.index = idx2;
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else {
                            missed = w;
                        }
                        idx = idx2;
                        q2 = q;
                        r2 = r;
                    } else {
                        q2 = q;
                        r2 = r;
                    }
                }
                q2.clear();
                return;
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                if (this.sourceMode == 1) {
                    drainSync();
                } else {
                    drainAsync();
                }
            }
        }
    }

    public ParallelFromPublisher(Publisher<? extends T> source2, int parallelism2, int prefetch2) {
        this.source = source2;
        this.parallelism = parallelism2;
        this.prefetch = prefetch2;
    }

    public int parallelism() {
        return this.parallelism;
    }

    public void subscribe(Subscriber<? super T>[] subscribers) {
        if (validate(subscribers)) {
            this.source.subscribe(new ParallelDispatcher(subscribers, this.prefetch));
        }
    }
}
