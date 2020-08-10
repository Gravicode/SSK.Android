package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableOperator;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableLift */
public final class ObservableLift<R, T> extends AbstractObservableWithUpstream<T, R> {
    final ObservableOperator<? extends R, ? super T> operator;

    public ObservableLift(ObservableSource<T> source, ObservableOperator<? extends R, ? super T> operator2) {
        super(source);
        this.operator = operator2;
    }

    public void subscribeActual(Observer<? super R> s) {
        try {
            Observer apply = this.operator.apply(s);
            StringBuilder sb = new StringBuilder();
            sb.append("Operator ");
            sb.append(this.operator);
            sb.append(" returned a null Observer");
            this.source.subscribe((Observer) ObjectHelper.requireNonNull(apply, sb.toString()));
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(e2);
            NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            npe.initCause(e2);
            throw npe;
        }
    }
}
