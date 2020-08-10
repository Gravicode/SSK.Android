package p008rx.subjects;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;

/* renamed from: rx.subjects.Subject */
public abstract class Subject<T, R> extends Observable<R> implements Observer<T> {
    public abstract boolean hasObservers();

    protected Subject(OnSubscribe<R> onSubscribe) {
        super(onSubscribe);
    }

    public final SerializedSubject<T, R> toSerialized() {
        if (getClass() == SerializedSubject.class) {
            return (SerializedSubject) this;
        }
        return new SerializedSubject<>(this);
    }
}
