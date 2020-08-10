package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableReduceWithSingle */
public final class ObservableReduceWithSingle<T, R> extends Single<R> {
    final BiFunction<R, ? super T, R> reducer;
    final Callable<R> seedSupplier;
    final ObservableSource<T> source;

    public ObservableReduceWithSingle(ObservableSource<T> source2, Callable<R> seedSupplier2, BiFunction<R, ? super T, R> reducer2) {
        this.source = source2;
        this.seedSupplier = seedSupplier2;
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> observer) {
        try {
            this.source.subscribe(new ReduceSeedObserver(observer, this.reducer, ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seedSupplier returned a null value")));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
