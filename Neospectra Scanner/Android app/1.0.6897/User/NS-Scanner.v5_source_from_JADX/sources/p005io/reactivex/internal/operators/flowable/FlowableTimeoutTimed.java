package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed */
public final class FlowableTimeoutTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$FallbackSubscriber */
    static final class FallbackSubscriber<T> implements FlowableSubscriber<T> {
        final Subscriber<? super T> actual;
        final SubscriptionArbiter arbiter;

        FallbackSubscriber(Subscriber<? super T> actual2, SubscriptionArbiter arbiter2) {
            this.actual = actual2;
            this.arbiter = arbiter2;
        }

        public void onSubscribe(Subscription s) {
            this.arbiter.setSubscription(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutFallbackSubscriber */
    static final class TimeoutFallbackSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T>, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        long consumed;
        Publisher<? extends T> fallback;
        final AtomicLong index = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        final Worker worker;

        TimeoutFallbackSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2, Publisher<? extends T> fallback2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.fallback = fallback2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.upstream, s)) {
                setSubscription(s);
            }
        }

        public void onNext(T t) {
            long idx = this.index.get();
            if (idx != Long.MAX_VALUE && this.index.compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.consumed++;
                this.actual.onNext(t);
                startTimeout(1 + idx);
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                long c = this.consumed;
                if (c != 0) {
                    produced(c);
                }
                Publisher<? extends T> f = this.fallback;
                this.fallback = null;
                f.subscribe(new FallbackSubscriber(this.actual, this));
                this.worker.dispose();
            }
        }

        public void cancel() {
            super.cancel();
            this.worker.dispose();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSubscriber */
    static final class TimeoutSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        final AtomicLong requested = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        final Worker worker;

        TimeoutSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
        }

        public void onNext(T t) {
            long idx = get();
            if (idx != Long.MAX_VALUE && compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.actual.onNext(t);
                startTimeout(1 + idx);
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(new TimeoutException());
                this.worker.dispose();
            }
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            this.worker.dispose();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutSupport */
    interface TimeoutSupport {
        void onTimeout(long j);
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeoutTimed$TimeoutTask */
    static final class TimeoutTask implements Runnable {
        final long idx;
        final TimeoutSupport parent;

        TimeoutTask(long idx2, TimeoutSupport parent2) {
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            this.parent.onTimeout(this.idx);
        }
    }

    public FlowableTimeoutTimed(Flowable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, Publisher<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.other == null) {
            TimeoutSubscriber timeoutSubscriber = new TimeoutSubscriber(s, this.timeout, this.unit, this.scheduler.createWorker());
            s.onSubscribe(timeoutSubscriber);
            timeoutSubscriber.startTimeout(0);
            this.source.subscribe((FlowableSubscriber<? super T>) timeoutSubscriber);
            return;
        }
        TimeoutFallbackSubscriber timeoutFallbackSubscriber = new TimeoutFallbackSubscriber(s, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
        s.onSubscribe(timeoutFallbackSubscriber);
        timeoutFallbackSubscriber.startTimeout(0);
        this.source.subscribe((FlowableSubscriber<? super T>) timeoutFallbackSubscriber);
    }
}
