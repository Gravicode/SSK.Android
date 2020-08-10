package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSerialized */
public final class FlowableSerialized<T> extends AbstractFlowableWithUpstream<T, T> {
    public FlowableSerialized(Flowable<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new SerializedSubscriber<Object>(s));
    }
}
