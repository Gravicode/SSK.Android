package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.CompletableConverter */
public interface CompletableConverter<R> {
    @NonNull
    R apply(@NonNull Completable completable);
}
