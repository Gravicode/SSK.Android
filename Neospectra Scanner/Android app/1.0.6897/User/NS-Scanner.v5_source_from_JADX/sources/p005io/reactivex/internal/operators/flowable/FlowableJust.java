package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.internal.fuseable.ScalarCallable;
import p005io.reactivex.internal.subscriptions.ScalarSubscription;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableJust */
public final class FlowableJust<T> extends Flowable<T> implements ScalarCallable<T> {
    private final T value;

    public FlowableJust(T value2) {
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        s.onSubscribe(new ScalarSubscription(s, this.value));
    }

    public T call() {
        return this.value;
    }
}
