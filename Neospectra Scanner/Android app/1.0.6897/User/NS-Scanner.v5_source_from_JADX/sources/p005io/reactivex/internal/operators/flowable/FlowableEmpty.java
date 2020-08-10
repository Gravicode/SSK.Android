package p005io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import p005io.reactivex.Flowable;
import p005io.reactivex.internal.fuseable.ScalarCallable;
import p005io.reactivex.internal.subscriptions.EmptySubscription;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableEmpty */
public final class FlowableEmpty extends Flowable<Object> implements ScalarCallable<Object> {
    public static final Flowable<Object> INSTANCE = new FlowableEmpty();

    private FlowableEmpty() {
    }

    public void subscribeActual(Subscriber<? super Object> s) {
        EmptySubscription.complete(s);
    }

    public Object call() {
        return null;
    }
}
