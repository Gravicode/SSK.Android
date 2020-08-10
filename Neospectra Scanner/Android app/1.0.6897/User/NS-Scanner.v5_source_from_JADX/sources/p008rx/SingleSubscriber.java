package p008rx;

import p008rx.internal.util.SubscriptionList;

/* renamed from: rx.SingleSubscriber */
public abstract class SingleSubscriber<T> implements Subscription {

    /* renamed from: cs */
    private final SubscriptionList f894cs = new SubscriptionList();

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public final void add(Subscription s) {
        this.f894cs.add(s);
    }

    public final void unsubscribe() {
        this.f894cs.unsubscribe();
    }

    public final boolean isUnsubscribed() {
        return this.f894cs.isUnsubscribed();
    }
}
