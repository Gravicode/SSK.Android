package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.functions.Func2;

/* renamed from: rx.internal.operators.OperatorTakeWhile */
public final class OperatorTakeWhile<T> implements Operator<T, T> {
    final Func2<? super T, ? super Integer, Boolean> predicate;

    public OperatorTakeWhile(final Func1<? super T, Boolean> underlying) {
        this((Func2<? super T, ? super Integer, Boolean>) new Func2<T, Integer, Boolean>() {
            public Boolean call(T input, Integer index) {
                return (Boolean) Func1.this.call(input);
            }
        });
    }

    public OperatorTakeWhile(Func2<? super T, ? super Integer, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Subscriber<T> s = new Subscriber<T>(false, subscriber) {
            private int counter;
            private boolean done;

            public void onNext(T t) {
                try {
                    Func2<? super T, ? super Integer, Boolean> func2 = OperatorTakeWhile.this.predicate;
                    int i = this.counter;
                    this.counter = i + 1;
                    if (((Boolean) func2.call(t, Integer.valueOf(i))).booleanValue()) {
                        subscriber.onNext(t);
                    } else {
                        this.done = true;
                        subscriber.onCompleted();
                        unsubscribe();
                    }
                } catch (Throwable e) {
                    this.done = true;
                    Exceptions.throwOrReport(e, (Observer<?>) subscriber, (Object) t);
                    unsubscribe();
                }
            }

            public void onCompleted() {
                if (!this.done) {
                    subscriber.onCompleted();
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    subscriber.onError(e);
                }
            }
        };
        subscriber.add(s);
        return s;
    }
}
