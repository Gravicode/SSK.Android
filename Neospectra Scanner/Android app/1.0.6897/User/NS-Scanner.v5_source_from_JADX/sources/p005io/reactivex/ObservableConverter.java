package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.ObservableConverter */
public interface ObservableConverter<T, R> {
    @NonNull
    R apply(@NonNull Observable<T> observable);
}
