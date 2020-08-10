package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func2;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorWithLatestFrom */
public final class OperatorWithLatestFrom<T, U, R> implements Operator<R, T> {
    static final Object EMPTY = new Object();
    final Observable<? extends U> other;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public OperatorWithLatestFrom(Observable<? extends U> other2, Func2<? super T, ? super U, ? extends R> resultSelector2) {
        this.other = other2;
        this.resultSelector = resultSelector2;
    }

    public Subscriber<? super T> call(Subscriber<? super R> child) {
        final SerializedSubscriber<R> s = new SerializedSubscriber<>(child, false);
        child.add(s);
        final AtomicReference<Object> current = new AtomicReference<>(EMPTY);
        final AtomicReference atomicReference = current;
        final SerializedSubscriber serializedSubscriber = s;
        C16941 r2 = new Subscriber<T>(s, true) {
            public void onNext(T t) {
                Object o = atomicReference.get();
                if (o != OperatorWithLatestFrom.EMPTY) {
                    try {
                        serializedSubscriber.onNext(OperatorWithLatestFrom.this.resultSelector.call(t, o));
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, (Observer<?>) this);
                    }
                }
            }

            public void onError(Throwable e) {
                serializedSubscriber.onError(e);
                serializedSubscriber.unsubscribe();
            }

            public void onCompleted() {
                serializedSubscriber.onCompleted();
                serializedSubscriber.unsubscribe();
            }
        };
        Subscriber<U> otherSubscriber = new Subscriber<U>() {
            public void onNext(U t) {
                current.set(t);
            }

            public void onError(Throwable e) {
                s.onError(e);
                s.unsubscribe();
            }

            public void onCompleted() {
                if (current.get() == OperatorWithLatestFrom.EMPTY) {
                    s.onCompleted();
                    s.unsubscribe();
                }
            }
        };
        s.add(r2);
        s.add(otherSubscriber);
        this.other.unsafeSubscribe(otherSubscriber);
        return r2;
    }
}
