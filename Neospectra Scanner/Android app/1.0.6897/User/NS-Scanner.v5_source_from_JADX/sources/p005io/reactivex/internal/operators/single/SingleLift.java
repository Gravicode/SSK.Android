package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleOperator;
import p005io.reactivex.SingleSource;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleLift */
public final class SingleLift<T, R> extends Single<R> {
    final SingleOperator<? extends R, ? super T> onLift;
    final SingleSource<T> source;

    public SingleLift(SingleSource<T> source2, SingleOperator<? extends R, ? super T> onLift2) {
        this.source = source2;
        this.onLift = onLift2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> s) {
        try {
            this.source.subscribe((SingleObserver) ObjectHelper.requireNonNull(this.onLift.apply(s), "The onLift returned a null SingleObserver"));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, s);
        }
    }
}
