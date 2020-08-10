package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableError */
public final class ObservableError<T> extends Observable<T> {
    final Callable<? extends Throwable> errorSupplier;

    public ObservableError(Callable<? extends Throwable> errorSupplier2) {
        this.errorSupplier = errorSupplier2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            t = (Throwable) ObjectHelper.requireNonNull(this.errorSupplier.call(), "Callable returned null throwable. Null values are generally not allowed in 2.x operators and sources.");
        } catch (Throwable th) {
            t = th;
            Exceptions.throwIfFatal(t);
        }
        EmptyDisposable.error(t, s);
    }
}
