package p008rx.subscriptions;

import java.util.concurrent.Future;
import p008rx.Subscription;
import p008rx.functions.Action0;

/* renamed from: rx.subscriptions.Subscriptions */
public final class Subscriptions {
    private static final Unsubscribed UNSUBSCRIBED = new Unsubscribed();

    /* renamed from: rx.subscriptions.Subscriptions$FutureSubscription */
    static final class FutureSubscription implements Subscription {

        /* renamed from: f */
        final Future<?> f933f;

        public FutureSubscription(Future<?> f) {
            this.f933f = f;
        }

        public void unsubscribe() {
            this.f933f.cancel(true);
        }

        public boolean isUnsubscribed() {
            return this.f933f.isCancelled();
        }
    }

    /* renamed from: rx.subscriptions.Subscriptions$Unsubscribed */
    static final class Unsubscribed implements Subscription {
        Unsubscribed() {
        }

        public void unsubscribe() {
        }

        public boolean isUnsubscribed() {
            return true;
        }
    }

    private Subscriptions() {
        throw new IllegalStateException("No instances!");
    }

    public static Subscription empty() {
        return BooleanSubscription.create();
    }

    public static Subscription unsubscribed() {
        return UNSUBSCRIBED;
    }

    public static Subscription create(Action0 unsubscribe) {
        return BooleanSubscription.create(unsubscribe);
    }

    public static Subscription from(Future<?> f) {
        return new FutureSubscription(f);
    }

    public static CompositeSubscription from(Subscription... subscriptions) {
        return new CompositeSubscription(subscriptions);
    }
}
