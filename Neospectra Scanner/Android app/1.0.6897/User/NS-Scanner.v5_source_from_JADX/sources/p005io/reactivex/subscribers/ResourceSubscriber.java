package p005io.reactivex.subscribers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.ListCompositeDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.subscribers.ResourceSubscriber */
public abstract class ResourceSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    private final AtomicLong missedRequested = new AtomicLong();
    private final ListCompositeDisposable resources = new ListCompositeDisposable();

    /* renamed from: s */
    private final AtomicReference<Subscription> f473s = new AtomicReference<>();

    public final void add(Disposable resource) {
        ObjectHelper.requireNonNull(resource, "resource is null");
        this.resources.add(resource);
    }

    public final void onSubscribe(Subscription s) {
        if (EndConsumerHelper.setOnce(this.f473s, s, getClass())) {
            long r = this.missedRequested.getAndSet(0);
            if (r != 0) {
                s.request(r);
            }
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(Long.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        SubscriptionHelper.deferredRequest(this.f473s, this.missedRequested, n);
    }

    public final void dispose() {
        if (SubscriptionHelper.cancel(this.f473s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return SubscriptionHelper.isCancelled((Subscription) this.f473s.get());
    }
}
