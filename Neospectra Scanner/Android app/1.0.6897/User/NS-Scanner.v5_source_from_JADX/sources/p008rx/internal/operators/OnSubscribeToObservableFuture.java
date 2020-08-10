package p008rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.internal.producers.SingleProducer;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OnSubscribeToObservableFuture */
public final class OnSubscribeToObservableFuture {

    /* renamed from: rx.internal.operators.OnSubscribeToObservableFuture$ToObservableFuture */
    static class ToObservableFuture<T> implements OnSubscribe<T> {
        final Future<? extends T> that;
        private final long time;
        private final TimeUnit unit;

        public ToObservableFuture(Future<? extends T> that2) {
            this.that = that2;
            this.time = 0;
            this.unit = null;
        }

        public ToObservableFuture(Future<? extends T> that2, long time2, TimeUnit unit2) {
            this.that = that2;
            this.time = time2;
            this.unit = unit2;
        }

        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(Subscriptions.create(new Action0() {
                public void call() {
                    ToObservableFuture.this.that.cancel(true);
                }
            }));
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.setProducer(new SingleProducer(subscriber, this.unit == null ? this.that.get() : this.that.get(this.time, this.unit)));
                }
            } catch (Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    Exceptions.throwOrReport(e, (Observer<?>) subscriber);
                }
            }
        }
    }

    private OnSubscribeToObservableFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> OnSubscribe<T> toObservableFuture(Future<? extends T> that) {
        return new ToObservableFuture(that);
    }

    public static <T> OnSubscribe<T> toObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
        return new ToObservableFuture(that, time, unit);
    }
}
