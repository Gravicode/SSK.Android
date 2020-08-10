package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.NeverObservableHolder */
public enum NeverObservableHolder implements OnSubscribe<Object> {
    INSTANCE;
    
    static final Observable<Object> NEVER = null;

    static {
        NEVER = Observable.create((OnSubscribe<T>) INSTANCE);
    }

    public static <T> Observable<T> instance() {
        return NEVER;
    }

    public void call(Subscriber<? super Object> subscriber) {
    }
}
