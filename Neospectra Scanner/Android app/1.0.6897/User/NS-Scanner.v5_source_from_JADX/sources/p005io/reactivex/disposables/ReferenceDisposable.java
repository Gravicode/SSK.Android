package p005io.reactivex.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.disposables.ReferenceDisposable */
abstract class ReferenceDisposable<T> extends AtomicReference<T> implements Disposable {
    private static final long serialVersionUID = 6537757548749041217L;

    /* access modifiers changed from: protected */
    public abstract void onDisposed(@NonNull T t);

    ReferenceDisposable(T value) {
        super(ObjectHelper.requireNonNull(value, "value is null"));
    }

    public final void dispose() {
        if (get() != null) {
            T value = getAndSet(null);
            if (value != null) {
                onDisposed(value);
            }
        }
    }

    public final boolean isDisposed() {
        return get() == null;
    }
}
