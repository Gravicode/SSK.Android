package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeout */
public final class FlowableTimeout<T, U, V> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> firstTimeoutIndicator;
    final Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator;
    final Publisher<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeout$TimeoutConsumer */
    static final class TimeoutConsumer extends AtomicReference<Subscription> implements FlowableSubscriber<Object>, Disposable {
        private static final long serialVersionUID = 8708641127342403073L;
        final long idx;
        final TimeoutSelectorSupport parent;

        TimeoutConsumer(long idx2, TimeoutSelectorSupport parent2) {
            this.idx = idx2;
            this.parent = parent2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
        }

        public void onNext(Object t) {
            Subscription upstream = (Subscription) get();
            if (upstream != SubscriptionHelper.CANCELLED) {
                upstream.cancel();
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.onTimeout(this.idx);
            }
        }

        public void onError(Throwable t) {
            if (get() != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.onTimeoutError(this.idx, t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (get() != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.onTimeout(this.idx);
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled((Subscription) get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeout$TimeoutFallbackSubscriber */
    static final class TimeoutFallbackSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T>, TimeoutSelectorSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        long consumed;
        Publisher<? extends T> fallback;
        final AtomicLong index;
        final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
        final SequentialDisposable task = new SequentialDisposable();
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        TimeoutFallbackSubscriber(Subscriber<? super T> actual2, Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator2, Publisher<? extends T> fallback2) {
            this.actual = actual2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
            this.fallback = fallback2;
            this.index = new AtomicLong();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.upstream, s)) {
                setSubscription(s);
            }
        }

        public void onNext(T t) {
            long idx = this.index.get();
            if (idx != Long.MAX_VALUE && this.index.compareAndSet(idx, idx + 1)) {
                Disposable d = (Disposable) this.task.get();
                if (d != null) {
                    d.dispose();
                }
                this.consumed++;
                this.actual.onNext(t);
                try {
                    Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The itemTimeoutIndicator returned a null Publisher.");
                    TimeoutConsumer consumer = new TimeoutConsumer(1 + idx, this);
                    if (this.task.replace(consumer)) {
                        publisher.subscribe(consumer);
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    ((Subscription) this.upstream.get()).cancel();
                    this.index.getAndSet(Long.MAX_VALUE);
                    this.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startFirstTimeout(Publisher<?> firstTimeoutIndicator) {
            if (firstTimeoutIndicator != null) {
                TimeoutConsumer consumer = new TimeoutConsumer(0, this);
                if (this.task.replace(consumer)) {
                    firstTimeoutIndicator.subscribe(consumer);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.task.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.task.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                Publisher<? extends T> f = this.fallback;
                this.fallback = null;
                long c = this.consumed;
                if (c != 0) {
                    produced(c);
                }
                f.subscribe(new FallbackSubscriber(this.actual, this));
            }
        }

        public void onTimeoutError(long idx, Throwable ex) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(ex);
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void cancel() {
            super.cancel();
            this.task.dispose();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeout$TimeoutSelectorSupport */
    interface TimeoutSelectorSupport extends TimeoutSupport {
        void onTimeoutError(long j, Throwable th);
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeout$TimeoutSubscriber */
    static final class TimeoutSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, TimeoutSelectorSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        final Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator;
        final AtomicLong requested = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        TimeoutSubscriber(Subscriber<? super T> actual2, Function<? super T, ? extends Publisher<?>> itemTimeoutIndicator2) {
            this.actual = actual2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
        }

        public void onNext(T t) {
            long idx = get();
            if (idx != Long.MAX_VALUE && compareAndSet(idx, idx + 1)) {
                Disposable d = (Disposable) this.task.get();
                if (d != null) {
                    d.dispose();
                }
                this.actual.onNext(t);
                try {
                    Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The itemTimeoutIndicator returned a null Publisher.");
                    TimeoutConsumer consumer = new TimeoutConsumer(1 + idx, this);
                    if (this.task.replace(consumer)) {
                        publisher.subscribe(consumer);
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    ((Subscription) this.upstream.get()).cancel();
                    getAndSet(Long.MAX_VALUE);
                    this.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startFirstTimeout(Publisher<?> firstTimeoutIndicator) {
            if (firstTimeoutIndicator != null) {
                TimeoutConsumer consumer = new TimeoutConsumer(0, this);
                if (this.task.replace(consumer)) {
                    firstTimeoutIndicator.subscribe(consumer);
                }
            }
        }

        public void onError(Throwable t) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
            }
        }

        public void onTimeout(long idx) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(new TimeoutException());
            }
        }

        public void onTimeoutError(long idx, Throwable ex) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(ex);
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            this.task.dispose();
        }
    }

    public FlowableTimeout(Flowable<T> source, Publisher<U> firstTimeoutIndicator2, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator2, Publisher<? extends T> other2) {
        super(source);
        this.firstTimeoutIndicator = firstTimeoutIndicator2;
        this.itemTimeoutIndicator = itemTimeoutIndicator2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.other == null) {
            TimeoutSubscriber<T> parent = new TimeoutSubscriber<>(s, this.itemTimeoutIndicator);
            s.onSubscribe(parent);
            parent.startFirstTimeout(this.firstTimeoutIndicator);
            this.source.subscribe((FlowableSubscriber<? super T>) parent);
            return;
        }
        TimeoutFallbackSubscriber<T> parent2 = new TimeoutFallbackSubscriber<>(s, this.itemTimeoutIndicator, this.other);
        s.onSubscribe(parent2);
        parent2.startFirstTimeout(this.firstTimeoutIndicator);
        this.source.subscribe((FlowableSubscriber<? super T>) parent2);
    }
}
