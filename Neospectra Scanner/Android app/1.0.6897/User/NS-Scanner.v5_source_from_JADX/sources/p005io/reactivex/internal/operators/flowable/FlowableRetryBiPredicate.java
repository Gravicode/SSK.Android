package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate */
public final class FlowableRetryBiPredicate<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiPredicate<? super Integer, ? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate$RetryBiSubscriber */
    static final class RetryBiSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        final BiPredicate<? super Integer, ? super Throwable> predicate;
        long produced;
        int retries;

        /* renamed from: sa */
        final SubscriptionArbiter f190sa;
        final Publisher<? extends T> source;

        RetryBiSubscriber(Subscriber<? super T> actual2, BiPredicate<? super Integer, ? super Throwable> predicate2, SubscriptionArbiter sa, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.f190sa = sa;
            this.source = source2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription s) {
            this.f190sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            try {
                BiPredicate<? super Integer, ? super Throwable> biPredicate = this.predicate;
                int i = this.retries + 1;
                this.retries = i;
                if (!biPredicate.test(Integer.valueOf(i), t)) {
                    this.actual.onError(t);
                } else {
                    subscribeNext();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f190sa.isCancelled()) {
                    long p = this.produced;
                    if (p != 0) {
                        this.produced = 0;
                        this.f190sa.produced(p);
                    }
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public FlowableRetryBiPredicate(Flowable<T> source, BiPredicate<? super Integer, ? super Throwable> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        new RetryBiSubscriber<>(s, this.predicate, sa, this.source).subscribeNext();
    }
}
