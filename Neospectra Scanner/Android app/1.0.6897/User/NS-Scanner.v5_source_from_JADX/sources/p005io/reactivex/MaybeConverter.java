package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.MaybeConverter */
public interface MaybeConverter<T, R> {
    @NonNull
    R apply(@NonNull Maybe<T> maybe);
}
