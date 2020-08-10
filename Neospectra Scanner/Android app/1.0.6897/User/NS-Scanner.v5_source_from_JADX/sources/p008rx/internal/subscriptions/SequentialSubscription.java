package p008rx.internal.subscriptions;

import java.util.concurrent.atomic.AtomicReference;
import p008rx.Subscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.subscriptions.SequentialSubscription */
public final class SequentialSubscription extends AtomicReference<Subscription> implements Subscription {
    private static final long serialVersionUID = 995205034283130269L;

    public SequentialSubscription() {
    }

    public SequentialSubscription(Subscription initial) {
        lazySet(initial);
    }

    public Subscription current() {
        Subscription current = (Subscription) super.get();
        if (current == Unsubscribed.INSTANCE) {
            return Subscriptions.unsubscribed();
        }
        return current;
    }

    public boolean update(Subscription next) {
        Subscription current;
        do {
            current = (Subscription) get();
            if (current == Unsubscribed.INSTANCE) {
                if (next != null) {
                    next.unsubscribe();
                }
                return false;
            }
        } while (!compareAndSet(current, next));
        if (current != null) {
            current.unsubscribe();
        }
        return true;
    }

    public boolean replace(Subscription next) {
        Subscription current;
        do {
            current = (Subscription) get();
            if (current == Unsubscribed.INSTANCE) {
                if (next != null) {
                    next.unsubscribe();
                }
                return false;
            }
        } while (!compareAndSet(current, next));
        return true;
    }

    public boolean updateWeak(Subscription next) {
        Subscription current = (Subscription) get();
        boolean z = false;
        if (current == Unsubscribed.INSTANCE) {
            if (next != null) {
                next.unsubscribe();
            }
            return false;
        } else if (compareAndSet(current, next)) {
            return true;
        } else {
            Subscription current2 = (Subscription) get();
            if (next != null) {
                next.unsubscribe();
            }
            if (current2 == Unsubscribed.INSTANCE) {
                z = true;
            }
            return z;
        }
    }

    public boolean replaceWeak(Subscription next) {
        Subscription current = (Subscription) get();
        if (current == Unsubscribed.INSTANCE) {
            if (next != null) {
                next.unsubscribe();
            }
            return false;
        } else if (compareAndSet(current, next) || ((Subscription) get()) != Unsubscribed.INSTANCE) {
            return true;
        } else {
            if (next != null) {
                next.unsubscribe();
            }
            return false;
        }
    }

    public void unsubscribe() {
        if (((Subscription) get()) != Unsubscribed.INSTANCE) {
            Subscription current = (Subscription) getAndSet(Unsubscribed.INSTANCE);
            if (current != null && current != Unsubscribed.INSTANCE) {
                current.unsubscribe();
            }
        }
    }

    public boolean isUnsubscribed() {
        return get() == Unsubscribed.INSTANCE;
    }
}
