package p005io.reactivex.parallel;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.functions.BiFunction;

@Experimental
/* renamed from: io.reactivex.parallel.ParallelFailureHandling */
public enum ParallelFailureHandling implements BiFunction<Long, Throwable, ParallelFailureHandling> {
    STOP,
    ERROR,
    SKIP,
    RETRY;

    public ParallelFailureHandling apply(Long t1, Throwable t2) {
        return this;
    }
}
