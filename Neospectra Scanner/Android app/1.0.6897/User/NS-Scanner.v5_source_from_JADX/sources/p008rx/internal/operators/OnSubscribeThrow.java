package p008rx.internal.operators;

import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OnSubscribeThrow */
public final class OnSubscribeThrow<T> implements OnSubscribe<T> {
    private final Throwable exception;

    public OnSubscribeThrow(Throwable exception2) {
        this.exception = exception2;
    }

    public void call(Subscriber<? super T> observer) {
        observer.onError(this.exception);
    }
}
