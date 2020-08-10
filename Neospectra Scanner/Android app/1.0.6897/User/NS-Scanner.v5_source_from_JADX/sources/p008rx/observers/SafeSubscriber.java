package p008rx.observers;

import java.util.Arrays;
import java.util.Collection;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorFailedException;
import p008rx.exceptions.OnErrorNotImplementedException;
import p008rx.exceptions.UnsubscribeFailedException;
import p008rx.plugins.RxJavaHooks;
import p008rx.plugins.RxJavaPlugins;

/* renamed from: rx.observers.SafeSubscriber */
public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done;

    public SafeSubscriber(Subscriber<? super T> actual2) {
        super(actual2);
        this.actual = actual2;
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            try {
                this.actual.onCompleted();
                try {
                    unsubscribe();
                } catch (Throwable e) {
                    RxJavaHooks.onError(e);
                    throw new UnsubscribeFailedException(e.getMessage(), e);
                }
            } catch (Throwable e2) {
                RxJavaHooks.onError(e2);
                throw new UnsubscribeFailedException(e2.getMessage(), e2);
            }
        }
    }

    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.done) {
            this.done = true;
            _onError(e);
        }
    }

    public void onNext(T args) {
        try {
            if (!this.done) {
                this.actual.onNext(args);
            }
        } catch (Throwable e) {
            Exceptions.throwOrReport(e, (Observer<?>) this);
        }
    }

    /* access modifiers changed from: protected */
    public void _onError(Throwable e) {
        RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
        try {
            this.actual.onError(e);
            try {
                unsubscribe();
            } catch (Throwable unsubscribeException) {
                RxJavaHooks.onError(unsubscribeException);
                throw new OnErrorFailedException(unsubscribeException);
            }
        } catch (OnErrorNotImplementedException e2) {
            unsubscribe();
            throw e2;
        } catch (Throwable unsubscribeException2) {
            RxJavaHooks.onError(unsubscribeException2);
            throw new OnErrorNotImplementedException("Observer.onError not implemented and error while unsubscribing.", new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, unsubscribeException2})));
        }
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
