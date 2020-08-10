package p008rx.internal.operators;

import java.util.Iterator;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func2;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OperatorZipIterable */
public final class OperatorZipIterable<T1, T2, R> implements Operator<R, T1> {
    final Iterable<? extends T2> iterable;
    final Func2<? super T1, ? super T2, ? extends R> zipFunction;

    public OperatorZipIterable(Iterable<? extends T2> iterable2, Func2<? super T1, ? super T2, ? extends R> zipFunction2) {
        this.iterable = iterable2;
        this.zipFunction = zipFunction2;
    }

    public Subscriber<? super T1> call(final Subscriber<? super R> subscriber) {
        final Iterator<? extends T2> iterator = this.iterable.iterator();
        try {
            if (iterator.hasNext()) {
                return new Subscriber<T1>(subscriber) {
                    boolean done;

                    public void onCompleted() {
                        if (!this.done) {
                            this.done = true;
                            subscriber.onCompleted();
                        }
                    }

                    public void onError(Throwable e) {
                        if (this.done) {
                            Exceptions.throwIfFatal(e);
                            return;
                        }
                        this.done = true;
                        subscriber.onError(e);
                    }

                    public void onNext(T1 t) {
                        if (!this.done) {
                            try {
                                subscriber.onNext(OperatorZipIterable.this.zipFunction.call(t, iterator.next()));
                                if (!iterator.hasNext()) {
                                    onCompleted();
                                }
                            } catch (Throwable e) {
                                Exceptions.throwOrReport(e, (Observer<?>) this);
                            }
                        }
                    }
                };
            }
            subscriber.onCompleted();
            return Subscribers.empty();
        } catch (Throwable e) {
            Exceptions.throwOrReport(e, (Observer<?>) subscriber);
            return Subscribers.empty();
        }
    }
}
