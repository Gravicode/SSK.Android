package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorSerialize */
public final class OperatorSerialize<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorSerialize$Holder */
    static final class Holder {
        static final OperatorSerialize<Object> INSTANCE = new OperatorSerialize<>();

        Holder() {
        }
    }

    public static <T> OperatorSerialize<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorSerialize() {
    }

    public Subscriber<? super T> call(final Subscriber<? super T> s) {
        return new SerializedSubscriber(new Subscriber<T>(s) {
            public void onCompleted() {
                s.onCompleted();
            }

            public void onError(Throwable e) {
                s.onError(e);
            }

            public void onNext(T t) {
                s.onNext(t);
            }
        });
    }
}
