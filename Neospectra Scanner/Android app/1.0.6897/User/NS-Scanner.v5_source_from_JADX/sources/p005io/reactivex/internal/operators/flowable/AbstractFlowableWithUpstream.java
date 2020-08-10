package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import p005io.reactivex.Flowable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamPublisher;

/* renamed from: io.reactivex.internal.operators.flowable.AbstractFlowableWithUpstream */
abstract class AbstractFlowableWithUpstream<T, R> extends Flowable<R> implements HasUpstreamPublisher<T> {
    protected final Flowable<T> source;

    AbstractFlowableWithUpstream(Flowable<T> source2) {
        this.source = (Flowable) ObjectHelper.requireNonNull(source2, "source is null");
    }

    public final Publisher<T> source() {
        return this.source;
    }
}
