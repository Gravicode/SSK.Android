package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.internal.fuseable.ScalarCallable;
import p005io.reactivex.internal.operators.observable.ObservableScalarXMap.ScalarDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableJust */
public final class ObservableJust<T> extends Observable<T> implements ScalarCallable<T> {
    private final T value;

    public ObservableJust(T value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        ScalarDisposable<T> sd = new ScalarDisposable<>(s, this.value);
        s.onSubscribe(sd);
        sd.run();
    }

    public T call() {
        return this.value;
    }
}
