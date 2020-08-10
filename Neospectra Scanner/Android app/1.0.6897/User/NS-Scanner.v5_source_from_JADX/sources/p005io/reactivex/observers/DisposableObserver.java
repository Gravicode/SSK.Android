package p005io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.DisposableObserver */
public abstract class DisposableObserver<T> implements Observer<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f461s = new AtomicReference<>();

    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.f461s, s, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final boolean isDisposed() {
        return this.f461s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f461s);
    }
}
