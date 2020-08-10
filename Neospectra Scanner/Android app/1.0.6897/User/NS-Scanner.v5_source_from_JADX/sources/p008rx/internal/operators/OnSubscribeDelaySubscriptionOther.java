package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.observers.Subscribers;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.SerialSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OnSubscribeDelaySubscriptionOther */
public final class OnSubscribeDelaySubscriptionOther<T, U> implements OnSubscribe<T> {
    final Observable<? extends T> main;
    final Observable<U> other;

    public OnSubscribeDelaySubscriptionOther(Observable<? extends T> main2, Observable<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void call(Subscriber<? super T> t) {
        final SerialSubscription serial = new SerialSubscription();
        t.add(serial);
        final Subscriber<T> child = Subscribers.wrap(t);
        Subscriber<U> otherSubscriber = new Subscriber<U>() {
            boolean done;

            public void onNext(U u) {
                onCompleted();
            }

            public void onError(Throwable e) {
                if (this.done) {
                    RxJavaHooks.onError(e);
                    return;
                }
                this.done = true;
                child.onError(e);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    serial.set(Subscriptions.unsubscribed());
                    OnSubscribeDelaySubscriptionOther.this.main.unsafeSubscribe(child);
                }
            }
        };
        serial.set(otherSubscriber);
        this.other.unsafeSubscribe(otherSubscriber);
    }
}
