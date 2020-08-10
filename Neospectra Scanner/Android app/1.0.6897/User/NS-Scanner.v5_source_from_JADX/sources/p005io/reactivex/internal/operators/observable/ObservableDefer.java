package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDefer */
public final class ObservableDefer<T> extends Observable<T> {
    final Callable<? extends ObservableSource<? extends T>> supplier;

    public ObservableDefer(Callable<? extends ObservableSource<? extends T>> supplier2) {
        this.supplier = supplier2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            ((ObservableSource) ObjectHelper.requireNonNull(this.supplier.call(), "null ObservableSource supplied")).subscribe(s);
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            EmptyDisposable.error(t, s);
        }
    }
}
