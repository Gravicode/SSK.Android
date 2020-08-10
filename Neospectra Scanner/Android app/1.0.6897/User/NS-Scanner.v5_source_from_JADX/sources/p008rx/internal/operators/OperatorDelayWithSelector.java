package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.observers.SerializedSubscriber;
import p008rx.observers.Subscribers;
import p008rx.subjects.PublishSubject;

/* renamed from: rx.internal.operators.OperatorDelayWithSelector */
public final class OperatorDelayWithSelector<T, V> implements Operator<T, T> {
    final Func1<? super T, ? extends Observable<V>> itemDelay;
    final Observable<? extends T> source;

    public OperatorDelayWithSelector(Observable<? extends T> source2, Func1<? super T, ? extends Observable<V>> itemDelay2) {
        this.source = source2;
        this.itemDelay = itemDelay2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> _child) {
        final SerializedSubscriber<T> child = new SerializedSubscriber<>(_child);
        final PublishSubject<Observable<T>> delayedEmissions = PublishSubject.create();
        _child.add(Observable.merge((Observable<? extends Observable<? extends T>>) delayedEmissions).unsafeSubscribe(Subscribers.from(child)));
        return new Subscriber<T>(_child) {
            public void onCompleted() {
                delayedEmissions.onCompleted();
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(final T t) {
                try {
                    delayedEmissions.onNext(((Observable) OperatorDelayWithSelector.this.itemDelay.call(t)).take(1).defaultIfEmpty(null).map(new Func1<V, T>() {
                        public T call(V v) {
                            return t;
                        }
                    }));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this);
                }
            }
        };
    }
}
