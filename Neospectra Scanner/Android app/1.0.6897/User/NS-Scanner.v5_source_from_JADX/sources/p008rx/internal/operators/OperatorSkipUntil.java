package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Subscriber;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorSkipUntil */
public final class OperatorSkipUntil<T, U> implements Operator<T, T> {
    final Observable<U> other;

    public OperatorSkipUntil(Observable<U> other2) {
        this.other = other2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final AtomicBoolean gate = new AtomicBoolean();
        Subscriber<U> u = new Subscriber<U>() {
            public void onNext(U u) {
                gate.set(true);
                unsubscribe();
            }

            public void onError(Throwable e) {
                s.onError(e);
                s.unsubscribe();
            }

            public void onCompleted() {
                unsubscribe();
            }
        };
        child.add(u);
        this.other.unsafeSubscribe(u);
        return new Subscriber<T>(child) {
            public void onNext(T t) {
                if (gate.get()) {
                    s.onNext(t);
                } else {
                    request(1);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                unsubscribe();
            }

            public void onCompleted() {
                s.onCompleted();
                unsubscribe();
            }
        };
    }
}
