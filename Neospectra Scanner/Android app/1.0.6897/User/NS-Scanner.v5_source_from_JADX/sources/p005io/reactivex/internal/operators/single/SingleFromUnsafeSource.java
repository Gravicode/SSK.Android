package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;

/* renamed from: io.reactivex.internal.operators.single.SingleFromUnsafeSource */
public final class SingleFromUnsafeSource<T> extends Single<T> {
    final SingleSource<T> source;

    public SingleFromUnsafeSource(SingleSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(observer);
    }
}
