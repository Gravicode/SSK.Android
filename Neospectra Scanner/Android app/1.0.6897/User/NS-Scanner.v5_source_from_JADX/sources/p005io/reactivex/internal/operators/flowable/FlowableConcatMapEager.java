package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscribers.InnerQueuedSubscriber;
import p005io.reactivex.internal.subscribers.InnerQueuedSubscriberSupport;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager */
public final class FlowableConcatMapEager<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager$ConcatMapEagerDelayErrorSubscriber */
    static final class ConcatMapEagerDelayErrorSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, InnerQueuedSubscriberSupport<R> {
        private static final long serialVersionUID = -4255299542215038287L;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        volatile InnerQueuedSubscriber<R> current;
        volatile boolean done;
        final ErrorMode errorMode;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final int maxConcurrency;
        final int prefetch;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f137s;
        final SpscLinkedArrayQueue<InnerQueuedSubscriber<R>> subscribers;

        ConcatMapEagerDelayErrorSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int maxConcurrency2, int prefetch2, ErrorMode errorMode2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.maxConcurrency = maxConcurrency2;
            this.prefetch = prefetch2;
            this.errorMode = errorMode2;
            this.subscribers = new SpscLinkedArrayQueue<>(Math.min(prefetch2, maxConcurrency2));
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f137s, s)) {
                this.f137s = s;
                this.actual.onSubscribe(this);
                s.request(this.maxConcurrency == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) this.maxConcurrency);
            }
        }

        public void onNext(T t) {
            try {
                Publisher<? extends R> p = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                InnerQueuedSubscriber<R> inner = new InnerQueuedSubscriber<>(this, this.prefetch);
                if (!this.cancelled) {
                    this.subscribers.offer(inner);
                    p.subscribe(inner);
                    if (this.cancelled) {
                        inner.cancel();
                        drainAndCancel();
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f137s.cancel();
                onError(ex);
            }
        }

        public void onError(Throwable t) {
            if (this.errors.addThrowable(t)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f137s.cancel();
                drainAndCancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainAndCancel() {
            if (getAndIncrement() == 0) {
                do {
                    cancelAll();
                } while (decrementAndGet() != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            while (true) {
                InnerQueuedSubscriber innerQueuedSubscriber = (InnerQueuedSubscriber) this.subscribers.poll();
                InnerQueuedSubscriber innerQueuedSubscriber2 = innerQueuedSubscriber;
                if (innerQueuedSubscriber != null) {
                    innerQueuedSubscriber2.cancel();
                } else {
                    return;
                }
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void innerNext(InnerQueuedSubscriber<R> inner, R value) {
            if (inner.queue().offer(value)) {
                drain();
                return;
            }
            inner.cancel();
            innerError(inner, new MissingBackpressureException());
        }

        public void innerError(InnerQueuedSubscriber<R> inner, Throwable e) {
            if (this.errors.addThrowable(e)) {
                inner.setDone();
                if (this.errorMode != ErrorMode.END) {
                    this.f137s.cancel();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void innerComplete(InnerQueuedSubscriber<R> inner) {
            inner.setDone();
            drain();
        }

        /* JADX WARNING: Removed duplicated region for block: B:78:0x0140  */
        /* JADX WARNING: Removed duplicated region for block: B:79:0x0145  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r20 = this;
                r1 = r20
                int r2 = r20.getAndIncrement()
                if (r2 == 0) goto L_0x0009
                return
            L_0x0009:
                r2 = 1
                io.reactivex.internal.subscribers.InnerQueuedSubscriber<R> r3 = r1.current
                org.reactivestreams.Subscriber<? super R> r4 = r1.actual
                io.reactivex.internal.util.ErrorMode r5 = r1.errorMode
            L_0x0010:
                java.util.concurrent.atomic.AtomicLong r6 = r1.requested
                long r6 = r6.get()
                r8 = 0
                if (r3 != 0) goto L_0x0058
                io.reactivex.internal.util.ErrorMode r10 = p005io.reactivex.internal.util.ErrorMode.END
                if (r5 == r10) goto L_0x0035
                io.reactivex.internal.util.AtomicThrowable r10 = r1.errors
                java.lang.Object r10 = r10.get()
                java.lang.Throwable r10 = (java.lang.Throwable) r10
                if (r10 == 0) goto L_0x0035
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r11 = r1.errors
                java.lang.Throwable r11 = r11.terminate()
                r4.onError(r11)
                return
            L_0x0035:
                boolean r10 = r1.done
                io.reactivex.internal.queue.SpscLinkedArrayQueue<io.reactivex.internal.subscribers.InnerQueuedSubscriber<R>> r11 = r1.subscribers
                java.lang.Object r11 = r11.poll()
                r3 = r11
                io.reactivex.internal.subscribers.InnerQueuedSubscriber r3 = (p005io.reactivex.internal.subscribers.InnerQueuedSubscriber) r3
                if (r10 == 0) goto L_0x0054
                if (r3 != 0) goto L_0x0054
                io.reactivex.internal.util.AtomicThrowable r11 = r1.errors
                java.lang.Throwable r11 = r11.terminate()
                if (r11 == 0) goto L_0x0050
                r4.onError(r11)
                goto L_0x0053
            L_0x0050:
                r4.onComplete()
            L_0x0053:
                return
            L_0x0054:
                if (r3 == 0) goto L_0x0058
                r1.current = r3
            L_0x0058:
                r10 = 0
                if (r3 == 0) goto L_0x0127
                io.reactivex.internal.fuseable.SimpleQueue r11 = r3.queue()
                if (r11 == 0) goto L_0x0127
            L_0x0061:
                int r12 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                r15 = 0
                if (r12 == 0) goto L_0x00df
                boolean r12 = r1.cancelled
                if (r12 == 0) goto L_0x006e
                r20.cancelAll()
                return
            L_0x006e:
                io.reactivex.internal.util.ErrorMode r12 = p005io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r5 != r12) goto L_0x008e
                io.reactivex.internal.util.AtomicThrowable r12 = r1.errors
                java.lang.Object r12 = r12.get()
                java.lang.Throwable r12 = (java.lang.Throwable) r12
                if (r12 == 0) goto L_0x008e
                r1.current = r15
                r3.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r13 = r1.errors
                java.lang.Throwable r13 = r13.terminate()
                r4.onError(r13)
                return
            L_0x008e:
                boolean r12 = r3.isDone()
                java.lang.Object r16 = r11.poll()     // Catch:{ Throwable -> 0x00cb }
                r17 = r16
                r13 = r17
                if (r13 != 0) goto L_0x00a0
                r14 = 1
                goto L_0x00a1
            L_0x00a0:
                r14 = 0
            L_0x00a1:
                if (r12 == 0) goto L_0x00b9
                if (r14 == 0) goto L_0x00b9
                r3 = 0
                r1.current = r15
                org.reactivestreams.Subscription r15 = r1.f137s
                r18 = r2
                r19 = r3
                r2 = 1
                r15.request(r2)
                r2 = 1
                r10 = r2
                r3 = r19
                goto L_0x00e1
            L_0x00b9:
                r18 = r2
                if (r14 == 0) goto L_0x00be
                goto L_0x00e1
            L_0x00be:
                r4.onNext(r13)
                r15 = 1
                long r8 = r8 + r15
                r3.requestOne()
                r2 = r18
                goto L_0x0061
            L_0x00cb:
                r0 = move-exception
                r18 = r2
                r2 = r0
                p005io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
                r13 = 0
                r1.current = r13
                r3.cancel()
                r20.cancelAll()
                r4.onError(r2)
                return
            L_0x00df:
                r18 = r2
            L_0x00e1:
                int r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                if (r2 != 0) goto L_0x0129
                boolean r2 = r1.cancelled
                if (r2 == 0) goto L_0x00ed
                r20.cancelAll()
                return
            L_0x00ed:
                io.reactivex.internal.util.ErrorMode r2 = p005io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r5 != r2) goto L_0x010e
                io.reactivex.internal.util.AtomicThrowable r2 = r1.errors
                java.lang.Object r2 = r2.get()
                java.lang.Throwable r2 = (java.lang.Throwable) r2
                if (r2 == 0) goto L_0x010e
                r12 = 0
                r1.current = r12
                r3.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r12 = r1.errors
                java.lang.Throwable r12 = r12.terminate()
                r4.onError(r12)
                return
            L_0x010e:
                boolean r2 = r3.isDone()
                boolean r12 = r11.isEmpty()
                if (r2 == 0) goto L_0x0129
                if (r12 == 0) goto L_0x0129
                r3 = 0
                r13 = 0
                r1.current = r13
                org.reactivestreams.Subscription r13 = r1.f137s
                r14 = 1
                r13.request(r14)
                r10 = 1
                goto L_0x0129
            L_0x0127:
                r18 = r2
            L_0x0129:
                r11 = 0
                int r2 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
                if (r2 == 0) goto L_0x013e
                r11 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r2 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
                if (r2 == 0) goto L_0x013e
                java.util.concurrent.atomic.AtomicLong r2 = r1.requested
                long r11 = -r8
                r2.addAndGet(r11)
            L_0x013e:
                if (r10 == 0) goto L_0x0145
                r2 = r18
                goto L_0x0010
            L_0x0145:
                r2 = r18
                int r11 = -r2
                int r2 = r1.addAndGet(r11)
                if (r2 != 0) goto L_0x0150
                return
            L_0x0150:
                goto L_0x0010
            */
            throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.internal.operators.flowable.FlowableConcatMapEager.ConcatMapEagerDelayErrorSubscriber.drain():void");
        }
    }

    public FlowableConcatMapEager(Flowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper2, int maxConcurrency2, int prefetch2, ErrorMode errorMode2) {
        super(source);
        this.mapper = mapper2;
        this.maxConcurrency = maxConcurrency2;
        this.prefetch = prefetch2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        Flowable flowable = this.source;
        ConcatMapEagerDelayErrorSubscriber concatMapEagerDelayErrorSubscriber = new ConcatMapEagerDelayErrorSubscriber(s, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode);
        flowable.subscribe((FlowableSubscriber<? super T>) concatMapEagerDelayErrorSubscriber);
    }
}
