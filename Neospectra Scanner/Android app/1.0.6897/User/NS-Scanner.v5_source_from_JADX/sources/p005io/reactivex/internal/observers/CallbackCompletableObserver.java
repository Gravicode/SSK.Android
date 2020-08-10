package p005io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.OnErrorNotImplementedException;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.LambdaConsumerIntrospection;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.CallbackCompletableObserver */
public final class CallbackCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Consumer<Throwable>, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -4361286194466301354L;
    final Action onComplete;
    final Consumer<? super Throwable> onError;

    public CallbackCompletableObserver(Action onComplete2) {
        this.onError = this;
        this.onComplete = onComplete2;
    }

    public CallbackCompletableObserver(Consumer<? super Throwable> onError2, Action onComplete2) {
        this.onError = onError2;
        this.onComplete = onComplete2;
    }

    public void accept(Throwable e) {
        RxJavaPlugins.onError(new OnErrorNotImplementedException(e));
    }

    public void onComplete() {
        try {
            this.onComplete.run();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onError(Throwable e) {
        try {
            this.onError.accept(e);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public boolean hasCustomOnError() {
        return this.onError != this;
    }
}
