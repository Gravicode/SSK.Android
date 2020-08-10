package p008rx.internal.operators;

import java.util.concurrent.Callable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.internal.producers.SingleDelayedProducer;

/* renamed from: rx.internal.operators.OnSubscribeFromCallable */
public final class OnSubscribeFromCallable<T> implements OnSubscribe<T> {
    private final Callable<? extends T> resultFactory;

    public OnSubscribeFromCallable(Callable<? extends T> resultFactory2) {
        this.resultFactory = resultFactory2;
    }

    public void call(Subscriber<? super T> subscriber) {
        SingleDelayedProducer<T> singleDelayedProducer = new SingleDelayedProducer<>(subscriber);
        subscriber.setProducer(singleDelayedProducer);
        try {
            singleDelayedProducer.setValue(this.resultFactory.call());
        } catch (Throwable t) {
            Exceptions.throwOrReport(t, (Observer<?>) subscriber);
        }
    }
}
