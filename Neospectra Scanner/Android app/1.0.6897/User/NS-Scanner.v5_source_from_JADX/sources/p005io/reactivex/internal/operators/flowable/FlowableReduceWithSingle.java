package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceWithSingle */
public final class FlowableReduceWithSingle<T, R> extends Single<R> {
    final BiFunction<R, ? super T, R> reducer;
    final Callable<R> seedSupplier;
    final Publisher<T> source;

    public FlowableReduceWithSingle(Publisher<T> source2, Callable<R> seedSupplier2, BiFunction<R, ? super T, R> reducer2) {
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
