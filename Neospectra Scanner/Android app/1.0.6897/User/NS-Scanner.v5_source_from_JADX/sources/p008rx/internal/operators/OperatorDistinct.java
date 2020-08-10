package p008rx.internal.operators;

import java.util.HashSet;
import java.util.Set;
import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.functions.Func1;
import p008rx.internal.util.UtilityFunctions;

/* renamed from: rx.internal.operators.OperatorDistinct */
public final class OperatorDistinct<T, U> implements Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    /* renamed from: rx.internal.operators.OperatorDistinct$Holder */
    static final class Holder {
        static final OperatorDistinct<?, ?> INSTANCE = new OperatorDistinct<>(UtilityFunctions.identity());

        Holder() {
        }
    }

    public static <T> OperatorDistinct<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinct(Func1<? super T, ? extends U> keySelector2) {
        this.keySelector = keySelector2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            Set<U> keyMemory = new HashSet();

            public void onNext(T t) {
                if (this.keyMemory.add(OperatorDistinct.this.keySelector.call(t))) {
                    child.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                this.keyMemory = null;
                child.onError(e);
            }

            public void onCompleted() {
                this.keyMemory = null;
                child.onCompleted();
            }
        };
    }
}
