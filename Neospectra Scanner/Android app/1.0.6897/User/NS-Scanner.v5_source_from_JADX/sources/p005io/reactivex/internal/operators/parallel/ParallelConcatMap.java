package p005io.reactivex.internal.operators.parallel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.parallel.ParallelFlowable;

/* renamed from: io.reactivex.internal.operators.parallel.ParallelConcatMap */
public final class ParallelConcatMap<T, R> extends ParallelFlowable<R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int prefetch;
    final ParallelFlowable<T> source;

    public ParallelConcatMap(ParallelFlowable<T> source2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int prefetch2, ErrorMode errorMode2) {
        this.source = source2;
        this.mapper = (Function) ObjectHelper.requireNonNull(mapper2, "mapper");
        this.prefetch = prefetch2;
        this.errorMode = (ErrorMode) ObjectHelper.requireNonNull(errorMode2, "errorMode");
    }

    public int parallelism() {
        return this.source.parallelism();
    }

    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (validate(subscribers)) {
            int n = subscribers.length;
            Subscriber<T>[] parents = new Subscriber[n];
            for (int i = 0; i < n; i++) {
                parents[i] = FlowableConcatMap.subscribe(subscribers[i], this.mapper, this.prefetch, this.errorMode);
            }
            this.source.subscribe(parents);
        }
    }
}
