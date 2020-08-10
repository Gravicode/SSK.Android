package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.internal.util.UtilityFunctions;

/* renamed from: rx.internal.operators.OperatorDistinctUntilChanged */
public final class OperatorDistinctUntilChanged<T, U> implements Operator<T, T>, Func2<U, U, Boolean> {
    final Func2<? super U, ? super U, Boolean> comparator;
    final Func1<? super T, ? extends U> keySelector;

    /* renamed from: rx.internal.operators.OperatorDistinctUntilChanged$Holder */
    static final class Holder {
        static final OperatorDistinctUntilChanged<?, ?> INSTANCE = new OperatorDistinctUntilChanged<>(UtilityFunctions.identity());

        Holder() {
        }
    }

    public static <T> OperatorDistinctUntilChanged<T, T> instance() {
        return Holder.INSTANCE;
    }

    public OperatorDistinctUntilChanged(Func1<? super T, ? extends U> keySelector2) {
        this.keySelector = keySelector2;
        this.comparator = this;
    }

    public OperatorDistinctUntilChanged(Func2<? super U, ? super U, Boolean> comparator2) {
        this.keySelector = UtilityFunctions.identity();
        this.comparator = comparator2;
    }

    public Boolean call(U t1, U t2) {
        return Boolean.valueOf(t1 == t2 || (t1 != null && t1.equals(t2)));
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            boolean hasPrevious;
            U previousKey;

            public void onNext(T t) {
                try {
                    U key = OperatorDistinctUntilChanged.this.keySelector.call(t);
                    U currentKey = this.previousKey;
                    this.previousKey = key;
                    if (this.hasPrevious) {
                        try {
                            if (!((Boolean) OperatorDistinctUntilChanged.this.comparator.call(currentKey, key)).booleanValue()) {
                                child.onNext(t);
                            } else {
                                request(1);
                            }
                        } catch (Throwable e) {
                            Exceptions.throwOrReport(e, (Observer<?>) child, (Object) key);
                        }
                    } else {
                        this.hasPrevious = true;
                        child.onNext(t);
                    }
                } catch (Throwable e2) {
                    Exceptions.throwOrReport(e2, (Observer<?>) child, (Object) t);
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onCompleted() {
                child.onCompleted();
            }
        };
    }
}
