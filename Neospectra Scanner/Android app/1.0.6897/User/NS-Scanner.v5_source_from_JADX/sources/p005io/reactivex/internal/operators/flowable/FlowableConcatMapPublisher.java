package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.util.ErrorMode;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher */
public final class FlowableConcatMapPublisher<T, R> extends Flowable<R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int prefetch;
    final Publisher<T> source;

    public FlowableConcatMapPublisher(Publisher<T> source2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int prefetch2, ErrorMode errorMode2) {
        this.source = source2;
        this.mapper = mapper2;
        this.prefetch = prefetch2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
            this.source.subscribe(FlowableConcatMap.subscribe(s, this.mapper, this.prefetch, this.errorMode));
        }
    }
}
