package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.observers.SerializedSubscriber;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorDebounceWithSelector */
public final class OperatorDebounceWithSelector<T, U> implements Operator<T, T> {
    final Func1<? super T, ? extends Observable<U>> selector;

    public OperatorDebounceWithSelector(Func1<? super T, ? extends Observable<U>> selector2) {
        this.selector = selector2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final SerialSubscription serial = new SerialSubscription();
        child.add(serial);
        return new Subscriber<T>(child) {
            final Subscriber<?> self = this;
            final DebounceState<T> state = new DebounceState<>();

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onNext(T t) {
                try {
                    Observable<U> debouncer = (Observable) OperatorDebounceWithSelector.this.selector.call(t);
                    final int index = this.state.next(t);
                    Subscriber<U> debounceSubscriber = new Subscriber<U>() {
                        public void onNext(U u) {
                            onCompleted();
                        }

                        public void onError(Throwable e) {
                            C15891.this.self.onError(e);
                        }

                        public void onCompleted() {
                            C15891.this.state.emit(index, s, C15891.this.self);
                            unsubscribe();
                        }
                    };
                    serial.set(debounceSubscriber);
                    debouncer.unsafeSubscribe(debounceSubscriber);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
                this.state.clear();
            }

            public void onCompleted() {
                this.state.emitAndComplete(s, this);
            }
        };
    }
}
