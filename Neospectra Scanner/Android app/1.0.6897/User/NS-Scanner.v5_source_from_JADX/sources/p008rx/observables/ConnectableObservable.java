package p008rx.observables;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscription;
import p008rx.functions.Action1;
import p008rx.functions.Actions;
import p008rx.internal.operators.OnSubscribeAutoConnect;
import p008rx.internal.operators.OnSubscribeRefCount;

/* renamed from: rx.observables.ConnectableObservable */
public abstract class ConnectableObservable<T> extends Observable<T> {
    public abstract void connect(Action1<? super Subscription> action1);

    protected ConnectableObservable(OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
    }

    public final Subscription connect() {
        final Subscription[] out = new Subscription[1];
        connect(new Action1<Subscription>() {
            public void call(Subscription t1) {
                out[0] = t1;
            }
        });
        return out[0];
    }

    public Observable<T> refCount() {
        return create((OnSubscribe<T>) new OnSubscribeRefCount<T>(this));
    }

    public Observable<T> autoConnect() {
        return autoConnect(1);
    }

    public Observable<T> autoConnect(int numberOfSubscribers) {
        return autoConnect(numberOfSubscribers, Actions.empty());
    }

    public Observable<T> autoConnect(int numberOfSubscribers, Action1<? super Subscription> connection) {
        if (numberOfSubscribers > 0) {
            return create((OnSubscribe<T>) new OnSubscribeAutoConnect<T>(this, numberOfSubscribers, connection));
        }
        connect(connection);
        return this;
    }
}
