package p005io.reactivex.internal.util;

import java.io.Serializable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.util.NotificationLite */
public enum NotificationLite {
    COMPLETE;

    /* renamed from: io.reactivex.internal.util.NotificationLite$DisposableNotification */
    static final class DisposableNotification implements Serializable {
        private static final long serialVersionUID = -7482590109178395495L;

        /* renamed from: d */
        final Disposable f455d;

        DisposableNotification(Disposable d) {
            this.f455d = d;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Disposable[");
            sb.append(this.f455d);
            sb.append("]");
            return sb.toString();
        }
    }

    /* renamed from: io.reactivex.internal.util.NotificationLite$ErrorNotification */
    static final class ErrorNotification implements Serializable {
        private static final long serialVersionUID = -8759979445933046293L;

        /* renamed from: e */
        final Throwable f456e;

        ErrorNotification(Throwable e) {
            this.f456e = e;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Error[");
            sb.append(this.f456e);
            sb.append("]");
            return sb.toString();
        }

        public int hashCode() {
            return this.f456e.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ErrorNotification)) {
                return false;
            }
            return ObjectHelper.equals(this.f456e, ((ErrorNotification) obj).f456e);
        }
    }

    /* renamed from: io.reactivex.internal.util.NotificationLite$SubscriptionNotification */
    static final class SubscriptionNotification implements Serializable {
        private static final long serialVersionUID = -1322257508628817540L;

        /* renamed from: s */
        final Subscription f457s;

        SubscriptionNotification(Subscription s) {
            this.f457s = s;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("NotificationLite.Subscription[");
            sb.append(this.f457s);
            sb.append("]");
            return sb.toString();
        }
    }

    public static <T> Object next(T value) {
        return value;
    }

    public static Object complete() {
        return COMPLETE;
    }

    public static Object error(Throwable e) {
        return new ErrorNotification(e);
    }

    public static Object subscription(Subscription s) {
        return new SubscriptionNotification(s);
    }

    public static Object disposable(Disposable d) {
        return new DisposableNotification(d);
    }

    public static boolean isComplete(Object o) {
        return o == COMPLETE;
    }

    public static boolean isError(Object o) {
        return o instanceof ErrorNotification;
    }

    public static boolean isSubscription(Object o) {
        return o instanceof SubscriptionNotification;
    }

    public static boolean isDisposable(Object o) {
        return o instanceof DisposableNotification;
    }

    public static <T> T getValue(Object o) {
        return o;
    }

    public static Throwable getError(Object o) {
        return ((ErrorNotification) o).f456e;
    }

    public static Subscription getSubscription(Object o) {
        return ((SubscriptionNotification) o).f457s;
    }

    public static Disposable getDisposable(Object o) {
        return ((DisposableNotification) o).f455d;
    }

    public static <T> boolean accept(Object o, Subscriber<? super T> s) {
        if (o == COMPLETE) {
            s.onComplete();
            return true;
        } else if (o instanceof ErrorNotification) {
            s.onError(((ErrorNotification) o).f456e);
            return true;
        } else {
            s.onNext(o);
            return false;
        }
    }

    public static <T> boolean accept(Object o, Observer<? super T> s) {
        if (o == COMPLETE) {
            s.onComplete();
            return true;
        } else if (o instanceof ErrorNotification) {
            s.onError(((ErrorNotification) o).f456e);
            return true;
        } else {
            s.onNext(o);
            return false;
        }
    }

    public static <T> boolean acceptFull(Object o, Subscriber<? super T> s) {
        if (o == COMPLETE) {
            s.onComplete();
            return true;
        } else if (o instanceof ErrorNotification) {
            s.onError(((ErrorNotification) o).f456e);
            return true;
        } else if (o instanceof SubscriptionNotification) {
            s.onSubscribe(((SubscriptionNotification) o).f457s);
            return false;
        } else {
            s.onNext(o);
            return false;
        }
    }

    public static <T> boolean acceptFull(Object o, Observer<? super T> s) {
        if (o == COMPLETE) {
            s.onComplete();
            return true;
        } else if (o instanceof ErrorNotification) {
            s.onError(((ErrorNotification) o).f456e);
            return true;
        } else if (o instanceof DisposableNotification) {
            s.onSubscribe(((DisposableNotification) o).f455d);
            return false;
        } else {
            s.onNext(o);
            return false;
        }
    }

    public String toString() {
        return "NotificationLite.Complete";
    }
}
