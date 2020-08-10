package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.Subscriber;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther */
public final class SingleOnSubscribeDelaySubscriptionOther<T> implements OnSubscribe<T> {
    final Single<? extends T> main;
    final Observable<?> other;

    public SingleOnSubscribeDelaySubscriptionOther(Single<? extends T> main2, Observable<?> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void call(final SingleSubscriber<? super T> subscriber) {
        final SingleSubscriber<T> child = new SingleSubscriber<T>() {
            public void onSuccess(T value) {
                subscriber.onSuccess(value);
            }

            public void onError(Throwable error) {
                subscriber.onError(error);
            }
        };
        final SerialSubscription serial = new SerialSubscription();
        subscriber.add(serial);
        Subscriber<Object> otherSubscriber = new Subscriber<Object>() {
            boolean done;

            public void onNext(Object t) {
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
                    serial.set(child);
                    SingleOnSubscribeDelaySubscriptionOther.this.main.subscribe(child);
                }
            }
        };
        serial.set(otherSubscriber);
        this.other.subscribe(otherSubscriber);
    }
}
