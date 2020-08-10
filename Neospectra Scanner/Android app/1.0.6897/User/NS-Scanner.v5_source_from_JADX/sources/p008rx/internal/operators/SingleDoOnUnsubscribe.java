package p008rx.internal.operators;

import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.functions.Action0;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.SingleDoOnUnsubscribe */
public final class SingleDoOnUnsubscribe<T> implements OnSubscribe<T> {
    final Action0 onUnsubscribe;
    final OnSubscribe<T> source;

    public SingleDoOnUnsubscribe(OnSubscribe<T> source2, Action0 onUnsubscribe2) {
        this.source = source2;
        this.onUnsubscribe = onUnsubscribe2;
    }

    public void call(SingleSubscriber<? super T> t) {
        t.add(Subscriptions.create(this.onUnsubscribe));
        this.source.call(t);
    }
}
