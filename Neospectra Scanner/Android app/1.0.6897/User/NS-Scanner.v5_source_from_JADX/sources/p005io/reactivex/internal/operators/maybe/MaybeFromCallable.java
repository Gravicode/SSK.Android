package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.Callable;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFromCallable */
public final class MaybeFromCallable<T> extends Maybe<T> implements Callable<T> {
    final Callable<? extends T> callable;

    public MaybeFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        Disposable d = Disposables.empty();
        observer.onSubscribe(d);
        if (!d.isDisposed()) {
            try {
                T v = this.callable.call();
                if (!d.isDisposed()) {
                    if (v == null) {
                        observer.onComplete();
                    } else {
                        observer.onSuccess(v);
                    }
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                if (!d.isDisposed()) {
                    observer.onError(ex);
                } else {
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public T call() throws Exception {
        return this.callable.call();
    }
}
