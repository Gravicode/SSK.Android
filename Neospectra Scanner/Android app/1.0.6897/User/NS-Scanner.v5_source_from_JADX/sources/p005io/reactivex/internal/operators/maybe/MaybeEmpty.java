package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.fuseable.ScalarCallable;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeEmpty */
public final class MaybeEmpty extends Maybe<Object> implements ScalarCallable<Object> {
    public static final MaybeEmpty INSTANCE = new MaybeEmpty();

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super Object> observer) {
        EmptyDisposable.complete(observer);
    }

    public Object call() {
        return null;
    }
}
