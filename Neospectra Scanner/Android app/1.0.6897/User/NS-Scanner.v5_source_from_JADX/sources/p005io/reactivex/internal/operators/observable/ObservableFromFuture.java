package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.DeferredScalarDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromFuture */
public final class ObservableFromFuture<T> extends Observable<T> {
    final Future<? extends T> future;
    final long timeout;
    final TimeUnit unit;

    public ObservableFromFuture(Future<? extends T> future2, long timeout2, TimeUnit unit2) {
        this.future = future2;
        this.timeout = timeout2;
        this.unit = unit2;
    }

    public void subscribeActual(Observer<? super T> s) {
        DeferredScalarDisposable<T> d = new DeferredScalarDisposable<>(s);
        s.onSubscribe(d);
        if (!d.isDisposed()) {
            try {
                d.complete(ObjectHelper.requireNonNull(this.unit != null ? this.future.get(this.timeout, this.unit) : this.future.get(), "Future returned null"));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                if (!d.isDisposed()) {
                    s.onError(ex);
                }
            }
        }
    }
}
