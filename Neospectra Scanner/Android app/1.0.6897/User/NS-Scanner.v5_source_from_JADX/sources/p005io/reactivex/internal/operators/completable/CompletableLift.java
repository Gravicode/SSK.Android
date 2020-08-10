package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableOperator;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableLift */
public final class CompletableLift extends Completable {
    final CompletableOperator onLift;
    final CompletableSource source;

    public CompletableLift(CompletableSource source2, CompletableOperator onLift2) {
        this.source = source2;
        this.onLift = onLift2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        try {
            this.source.subscribe(this.onLift.apply(s));
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            RxJavaPlugins.onError(ex2);
        }
    }
}
