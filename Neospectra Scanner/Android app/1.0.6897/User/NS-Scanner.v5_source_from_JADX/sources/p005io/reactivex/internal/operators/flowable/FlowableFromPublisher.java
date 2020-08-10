package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromPublisher */
public final class FlowableFromPublisher<T> extends Flowable<T> {
    final Publisher<? extends T> publisher;

    public FlowableFromPublisher(Publisher<? extends T> publisher2) {
        this.publisher = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.publisher.subscribe(s);
    }
}
