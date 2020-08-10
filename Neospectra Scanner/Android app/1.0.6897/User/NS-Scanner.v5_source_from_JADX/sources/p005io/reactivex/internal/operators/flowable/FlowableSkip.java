package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkip */
public final class FlowableSkip<T> extends AbstractFlowableWithUpstream<T, T> {

    /* renamed from: n */
    final long f203n;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkip$SkipSubscriber */
    static final class SkipSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        long remaining;

        /* renamed from: s */
        Subscription f204s;

        SkipSubscriber(Subscriber<? super T> actual2, long n) {
            this.actual = actual2;
            this.remaining = n;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f204s, s)) {
                long n = this.remaining;
                this.f204s = s;
                this.actual.onSubscribe(this);
                s.request(n);
            }
        }

        public void onNext(T t) {
            if (this.remaining != 0) {
                this.remaining--;
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long n) {
            this.f204s.request(n);
        }

        public void cancel() {
            this.f204s.cancel();
        }
    }

    public FlowableSkip(Flowable<T> source, long n) {
        super(source);
        this.f203n = n;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SkipSubscriber<Object>(s, this.f203n));
    }
}
