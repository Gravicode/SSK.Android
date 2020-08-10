package p005io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.DisposableSingleObserver */
public abstract class DisposableSingleObserver<T> implements SingleObserver<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f462s = new AtomicReference<>();

    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.f462s, s, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final boolean isDisposed() {
        return this.f462s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f462s);
    }
}
