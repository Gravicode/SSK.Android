package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.flowable.FlowableLimit */
public final class FlowableLimit<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: n */
    final long f173n;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableLimit$LimitSubscriber */
    static final class LimitSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 2288246011222124525L;
        final Subscriber<? super T> actual;
        long remaining;
        Subscription upstream;

        LimitSubscriber(Subscriber<? super T> actual2, long remaining2) {
            this.actual = actual2;
            this.remaining = remaining2;
            lazySet(remaining2);
        }

        public void onSubscribe(Subscription s) {
            if (!SubscriptionHelper.validate(this.upstream, s)) {
                return;
            }
            if (this.remaining == 0) {
                s.cancel();
                EmptySubscription.complete(this.actual);
                return;
            }
            this.upstream = s;
            this.actual.onSubscribe(this);
        }

        public void onNext(T t) {
            long r = this.remaining;
            if (r > 0) {
                long j = r - 1;
                long r2 = j;
                this.remaining = j;
                this.actual.onNext(t);
                if (r2 == 0) {
                    this.upstream.cancel();
                    this.actual.onComplete();
                }
            }
        }

        public void onError(Throwable t) {
            if (this.remaining > 0) {
                this.remaining = 0;
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.remaining > 0) {
                this.remaining = 0;
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            long r;
            long toRequest;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != 0) {
                        if (r <= n) {
                            toRequest = r;
                        } else {
                            toRequest = n;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, r - toRequest));
                this.upstream.request(toRequest);
            }
        }

        public void cancel() {
            this.upstream.cancel();
        }
    }

    public FlowableLimit(Flowable<T> source, long n) {
        super(source);
        this.f173n = n;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new LimitSubscriber<Object>(s, this.f173n));
    }
}
