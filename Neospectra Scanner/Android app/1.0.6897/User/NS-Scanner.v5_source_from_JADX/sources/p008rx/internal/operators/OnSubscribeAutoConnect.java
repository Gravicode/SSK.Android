package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Action1;
import p008rx.observables.ConnectableObservable;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeAutoConnect */
public final class OnSubscribeAutoConnect<T> extends AtomicInteger implements OnSubscribe<T> {
    final Action1<? super Subscription> connection;
    final int numberOfSubscribers;
    final ConnectableObservable<? extends T> source;

    public OnSubscribeAutoConnect(ConnectableObservable<? extends T> source2, int numberOfSubscribers2, Action1<? super Subscription> connection2) {
        if (numberOfSubscribers2 <= 0) {
            throw new IllegalArgumentException("numberOfSubscribers > 0 required");
        }
        this.source = source2;
        this.numberOfSubscribers = numberOfSubscribers2;
        this.connection = connection2;
    }

    public void call(Subscriber<? super T> child) {
        this.source.unsafeSubscribe(Subscribers.wrap(child));
        if (incrementAndGet() == this.numberOfSubscribers) {
            this.source.connect(this.connection);
        }
    }
}
