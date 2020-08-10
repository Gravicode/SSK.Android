package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.internal.fuseable.ScalarCallable;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeJust */
public final class MaybeJust<T> extends Maybe<T> implements ScalarCallable<T> {
    final T value;

    public MaybeJust(T value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        observer.onSubscribe(Disposables.disposed());
        observer.onSuccess(this.value);
    }

    public T call() {
        return this.value;
    }
}
