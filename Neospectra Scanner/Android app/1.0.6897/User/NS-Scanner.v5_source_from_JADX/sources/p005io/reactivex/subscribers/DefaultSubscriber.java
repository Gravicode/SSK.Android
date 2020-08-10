package p005io.reactivex.subscribers;

import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.subscribers.DefaultSubscriber */
public abstract class DefaultSubscriber<T> implements FlowableSubscriber<T> {

    /* renamed from: s */
    private Subscription f471s;

    public final void onSubscribe(Subscription s) {
        if (EndConsumerHelper.validate(this.f471s, s, getClass())) {
            this.f471s = s;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        Subscription s = this.f471s;
        if (s != null) {
            s.request(n);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription s = this.f471s;
        this.f471s = SubscriptionHelper.CANCELLED;
        s.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(Long.MAX_VALUE);
    }
}
