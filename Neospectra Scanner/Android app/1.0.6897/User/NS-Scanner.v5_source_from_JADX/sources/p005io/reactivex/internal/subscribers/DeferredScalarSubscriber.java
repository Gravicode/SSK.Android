package p005io.reactivex.internal.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.subscribers.DeferredScalarSubscriber */
public abstract class DeferredScalarSubscriber<T, R> extends DeferredScalarSubscription<R> implements FlowableSubscriber<T> {
    private static final long serialVersionUID = 2984505488220891551L;
    protected boolean hasValue;

    /* renamed from: s */
    protected Subscription f433s;

    public DeferredScalarSubscriber(Subscriber<? super R> actual) {
        super(actual);
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.f433s, s)) {
            this.f433s = s;
            this.actual.onSubscribe(this);
            s.request(Long.MAX_VALUE);
        }
    }

    public void onError(Throwable t) {
        this.value = null;
        this.actual.onError(t);
    }

    public void onComplete() {
        if (this.hasValue) {
            complete(this.value);
        } else {
            this.actual.onComplete();
        }
    }

    public void cancel() {
        super.cancel();
        this.f433s.cancel();
    }
}
