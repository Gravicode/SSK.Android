package p008rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.exceptions.Exceptions;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.SingleFromFuture */
public final class SingleFromFuture<T> implements OnSubscribe<T> {
    final Future<? extends T> future;
    final long timeout;
    final TimeUnit unit;

    public SingleFromFuture(Future<? extends T> future2, long timeout2, TimeUnit unit2) {
        this.future = future2;
        this.timeout = timeout2;
        this.unit = unit2;
    }

    public void call(SingleSubscriber<? super T> t) {
        T v;
        Future<? extends T> f = this.future;
        t.add(Subscriptions.from(f));
        try {
            if (this.timeout == 0) {
                v = f.get();
            } else {
                v = f.get(this.timeout, this.unit);
            }
            t.onSuccess(v);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            t.onError(ex);
        }
    }
}
