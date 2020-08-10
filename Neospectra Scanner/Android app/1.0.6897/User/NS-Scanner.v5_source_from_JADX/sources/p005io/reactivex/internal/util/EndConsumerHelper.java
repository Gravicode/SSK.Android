package p005io.reactivex.internal.util;

import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.ProtocolViolationException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.util.EndConsumerHelper */
public final class EndConsumerHelper {
    private EndConsumerHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static boolean validate(Disposable upstream, Disposable next, Class<?> observer) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream == null) {
            return true;
        }
        next.dispose();
        if (upstream != DisposableHelper.DISPOSED) {
            reportDoubleSubscription(observer);
        }
        return false;
    }

    public static boolean setOnce(AtomicReference<Disposable> upstream, Disposable next, Class<?> observer) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream.compareAndSet(null, next)) {
            return true;
        }
        next.dispose();
        if (upstream.get() != DisposableHelper.DISPOSED) {
            reportDoubleSubscription(observer);
        }
        return false;
    }

    public static boolean validate(Subscription upstream, Subscription next, Class<?> subscriber) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream == null) {
            return true;
        }
        next.cancel();
        if (upstream != SubscriptionHelper.CANCELLED) {
            reportDoubleSubscription(subscriber);
        }
        return false;
    }

    public static boolean setOnce(AtomicReference<Subscription> upstream, Subscription next, Class<?> subscriber) {
        ObjectHelper.requireNonNull(next, "next is null");
        if (upstream.compareAndSet(null, next)) {
            return true;
        }
        next.cancel();
        if (upstream.get() != SubscriptionHelper.CANCELLED) {
            reportDoubleSubscription(subscriber);
        }
        return false;
    }

    public static String composeMessage(String consumer) {
        StringBuilder sb = new StringBuilder();
        sb.append("It is not allowed to subscribe with a(n) ");
        sb.append(consumer);
        sb.append(" multiple times. Please create a fresh instance of ");
        sb.append(consumer);
        sb.append(" and subscribe that to the target source instead.");
        return sb.toString();
    }

    public static void reportDoubleSubscription(Class<?> consumer) {
        RxJavaPlugins.onError(new ProtocolViolationException(composeMessage(consumer.getName())));
    }
}
