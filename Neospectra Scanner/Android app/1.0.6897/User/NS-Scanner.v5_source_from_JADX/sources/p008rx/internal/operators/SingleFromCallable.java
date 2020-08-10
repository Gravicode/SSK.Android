package p008rx.internal.operators;

import java.util.concurrent.Callable;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.internal.operators.SingleFromCallable */
public final class SingleFromCallable<T> implements OnSubscribe<T> {
    final Callable<? extends T> callable;

    public SingleFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    public void call(SingleSubscriber<? super T> t) {
        try {
            t.onSuccess(this.callable.call());
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            t.onError(ex);
        }
    }
}
