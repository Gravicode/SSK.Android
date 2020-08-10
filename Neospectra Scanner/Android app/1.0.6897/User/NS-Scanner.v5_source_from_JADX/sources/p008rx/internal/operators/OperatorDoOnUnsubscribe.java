package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.Subscribers;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorDoOnUnsubscribe */
public class OperatorDoOnUnsubscribe<T> implements Operator<T, T> {
    private final Action0 unsubscribe;

    public OperatorDoOnUnsubscribe(Action0 unsubscribe2) {
        this.unsubscribe = unsubscribe2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        child.add(Subscriptions.create(this.unsubscribe));
        return Subscribers.wrap(child);
    }
}
