package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposables;

/* renamed from: io.reactivex.internal.operators.single.SingleJust */
public final class SingleJust<T> extends Single<T> {
    final T value;

    public SingleJust(T value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        s.onSubscribe(Disposables.disposed());
        s.onSuccess(this.value);
    }
}
