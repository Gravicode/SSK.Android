package p008rx.observers;

import p008rx.CompletableSubscriber;
import p008rx.Subscription;
import p008rx.annotations.Experimental;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnCompletedFailedException;
import p008rx.exceptions.OnErrorFailedException;
import p008rx.plugins.RxJavaHooks;

@Experimental
/* renamed from: rx.observers.SafeCompletableSubscriber */
public final class SafeCompletableSubscriber implements CompletableSubscriber, Subscription {
    final CompletableSubscriber actual;
    boolean done;

    /* renamed from: s */
    Subscription f930s;

    public SafeCompletableSubscriber(CompletableSubscriber actual2) {
        this.actual = actual2;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                throw new OnCompletedFailedException(ex);
            }
        }
    }

    public void onError(Throwable e) {
        RxJavaHooks.onError(e);
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onError(e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                throw new OnErrorFailedException(new CompositeException(e, ex));
            }
        }
    }

    public void onSubscribe(Subscription d) {
        this.f930s = d;
        try {
            this.actual.onSubscribe(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            d.unsubscribe();
            onError(ex);
        }
    }

    public void unsubscribe() {
        this.f930s.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return this.done || this.f930s.isUnsubscribed();
    }
}
