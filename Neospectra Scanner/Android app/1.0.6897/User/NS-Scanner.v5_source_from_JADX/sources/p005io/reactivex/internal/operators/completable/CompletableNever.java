package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.internal.disposables.EmptyDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableNever */
public final class CompletableNever extends Completable {
    public static final Completable INSTANCE = new CompletableNever();

    private CompletableNever() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        s.onSubscribe(EmptyDisposable.NEVER);
    }
}
