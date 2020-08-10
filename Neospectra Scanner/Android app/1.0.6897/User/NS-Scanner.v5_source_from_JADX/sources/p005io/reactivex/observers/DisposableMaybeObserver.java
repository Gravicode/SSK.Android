package p005io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.DisposableMaybeObserver */
public abstract class DisposableMaybeObserver<T> implements MaybeObserver<T>, Disposable {

    /* renamed from: s */
    final AtomicReference<Disposable> f460s = new AtomicReference<>();

    public final void onSubscribe(@NonNull Disposable s) {
        if (EndConsumerHelper.setOnce(this.f460s, s, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final boolean isDisposed() {
        return this.f460s.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.f460s);
    }
}
