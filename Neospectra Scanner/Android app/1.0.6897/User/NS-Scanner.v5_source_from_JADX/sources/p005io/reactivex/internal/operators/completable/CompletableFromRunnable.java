package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.exceptions.Exceptions;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromRunnable */
public final class CompletableFromRunnable extends Completable {
    final Runnable runnable;

    public CompletableFromRunnable(Runnable runnable2) {
        this.runnable = runnable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        Disposable d = Disposables.empty();
        s.onSubscribe(d);
        try {
            this.runnable.run();
            if (!d.isDisposed()) {
                s.onComplete();
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            if (!d.isDisposed()) {
                s.onError(e);
            }
        }
    }
}
