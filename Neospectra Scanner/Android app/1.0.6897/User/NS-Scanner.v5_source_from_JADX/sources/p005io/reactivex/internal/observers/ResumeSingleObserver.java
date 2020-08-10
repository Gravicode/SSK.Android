package p005io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.ResumeSingleObserver */
public final class ResumeSingleObserver<T> implements SingleObserver<T> {
    final SingleObserver<? super T> actual;
    final AtomicReference<Disposable> parent;

    public ResumeSingleObserver(AtomicReference<Disposable> parent2, SingleObserver<? super T> actual2) {
        this.parent = parent2;
        this.actual = actual2;
    }

    public void onSubscribe(Disposable d) {
        DisposableHelper.replace(this.parent, d);
    }

    public void onSuccess(T value) {
        this.actual.onSuccess(value);
    }

    public void onError(Throwable e) {
        this.actual.onError(e);
    }
}
