package p005io.reactivex.internal.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.disposables.SequentialDisposable */
public final class SequentialDisposable extends AtomicReference<Disposable> implements Disposable {
    private static final long serialVersionUID = -754898800686245608L;

    public SequentialDisposable() {
    }

    public SequentialDisposable(Disposable initial) {
        lazySet(initial);
    }

    public boolean update(Disposable next) {
        return DisposableHelper.set(this, next);
    }

    public boolean replace(Disposable next) {
        return DisposableHelper.replace(this, next);
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) get());
    }
}
