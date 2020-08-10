package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableOperator;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableLift */
public final class FlowableLift<R, T> extends AbstractFlowableWithUpstream<T, R> {
    final FlowableOperator<? extends R, ? super T> operator;

    public FlowableLift(Flowable<T> source, FlowableOperator<? extends R, ? super T> operator2) {
        super(source);
        this.operator = operator2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        try {
            Subscriber<? super T> st = this.operator.apply(s);
            if (st == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Operator ");
                sb.append(this.operator);
                sb.append(" returned a null Subscriber");
                throw new NullPointerException(sb.toString());
            }
            this.source.subscribe(st);
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
