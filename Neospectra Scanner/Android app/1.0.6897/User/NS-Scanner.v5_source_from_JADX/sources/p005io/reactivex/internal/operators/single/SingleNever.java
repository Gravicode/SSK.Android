package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.internal.disposables.EmptyDisposable;

/* renamed from: io.reactivex.internal.operators.single.SingleNever */
public final class SingleNever extends Single<Object> {
    public static final Single<Object> INSTANCE = new SingleNever();

    private SingleNever() {
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Object> s) {
        s.onSubscribe(EmptyDisposable.NEVER);
    }
}
