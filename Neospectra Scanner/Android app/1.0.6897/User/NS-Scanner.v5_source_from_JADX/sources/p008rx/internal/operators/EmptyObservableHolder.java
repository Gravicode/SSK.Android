package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.EmptyObservableHolder */
public enum EmptyObservableHolder implements OnSubscribe<Object> {
    INSTANCE;
    
    static final Observable<Object> EMPTY = null;

    static {
        EMPTY = Observable.create((OnSubscribe<T>) INSTANCE);
    }

    public static <T> Observable<T> instance() {
        return EMPTY;
    }

    public void call(Subscriber<? super Object> child) {
        child.onCompleted();
    }
}
