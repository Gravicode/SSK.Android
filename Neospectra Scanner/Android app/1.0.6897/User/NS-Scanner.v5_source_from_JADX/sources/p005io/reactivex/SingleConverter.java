package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.SingleConverter */
public interface SingleConverter<T, R> {
    @NonNull
    R apply(@NonNull Single<T> single);
}
