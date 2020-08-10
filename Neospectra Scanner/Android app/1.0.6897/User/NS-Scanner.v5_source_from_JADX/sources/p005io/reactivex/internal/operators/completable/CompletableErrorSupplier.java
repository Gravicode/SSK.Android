package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableErrorSupplier */
public final class CompletableErrorSupplier extends Completable {
    final Callable<? extends Throwable> errorSupplier;

    public CompletableErrorSupplier(Callable<? extends Throwable> errorSupplier2) {
        this.errorSupplier = errorSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        try {
            e = (Throwable) ObjectHelper.requireNonNull(this.errorSupplier.call(), "The error returned is null");
        } catch (Throwable th) {
            e = th;
            Exceptions.throwIfFatal(e);
        }
        EmptyDisposable.error(e, s);
    }
}
