package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeUnsafeCreate */
public final class MaybeUnsafeCreate<T> extends AbstractMaybeWithUpstream<T, T> {
    public MaybeUnsafeCreate(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(observer);
    }
}
