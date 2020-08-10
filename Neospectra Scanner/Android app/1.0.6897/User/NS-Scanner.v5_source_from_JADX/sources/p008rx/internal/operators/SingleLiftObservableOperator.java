package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.internal.producers.SingleProducer;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleLiftObservableOperator */
public final class SingleLiftObservableOperator<T, R> implements OnSubscribe<R> {
    final Operator<? extends R, ? super T> lift;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleLiftObservableOperator$WrapSubscriberIntoSingle */
    static final class WrapSubscriberIntoSingle<T> extends SingleSubscriber<T> {
        final Subscriber<? super T> actual;

        WrapSubscriberIntoSingle(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSuccess(T value) {
            this.actual.setProducer(new SingleProducer(this.actual, value));
        }

        public void onError(Throwable error) {
            this.actual.onError(error);
        }
    }

    public SingleLiftObservableOperator(OnSubscribe<T> source2, Operator<? extends R, ? super T> lift2) {
        this.source = source2;
        this.lift = lift2;
    }

    public void call(SingleSubscriber<? super R> t) {
        Subscriber<R> outputAsSubscriber = new WrapSingleIntoSubscriber<>(t);
        t.add(outputAsSubscriber);
        try {
            Subscriber<? super T> inputAsSubscriber = (Subscriber) RxJavaHooks.onSingleLift(this.lift).call(outputAsSubscriber);
            SingleSubscriber<? super T> input = wrap(inputAsSubscriber);
            inputAsSubscriber.onStart();
            this.source.call(input);
        } catch (Throwable ex) {
            Exceptions.throwOrReport(ex, t);
        }
    }

    public static <T> SingleSubscriber<T> wrap(Subscriber<T> subscriber) {
        WrapSubscriberIntoSingle<T> parent = new WrapSubscriberIntoSingle<>(subscriber);
        subscriber.add(parent);
        return parent;
    }
}
