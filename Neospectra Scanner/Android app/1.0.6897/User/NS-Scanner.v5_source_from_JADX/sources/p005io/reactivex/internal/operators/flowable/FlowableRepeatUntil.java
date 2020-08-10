package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BooleanSupplier;
import p005io.reactivex.internal.subscriptions.SubscriptionArbiter;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeatUntil */
public final class FlowableRepeatUntil<T> extends AbstractFlowableWithUpstream<T, T> {
    final BooleanSupplier until;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRepeatUntil$RepeatSubscriber */
    static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        long produced;

        /* renamed from: sa */
        final SubscriptionArbiter f188sa;
        final Publisher<? extends T> source;
        final BooleanSupplier stop;

        RepeatSubscriber(Subscriber<? super T> actual2, BooleanSupplier until, SubscriptionArbiter sa, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.f188sa = sa;
            this.source = source2;
            this.stop = until;
        }

        public void onSubscribe(Subscription s) {
            this.f188sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            try {
                if (this.stop.getAsBoolean()) {
                    this.actual.onComplete();
                } else {
                    subscribeNext();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f188sa.isCancelled()) {
                    long p = this.produced;
                    if (p != 0) {
                        this.produced = 0;
                        this.f188sa.produced(p);
                    }
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public FlowableRepeatUntil(Flowable<T> source, BooleanSupplier until2) {
        super(source);
        this.until = until2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        new RepeatSubscriber<>(s, this.until, sa, this.source).subscribeNext();
    }
}
