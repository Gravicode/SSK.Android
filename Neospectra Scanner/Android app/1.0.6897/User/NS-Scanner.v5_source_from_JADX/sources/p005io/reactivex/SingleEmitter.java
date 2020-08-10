package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Cancellable;

/* renamed from: io.reactivex.SingleEmitter */
public interface SingleEmitter<T> {
    boolean isDisposed();

    void onError(@NonNull Throwable th);

    void onSuccess(@NonNull T t);

    void setCancellable(@Nullable Cancellable cancellable);

    void setDisposable(@Nullable Disposable disposable);

    @Experimental
    boolean tryOnError(@NonNull Throwable th);
}
