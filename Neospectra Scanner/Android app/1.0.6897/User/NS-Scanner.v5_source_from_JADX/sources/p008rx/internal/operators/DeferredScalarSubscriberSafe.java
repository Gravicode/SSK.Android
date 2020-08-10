package p008rx.internal.operators;

import p008rx.Subscriber;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.DeferredScalarSubscriberSafe */
public abstract class DeferredScalarSubscriberSafe<T, R> extends DeferredScalarSubscriber<T, R> {
    protected boolean done;

    public DeferredScalarSubscriberSafe(Subscriber<? super R> actual) {
        super(actual);
    }

    public void onError(Throwable ex) {
        if (!this.done) {
            this.done = true;
            super.onError(ex);
            return;
        }
        RxJavaHooks.onError(ex);
    }

    public void onCompleted() {
        if (!this.done) {
            this.done = true;
            super.onCompleted();
        }
    }
}
