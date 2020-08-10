package p005io.reactivex.subscribers;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.subscribers.SafeSubscriber */
public final class SafeSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    final Subscriber<? super T> actual;
    boolean done;

    /* renamed from: s */
    Subscription f474s;

    public SafeSubscriber(Subscriber<? super T> actual2) {
        this.actual = actual2;
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.f474s, s)) {
            this.f474s = s;
            try {
                this.actual.onSubscribe(this);
            } catch (Throwable e1) {
                Exceptions.throwIfFatal(e1);
                RxJavaPlugins.onError(new CompositeException(e, e1));
            }
        }
    }

    public void onNext(T t) {
        if (!this.done) {
            if (this.f474s == null) {
                onNextNoSubscription();
            } else if (t == null) {
                Throwable ex = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                try {
                    this.f474s.cancel();
                    onError(ex);
                } catch (Throwable e1) {
                    Exceptions.throwIfFatal(e1);
                    onError(new CompositeException(ex, e1));
                }
            } else {
                try {
                    this.actual.onNext(t);
                } catch (Throwable e12) {
                    Exceptions.throwIfFatal(e12);
                    onError(new CompositeException(e, e12));
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onNextNoSubscription() {
        this.done = true;
        Throwable ex = new NullPointerException("Subscription not set!");
        try {
            this.actual.onSubscribe(EmptySubscription.INSTANCE);
            try {
                this.actual.onError(ex);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(ex, e));
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(new CompositeException(ex, e2));
        }
    }

    public void onError(Throwable t) {
        if (this.done) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.done = true;
        if (this.f474s == null) {
            Throwable npe = new NullPointerException("Subscription not set!");
            try {
                this.actual.onSubscribe(EmptySubscription.INSTANCE);
                try {
                    this.actual.onError(new CompositeException(t, npe));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(new CompositeException(t, npe, e));
                }
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                RxJavaPlugins.onError(new CompositeException(t, npe, e2));
            }
        } else {
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            try {
                this.actual.onError(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(new CompositeException(t, ex));
            }
        }
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            if (this.f474s == null) {
                onCompleteNoSubscription();
                return;
            }
            try {
                this.actual.onComplete();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void onCompleteNoSubscription() {
        Throwable ex = new NullPointerException("Subscription not set!");
        try {
            this.actual.onSubscribe(EmptySubscription.INSTANCE);
            try {
                this.actual.onError(ex);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(ex, e));
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(new CompositeException(ex, e2));
        }
    }

    public void request(long n) {
        try {
            this.f474s.request(n);
        } catch (Throwable e1) {
            Exceptions.throwIfFatal(e1);
            RxJavaPlugins.onError(new CompositeException(e, e1));
        }
    }

    public void cancel() {
        try {
            this.f474s.cancel();
        } catch (Throwable e1) {
            Exceptions.throwIfFatal(e1);
            RxJavaPlugins.onError(e1);
        }
    }
}
