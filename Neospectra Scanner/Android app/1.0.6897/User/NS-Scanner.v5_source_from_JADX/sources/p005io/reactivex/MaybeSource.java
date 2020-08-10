package p005io.reactivex;

import p005io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.MaybeSource */
public interface MaybeSource<T> {
    void subscribe(@NonNull MaybeObserver<? super T> maybeObserver);
}
