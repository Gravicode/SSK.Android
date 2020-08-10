package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlattenIterable */
public final class FlowableFlattenIterable<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlattenIterable$FlattenIterableSubscriber */
    static final class FlattenIterableSubscriber<T, R> extends BasicIntQueueSubscription<R> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -3096000382929934955L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        int consumed;
        Iterator<? extends R> current;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        int fusionMode;
        final int limit;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        final int prefetch;
        SimpleQueue<T> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f162s;

        FlattenIterableSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f162s, s)) {
                this.f162s = s;
                if (s instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.actual.onSubscribe(this);
                        s.request((long) this.prefetch);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.prefetch);
                this.actual.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.fusionMode != 0 || this.queue.offer(t)) {
                    drain();
                } else {
                    onError(new MissingBackpressureException("Queue is full?!"));
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done || !ExceptionHelper.addThrowable(this.error, t)) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            drain();
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
                this.f162s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x012d, code lost:
            if (r8 == null) goto L_0x0139;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r18 = this;
                r1 = r18
                int r2 = r18.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                org.reactivestreams.Subscriber<? super R> r2 = r1.actual
                io.reactivex.internal.fuseable.SimpleQueue<T> r3 = r1.queue
                int r4 = r1.fusionMode
                r5 = 1
                r6 = 0
                if (r4 == r5) goto L_0x0015
                r4 = 1
                goto L_0x0016
            L_0x0015:
                r4 = 0
            L_0x0016:
                r7 = 1
                java.util.Iterator<? extends R> r8 = r1.current
            L_0x0019:
                r9 = 0
                if (r8 != 0) goto L_0x0086
                boolean r10 = r1.done
                java.lang.Object r11 = r3.poll()     // Catch:{ Throwable -> 0x0068 }
                if (r11 != 0) goto L_0x0028
                r12 = 1
                goto L_0x0029
            L_0x0028:
                r12 = 0
            L_0x0029:
                boolean r13 = r1.checkTerminated(r10, r12, r2, r3)
                if (r13 == 0) goto L_0x0030
                return
            L_0x0030:
                if (r11 == 0) goto L_0x0086
                io.reactivex.functions.Function<? super T, ? extends java.lang.Iterable<? extends R>> r13 = r1.mapper     // Catch:{ Throwable -> 0x004f }
                java.lang.Object r13 = r13.apply(r11)     // Catch:{ Throwable -> 0x004f }
                java.lang.Iterable r13 = (java.lang.Iterable) r13     // Catch:{ Throwable -> 0x004f }
                java.util.Iterator r14 = r13.iterator()     // Catch:{ Throwable -> 0x004f }
                r8 = r14
                boolean r14 = r8.hasNext()     // Catch:{ Throwable -> 0x004f }
                if (r14 != 0) goto L_0x004c
                r8 = 0
                r1.consumedOne(r4)
                goto L_0x0019
            L_0x004c:
                r1.current = r8
                goto L_0x0086
            L_0x004f:
                r0 = move-exception
                r5 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r5)
                org.reactivestreams.Subscription r6 = r1.f162s
                r6.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                p005io.reactivex.internal.util.ExceptionHelper.addThrowable(r6, r5)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                java.lang.Throwable r5 = p005io.reactivex.internal.util.ExceptionHelper.terminate(r6)
                r2.onError(r5)
                return
            L_0x0068:
                r0 = move-exception
                r5 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r5)
                org.reactivestreams.Subscription r6 = r1.f162s
                r6.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                p005io.reactivex.internal.util.ExceptionHelper.addThrowable(r6, r5)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                java.lang.Throwable r5 = p005io.reactivex.internal.util.ExceptionHelper.terminate(r6)
                r1.current = r9
                r3.clear()
                r2.onError(r5)
                return
            L_0x0086:
                if (r8 == 0) goto L_0x0130
                java.util.concurrent.atomic.AtomicLong r10 = r1.requested
                long r10 = r10.get()
                r14 = 0
            L_0x0090:
                int r16 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
                if (r16 == 0) goto L_0x0100
                boolean r5 = r1.done
                boolean r5 = r1.checkTerminated(r5, r6, r2, r3)
                if (r5 == 0) goto L_0x009d
                return
            L_0x009d:
                java.lang.Object r5 = r8.next()     // Catch:{ Throwable -> 0x00e5 }
                java.lang.String r12 = "The iterator returned a null value"
                java.lang.Object r5 = p005io.reactivex.internal.functions.ObjectHelper.requireNonNull(r5, r12)     // Catch:{ Throwable -> 0x00e5 }
                r2.onNext(r5)
                boolean r12 = r1.done
                boolean r12 = r1.checkTerminated(r12, r6, r2, r3)
                if (r12 == 0) goto L_0x00b5
                return
            L_0x00b5:
                r12 = 1
                long r14 = r14 + r12
                boolean r12 = r8.hasNext()     // Catch:{ Throwable -> 0x00ca }
                if (r12 != 0) goto L_0x00c7
                r1.consumedOne(r4)
                r8 = 0
                r1.current = r9
                goto L_0x0100
            L_0x00c7:
                r5 = 1
                goto L_0x0090
            L_0x00ca:
                r0 = move-exception
                r6 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r6)
                r1.current = r9
                org.reactivestreams.Subscription r9 = r1.f162s
                r9.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r9 = r1.error
                p005io.reactivex.internal.util.ExceptionHelper.addThrowable(r9, r6)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r9 = r1.error
                java.lang.Throwable r6 = p005io.reactivex.internal.util.ExceptionHelper.terminate(r9)
                r2.onError(r6)
                return
            L_0x00e5:
                r0 = move-exception
                r5 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r5)
                r1.current = r9
                org.reactivestreams.Subscription r6 = r1.f162s
                r6.cancel()
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                p005io.reactivex.internal.util.ExceptionHelper.addThrowable(r6, r5)
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r6 = r1.error
                java.lang.Throwable r5 = p005io.reactivex.internal.util.ExceptionHelper.terminate(r6)
                r2.onError(r5)
                return
            L_0x0100:
                int r5 = (r14 > r10 ? 1 : (r14 == r10 ? 0 : -1))
                if (r5 != 0) goto L_0x0118
                boolean r5 = r1.done
                boolean r9 = r3.isEmpty()
                if (r9 == 0) goto L_0x0110
                if (r8 != 0) goto L_0x0110
                r9 = 1
                goto L_0x0111
            L_0x0110:
                r9 = 0
            L_0x0111:
                boolean r12 = r1.checkTerminated(r5, r9, r2, r3)
                if (r12 == 0) goto L_0x0118
                return
            L_0x0118:
                r12 = 0
                int r5 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
                if (r5 == 0) goto L_0x012d
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r5 == 0) goto L_0x012d
                java.util.concurrent.atomic.AtomicLong r5 = r1.requested
                long r12 = -r14
                r5.addAndGet(r12)
            L_0x012d:
                if (r8 != 0) goto L_0x0130
                goto L_0x0139
            L_0x0130:
                int r5 = -r7
                int r7 = r1.addAndGet(r5)
                if (r7 != 0) goto L_0x0139
                return
            L_0x0139:
                r5 = 1
                goto L_0x0019
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableFlattenIterable.FlattenIterableSubscriber.drain():void");
        }

        /* access modifiers changed from: 0000 */
        public void consumedOne(boolean enabled) {
            if (enabled) {
                int c = this.consumed + 1;
                if (c == this.limit) {
                    this.consumed = 0;
                    this.f162s.request((long) c);
                    return;
                }
                this.consumed = c;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
            if (this.cancelled) {
                this.current = null;
                q.clear();
                return true;
            }
            if (d) {
                if (((Throwable) this.error.get()) != null) {
                    Throwable ex = ExceptionHelper.terminate(this.error);
                    this.current = null;
                    q.clear();
                    a.onError(ex);
                    return true;
                } else if (empty) {
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            this.current = null;
            this.queue.clear();
        }

        public boolean isEmpty() {
            return this.current == null && this.queue.isEmpty();
        }

        @Nullable
        public R poll() throws Exception {
            Iterator<? extends R> it = this.current;
            while (true) {
                if (it != null) {
                    break;
                }
                T v = this.queue.poll();
                if (v != null) {
                    it = ((Iterable) this.mapper.apply(v)).iterator();
                    if (it.hasNext()) {
                        this.current = it;
                        break;
                    }
                    it = null;
                } else {
                    return null;
                }
            }
            R r = ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value");
            if (!it.hasNext()) {
                this.current = null;
            }
            return r;
        }

        public int requestFusion(int requestedMode) {
            if ((requestedMode & 1) == 0 || this.fusionMode != 1) {
                return 0;
            }
            return 1;
        }
    }

    public FlowableFlattenIterable(Flowable<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper2, int prefetch2) {
        super(source);
        this.mapper = mapper2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        if (this.source instanceof Callable) {
            try {
                T v = ((Callable) this.source).call();
                if (v == null) {
                    EmptySubscription.complete(s);
                    return;
                }
                try {
                    FlowableFromIterable.subscribe(s, ((Iterable) this.mapper.apply(v)).iterator());
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    EmptySubscription.error(ex, s);
                }
            } catch (Throwable ex2) {
                Exceptions.throwIfFatal(ex2);
                EmptySubscription.error(ex2, s);
            }
        } else {
            this.source.subscribe((FlowableSubscriber<? super T>) new FlattenIterableSubscriber<Object>(s, this.mapper, this.prefetch));
        }
    }
}
