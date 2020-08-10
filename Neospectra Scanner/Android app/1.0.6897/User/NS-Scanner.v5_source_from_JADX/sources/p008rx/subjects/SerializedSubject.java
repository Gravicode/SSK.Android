package p008rx.subjects;

import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;
import p008rx.observers.SerializedObserver;

/* renamed from: rx.subjects.SerializedSubject */
public class SerializedSubject<T, R> extends Subject<T, R> {
    private final Subject<T, R> actual;
    private final SerializedObserver<T> observer;

    public SerializedSubject(final Subject<T, R> actual2) {
        super(new OnSubscribe<R>() {
            public void call(Subscriber<? super R> child) {
                Subject.this.unsafeSubscribe(child);
            }
        });
        this.actual = actual2;
        this.observer = new SerializedObserver<>(actual2);
    }

    public void onCompleted() {
        this.observer.onCompleted();
    }

    public void onError(Throwable e) {
        this.observer.onError(e);
    }

    public void onNext(T t) {
        this.observer.onNext(t);
    }

    public boolean hasObservers() {
        return this.actual.hasObservers();
    }
}
