package p005io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.subscribers.SubscriberResourceWrapper */
public final class SubscriberResourceWrapper<T> extends AtomicReference<Disposable> implements FlowableSubscriber<T>, Disposable, Subscription {
    private static final long serialVersionUID = -8612022020200669122L;
    final Subscriber<? super T> actual;
    final AtomicReference<Subscription> subscription = new AtomicReference<>();

    public SubscriberResourceWrapper(Subscriber<? super T> actual2) {
        this.actual = actual2;
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this.subscription, s)) {
            this.actual.onSubscribe(this);
        }
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        DisposableHelper.dispose(this);
        this.actual.onError(t);
    }

    public void onComplete() {
        DisposableHelper.dispose(this);
        this.actual.onComplete();
    }

    public void request(long n) {
        if (SubscriptionHelper.validate(n)) {
            ((Subscription) this.subscription.get()).request(n);
        }
    }

    public void dispose() {
        SubscriptionHelper.cancel(this.subscription);
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return this.subscription.get() == SubscriptionHelper.CANCELLED;
    }

    public void cancel() {
        dispose();
    }

    public void setResource(Disposable resource) {
        DisposableHelper.set(this, resource);
    }
}
