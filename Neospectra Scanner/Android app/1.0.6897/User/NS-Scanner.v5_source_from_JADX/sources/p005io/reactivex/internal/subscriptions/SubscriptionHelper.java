package p005io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.exceptions.ProtocolViolationException;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.subscriptions.SubscriptionHelper */
public enum SubscriptionHelper implements Subscription {
    CANCELLED;

    public void request(long n) {
    }

    public void cancel() {
    }

    public static boolean validate(Subscription current, Subscription next) {
        if (next == null) {
            RxJavaPlugins.onError(new NullPointerException("next is null"));
            return false;
        } else if (current == null) {
            return true;
        } else {
            next.cancel();
            reportSubscriptionSet();
            return false;
        }
    }

    public static void reportSubscriptionSet() {
        RxJavaPlugins.onError(new ProtocolViolationException("Subscription already set!"));
    }

    public static boolean validate(long n) {
        if (n > 0) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("n > 0 required but it was ");
        sb.append(n);
        RxJavaPlugins.onError(new IllegalArgumentException(sb.toString()));
        return false;
    }

    public static void reportMoreProduced(long n) {
        StringBuilder sb = new StringBuilder();
        sb.append("More produced than requested: ");
        sb.append(n);
        RxJavaPlugins.onError(new ProtocolViolationException(sb.toString()));
    }

    public static boolean isCancelled(Subscription s) {
        return s == CANCELLED;
    }

    public static boolean set(AtomicReference<Subscription> field, Subscription s) {
        Subscription current;
        do {
            current = (Subscription) field.get();
            if (current == CANCELLED) {
                if (s != null) {
                    s.cancel();
                }
                return false;
            }
        } while (!field.compareAndSet(current, s));
        if (current != null) {
            current.cancel();
        }
        return true;
    }

    public static boolean setOnce(AtomicReference<Subscription> field, Subscription s) {
        ObjectHelper.requireNonNull(s, "s is null");
        if (field.compareAndSet(null, s)) {
            return true;
        }
        s.cancel();
        if (field.get() != CANCELLED) {
            reportSubscriptionSet();
        }
        return false;
    }

    public static boolean replace(AtomicReference<Subscription> field, Subscription s) {
        Subscription current;
        do {
            current = (Subscription) field.get();
            if (current == CANCELLED) {
                if (s != null) {
                    s.cancel();
                }
                return false;
            }
        } while (!field.compareAndSet(current, s));
        return true;
    }

    public static boolean cancel(AtomicReference<Subscription> field) {
        if (((Subscription) field.get()) != CANCELLED) {
            Subscription current = (Subscription) field.getAndSet(CANCELLED);
            if (current != CANCELLED) {
                if (current != null) {
                    current.cancel();
                }
                return true;
            }
        }
        return false;
    }

    public static boolean deferredSetOnce(AtomicReference<Subscription> field, AtomicLong requested, Subscription s) {
        if (!setOnce(field, s)) {
            return false;
        }
        long r = requested.getAndSet(0);
        if (r != 0) {
            s.request(r);
        }
        return true;
    }

    public static void deferredRequest(AtomicReference<Subscription> field, AtomicLong requested, long n) {
        Subscription s = (Subscription) field.get();
        if (s != null) {
            s.request(n);
        } else if (validate(n)) {
            BackpressureHelper.add(requested, n);
            Subscription s2 = (Subscription) field.get();
            if (s2 != null) {
                long r = requested.getAndSet(0);
                if (r != 0) {
                    s2.request(r);
                }
            }
        }
    }

    public static boolean setOnce(AtomicReference<Subscription> field, Subscription s, long request) {
        if (!setOnce(field, s)) {
            return false;
        }
        s.request(request);
        return true;
    }
}
