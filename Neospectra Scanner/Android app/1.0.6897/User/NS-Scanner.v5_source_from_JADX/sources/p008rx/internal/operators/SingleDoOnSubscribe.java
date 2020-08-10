package p008rx.internal.operators;

import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.SingleDoOnSubscribe */
public final class SingleDoOnSubscribe<T> implements OnSubscribe<T> {
    final Action0 onSubscribe;
    final OnSubscribe<T> source;

    public SingleDoOnSubscribe(OnSubscribe<T> source2, Action0 onSubscribe2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
    }

    public void call(SingleSubscriber<? super T> t) {
        try {
            this.onSubscribe.call();
            this.source.call(t);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            t.onError(ex);
        }
    }
}
