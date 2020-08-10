package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.functions.Func2;

/* renamed from: rx.internal.operators.OperatorSkipWhile */
public final class OperatorSkipWhile<T> implements Operator<T, T> {
    final Func2<? super T, Integer, Boolean> predicate;

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {
            int index;
            boolean skipping = true;

            public void onNext(T t) {
                if (!this.skipping) {
                    child.onNext(t);
                } else {
                    try {
                        Func2<? super T, Integer, Boolean> func2 = OperatorSkipWhile.this.predicate;
                        int i = this.index;
                        this.index = i + 1;
                        if (!((Boolean) func2.call(t, Integer.valueOf(i))).booleanValue()) {
                            this.skipping = false;
                            child.onNext(t);
                        } else {
                            request(1);
                        }
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, (Observer<?>) child, (Object) t);
                    }
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

    public static <T> Func2<T, Integer, Boolean> toPredicate2(final Func1<? super T, Boolean> predicate2) {
        return new Func2<T, Integer, Boolean>() {
            public Boolean call(T t1, Integer t2) {
                return (Boolean) predicate2.call(t1);
            }
        };
    }
}
