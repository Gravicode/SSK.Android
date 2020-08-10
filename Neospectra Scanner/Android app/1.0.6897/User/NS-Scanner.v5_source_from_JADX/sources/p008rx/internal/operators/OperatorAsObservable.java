package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorAsObservable */
public final class OperatorAsObservable<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorAsObservable$Holder */
    static final class Holder {
        static final OperatorAsObservable<Object> INSTANCE = new OperatorAsObservable<>();

        Holder() {
        }
    }

    public static <T> OperatorAsObservable<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorAsObservable() {
    }

    public Subscriber<? super T> call(Subscriber<? super T> s) {
        return s;
    }
}
