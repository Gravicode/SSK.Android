package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.fuseable.ScalarCallable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableEmpty */
public final class ObservableEmpty extends Observable<Object> implements ScalarCallable<Object> {
    public static final Observable<Object> INSTANCE = new ObservableEmpty();

    private ObservableEmpty() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Object> o) {
        EmptyDisposable.complete(o);
    }

    public Object call() {
        return null;
    }
}
