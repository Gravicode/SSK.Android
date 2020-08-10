package p005io.reactivex.internal.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Cancellable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.disposables.CancellableDisposable */
public final class CancellableDisposable extends AtomicReference<Cancellable> implements Disposable {
    private static final long serialVersionUID = 5718521705281392066L;

    public CancellableDisposable(Cancellable cancellable) {
        super(cancellable);
    }

    public boolean isDisposed() {
        return get() == null;
    }

    public void dispose() {
        if (get() != null) {
            Cancellable c = (Cancellable) getAndSet(null);
            if (c != null) {
                try {
                    c.cancel();
                } catch (Exception ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}
