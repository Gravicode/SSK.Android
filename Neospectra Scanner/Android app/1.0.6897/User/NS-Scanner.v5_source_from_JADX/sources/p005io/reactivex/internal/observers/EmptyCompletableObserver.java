package p005io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.OnErrorNotImplementedException;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.LambdaConsumerIntrospection;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.EmptyCompletableObserver */
public final class EmptyCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -7545121636549663526L;

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public void onComplete() {
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onError(Throwable e) {
        lazySet(DisposableHelper.DISPOSED);
        RxJavaPlugins.onError(new OnErrorNotImplementedException(e));
    }

    public void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    public boolean hasCustomOnError() {
        return false;
    }
}
