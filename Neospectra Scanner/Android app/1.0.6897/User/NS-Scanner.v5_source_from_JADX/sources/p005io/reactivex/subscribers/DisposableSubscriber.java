package p005io.reactivex.subscribers;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.subscribers.DisposableSubscriber */
public abstract class DisposableSubscriber<T> implements FlowableSubscriber<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Subscription> f472s = new AtomicReference<>();

    public final void onSubscribe(Subscription s) {
        if (EndConsumerHelper.setOnce(this.f472s, s, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        ((Subscription) this.f472s.get()).request(Long.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        ((Subscription) this.f472s.get()).request(n);
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        dispose();
    }

    public final boolean isDisposed() {
        return this.f472s.get() == SubscriptionHelper.CANCELLED;
    }

    public final void dispose() {
        SubscriptionHelper.cancel(this.f472s);
    }
}
