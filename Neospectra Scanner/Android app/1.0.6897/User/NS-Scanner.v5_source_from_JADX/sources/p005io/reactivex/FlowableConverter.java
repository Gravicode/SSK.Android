package p005io.reactivex;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.FlowableConverter */
public interface FlowableConverter<T, R> {
    @NonNull
    R apply(@NonNull Flowable<T> flowable);
}
