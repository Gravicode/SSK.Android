package p008rx.observers;

import java.util.concurrent.atomic.AtomicReference;
import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.annotations.Experimental;
import p008rx.plugins.RxJavaHooks;

@Experimental
/* renamed from: rx.observers.AsyncCompletableSubscriber */
public abstract class AsyncCompletableSubscriber implements CompletableSubscriber, Subscription {
    static final Unsubscribed UNSUBSCRIBED = new Unsubscribed();
    private final AtomicReference<Subscription> upstream = new AtomicReference<>();

    /* renamed from: rx.observers.AsyncCompletableSubscriber$Unsubscribed */
    static final class Unsubscribed implements Subscription {
        Unsubscribed() {
        }

        public void unsubscribe() {
        }

        public boolean isUnsubscribed() {
            return true;
        }
    }

    public final void onSubscribe(Subscription d) {
        if (!this.upstream.compareAndSet(null, d)) {
            d.unsubscribe();
            if (this.upstream.get() != UNSUBSCRIBED) {
                RxJavaHooks.onError(new IllegalStateException("Subscription already set!"));
                return;
            }
            return;
        }
        onStart();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final boolean isUnsubscribed() {
        return this.upstream.get() == UNSUBSCRIBED;
    }

    /* access modifiers changed from: protected */
    public final void clear() {
        this.upstream.set(UNSUBSCRIBED);
    }

    public final void unsubscribe() {
        if (((Subscription) this.upstream.get()) != UNSUBSCRIBED) {
            Subscription current = (Subscription) this.upstream.getAndSet(UNSUBSCRIBED);
            if (current != null && current != UNSUBSCRIBED) {
                current.unsubscribe();
            }
        }
    }
}
