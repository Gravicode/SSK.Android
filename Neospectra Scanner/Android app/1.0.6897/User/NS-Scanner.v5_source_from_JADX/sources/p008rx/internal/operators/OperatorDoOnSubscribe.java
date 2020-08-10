package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OperatorDoOnSubscribe */
public class OperatorDoOnSubscribe<T> implements Operator<T, T> {
    private final Action0 subscribe;

    public OperatorDoOnSubscribe(Action0 subscribe2) {
        this.subscribe = subscribe2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        this.subscribe.call();
        return Subscribers.wrap(child);
    }
}
