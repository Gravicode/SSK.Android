package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeOperator;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeLift */
public final class MaybeLift<T, R> extends AbstractMaybeWithUpstream<T, R> {
    final MaybeOperator<? extends R, ? super T> operator;

    public MaybeLift(MaybeSource<T> source, MaybeOperator<? extends R, ? super T> operator2) {
        super(source);
        this.operator = operator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> observer) {
        try {
            this.source.subscribe((MaybeObserver) ObjectHelper.requireNonNull(this.operator.apply(observer), "The operator returned a null MaybeObserver"));
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
