package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func0;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeDelaySubscriptionWithSelector */
public final class OnSubscribeDelaySubscriptionWithSelector<T, U> implements OnSubscribe<T> {
    final Observable<? extends T> source;
    final Func0<? extends Observable<U>> subscriptionDelay;

    public OnSubscribeDelaySubscriptionWithSelector(Observable<? extends T> source2, Func0<? extends Observable<U>> subscriptionDelay2) {
        this.source = source2;
        this.subscriptionDelay = subscriptionDelay2;
    }

    public void call(final Subscriber<? super T> child) {
        try {
            ((Observable) this.subscriptionDelay.call()).take(1).unsafeSubscribe(new Subscriber<U>() {
                public void onCompleted() {
                    OnSubscribeDelaySubscriptionWithSelector.this.source.unsafeSubscribe(Subscribers.wrap(child));
                }

                public void onError(Throwable e) {
                    child.onError(e);
                }

                public void onNext(U u) {
                }
            });
        } catch (Throwable e) {
            Exceptions.throwOrReport(e, (Observer<?>) child);
        }
    }
}
