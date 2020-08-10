package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCount */
public final class FlowableCount<T> extends AbstractFlowableWithUpstream<T, Long> {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCount$CountSubscriber */
    static final class CountSubscriber extends DeferredScalarSubscription<Long> implements FlowableSubscriber<Object> {
        private static final long serialVersionUID = 4973004223787171406L;
        long count;

        /* renamed from: s */
        Subscription f138s;

        CountSubscriber(Subscriber<? super Long> actual) {
            super(actual);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f138s, s)) {
                this.f138s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(Object t) {
            this.count++;
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            complete(Long.valueOf(this.count));
        }

        public void cancel() {
            super.cancel();
            this.f138s.cancel();
        }
    }

    public FlowableCount(Flowable<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Long> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new CountSubscriber<Object>(s));
    }
}
