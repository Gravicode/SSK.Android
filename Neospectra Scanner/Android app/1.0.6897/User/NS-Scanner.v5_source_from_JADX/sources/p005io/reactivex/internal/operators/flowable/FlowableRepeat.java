package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeat */
public final class FlowableRepeat<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeat$RepeatSubscriber */
    static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        long produced;
        long remaining;

        /* renamed from: sa */
        final SubscriptionArbiter f187sa;
        final Publisher<? extends T> source;

        RepeatSubscriber(Subscriber<? super T> actual2, long count, SubscriptionArbiter sa, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.f187sa = sa;
            this.source = source2;
            this.remaining = count;
        }

        public void onSubscribe(Subscription s) {
            this.f187sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            long r = this.remaining;
            if (r != Long.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f187sa.isCancelled()) {
                    long p = this.produced;
                    if (p != 0) {
                        this.produced = 0;
                        this.f187sa.produced(p);
                    }
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public FlowableRepeat(Flowable<T> source, long count2) {
        super(source);
        this.count = count2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        long j = Long.MAX_VALUE;
        if (this.count != Long.MAX_VALUE) {
            j = this.count - 1;
        }
        RepeatSubscriber<T> rs = new RepeatSubscriber<>(s, j, sa, this.source);
        rs.subscribeNext();
    }
}
