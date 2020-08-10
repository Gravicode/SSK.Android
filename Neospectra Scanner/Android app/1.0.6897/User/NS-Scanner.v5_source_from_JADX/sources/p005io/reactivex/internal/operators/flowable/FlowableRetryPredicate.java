package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryPredicate */
public final class FlowableRetryPredicate<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;
    final Predicate<? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRetryPredicate$RetrySubscriber */
    static final class RetrySubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        final Predicate<? super Throwable> predicate;
        long produced;
        long remaining;

        /* renamed from: sa */
        final SubscriptionArbiter f191sa;
        final Publisher<? extends T> source;

        RetrySubscriber(Subscriber<? super T> actual2, long count, Predicate<? super Throwable> predicate2, SubscriptionArbiter sa, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.f191sa = sa;
            this.source = source2;
            this.predicate = predicate2;
            this.remaining = count;
        }

        public void onSubscribe(Subscription s) {
            this.f191sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            long r = this.remaining;
            if (r != Long.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r == 0) {
                this.actual.onError(t);
            } else {
                try {
                    if (!this.predicate.test(t)) {
                        this.actual.onError(t);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.actual.onError(new CompositeException(t, e));
                }
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f191sa.isCancelled()) {
                    long p = this.produced;
                    if (p != 0) {
                        this.produced = 0;
                        this.f191sa.produced(p);
                    }
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public FlowableRetryPredicate(Flowable<T> source, long count2, Predicate<? super Throwable> predicate2) {
        super(source);
        this.predicate = predicate2;
        this.count = count2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        RetrySubscriber<T> rs = new RetrySubscriber<>(s, this.count, this.predicate, sa, this.source);
        rs.subscribeNext();
    }
}
