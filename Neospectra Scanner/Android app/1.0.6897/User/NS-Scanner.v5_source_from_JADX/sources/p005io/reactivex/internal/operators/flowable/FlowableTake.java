package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTake */
public final class FlowableTake<T> extends AbstractFlowableWithUpstream<T, T> {
    final long limit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTake$TakeSubscriber */
    static final class TakeSubscriber<T> extends AtomicBoolean implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -5636543848937116287L;
        final Subscriber<? super T> actual;
        boolean done;
        final long limit;
        long remaining;
        Subscription subscription;

        TakeSubscriber(Subscriber<? super T> actual2, long limit2) {
            this.actual = actual2;
            this.limit = limit2;
            this.remaining = limit2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.subscription, s)) {
                this.subscription = s;
                if (this.limit == 0) {
                    s.cancel();
                    this.done = true;
                    EmptySubscription.complete(this.actual);
                    return;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.remaining;
                this.remaining = j - 1;
                if (j > 0) {
                    boolean stop = this.remaining == 0;
                    this.actual.onNext(t);
                    if (stop) {
                        this.subscription.cancel();
                        onComplete();
                    }
                }
            }
        }

        public void onError(Throwable t) {
            if (!this.done) {
                this.done = true;
                this.subscription.cancel();
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                if (get() || !compareAndSet(false, true) || n < this.limit) {
                    this.subscription.request(n);
                } else {
                    this.subscription.request(Long.MAX_VALUE);
                }
            }
        }

        public void cancel() {
            this.subscription.cancel();
        }
    }

    public FlowableTake(Flowable<T> source, long limit2) {
        super(source);
        this.limit = limit2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new TakeSubscriber<Object>(s, this.limit));
    }
}
