package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromUnsafeSource */
public final class ObservableFromUnsafeSource<T> extends Observable<T> {
    final ObservableSource<T> source;

    public ObservableFromUnsafeSource(ObservableSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(observer);
    }
}
