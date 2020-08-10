package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeDefer */
public final class OnSubscribeDefer<T> implements OnSubscribe<T> {
    final Func0<? extends Observable<? extends T>> observableFactory;

    public OnSubscribeDefer(Func0<? extends Observable<? extends T>> observableFactory2) {
        this.observableFactory = observableFactory2;
    }

    public void call(Subscriber<? super T> s) {
        try {
            ((Observable) this.observableFactory.call()).unsafeSubscribe(Subscribers.wrap(s));
        } catch (Throwable t) {
            Exceptions.throwOrReport(t, (Observer<?>) s);
        }
    }
}
