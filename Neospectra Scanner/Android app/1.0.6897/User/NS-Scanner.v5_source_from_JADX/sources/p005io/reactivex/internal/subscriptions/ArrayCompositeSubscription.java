package p005io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Subscription;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.subscriptions.ArrayCompositeSubscription */
public final class ArrayCompositeSubscription extends AtomicReferenceArray<Subscription> implements Disposable {
    private static final long serialVersionUID = 2746389416410565408L;

    public ArrayCompositeSubscription(int capacity) {
        super(capacity);
    }

    public boolean setResource(int index, Subscription resource) {
        Subscription o;
        do {
            o = (Subscription) get(index);
            if (o == SubscriptionHelper.CANCELLED) {
                if (resource != null) {
                    resource.cancel();
                }
                return false;
            }
        } while (!compareAndSet(index, o, resource));
        if (o != null) {
            o.cancel();
        }
        return true;
    }

    public Subscription replaceResource(int index, Subscription resource) {
        Subscription o;
        do {
            o = (Subscription) get(index);
            if (o == SubscriptionHelper.CANCELLED) {
                if (resource != null) {
                    resource.cancel();
                }
                return null;
            }
        } while (!compareAndSet(index, o, resource));
        return o;
    }

    public void dispose() {
        if (get(0) != SubscriptionHelper.CANCELLED) {
            int s = length();
            for (int i = 0; i < s; i++) {
                if (((Subscription) get(i)) != SubscriptionHelper.CANCELLED) {
                    Subscription o = (Subscription) getAndSet(i, SubscriptionHelper.CANCELLED);
                    if (!(o == SubscriptionHelper.CANCELLED || o == null)) {
                        o.cancel();
                    }
                }
            }
        }
    }

    public boolean isDisposed() {
        return get(0) == SubscriptionHelper.CANCELLED;
    }
}
