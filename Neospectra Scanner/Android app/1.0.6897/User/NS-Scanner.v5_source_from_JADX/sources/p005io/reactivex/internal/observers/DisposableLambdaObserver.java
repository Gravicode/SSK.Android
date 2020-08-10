package p005io.reactivex.internal.observers;

import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.DisposableLambdaObserver */
public final class DisposableLambdaObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> actual;
    final Action onDispose;
    final Consumer<? super Disposable> onSubscribe;

    /* renamed from: s */
    Disposable f82s;

    public DisposableLambdaObserver(Observer<? super T> actual2, Consumer<? super Disposable> onSubscribe2, Action onDispose2) {
        this.actual = actual2;
        this.onSubscribe = onSubscribe2;
        this.onDispose = onDispose2;
    }

    public void onSubscribe(Disposable s) {
        try {
            this.onSubscribe.accept(s);
            if (DisposableHelper.validate(this.f82s, s)) {
                this.f82s = s;
                this.actual.onSubscribe(this);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            s.dispose();
            this.f82s = DisposableHelper.DISPOSED;
            EmptyDisposable.error(e, this.actual);
        }
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        if (this.f82s != DisposableHelper.DISPOSED) {
            this.actual.onError(t);
        } else {
            RxJavaPlugins.onError(t);
        }
    }

    public void onComplete() {
        if (this.f82s != DisposableHelper.DISPOSED) {
            this.actual.onComplete();
        }
    }

    public void dispose() {
        try {
            this.onDispose.run();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
        }
        this.f82s.dispose();
    }

    public boolean isDisposed() {
        return this.f82s.isDisposed();
    }
}
