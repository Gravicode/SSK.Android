package p005io.reactivex.parallel;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.parallel.ParallelFlowableConverter */
public interface ParallelFlowableConverter<T, R> {
    @NonNull
    R apply(@NonNull ParallelFlowable<T> parallelFlowable);
}
