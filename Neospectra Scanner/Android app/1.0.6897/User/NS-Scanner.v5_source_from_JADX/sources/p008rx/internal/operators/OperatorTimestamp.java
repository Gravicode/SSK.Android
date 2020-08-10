package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Subscriber;
import p008rx.schedulers.Timestamped;

/* renamed from: rx.internal.operators.OperatorTimestamp */
public final class OperatorTimestamp<T> implements Operator<Timestamped<T>, T> {
    final Scheduler scheduler;

    public OperatorTimestamp(Scheduler scheduler2) {
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Timestamped<T>> o) {
        return new Subscriber<T>(o) {
            public void onCompleted() {
                o.onCompleted();
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(T t) {
                o.onNext(new Timestamped(OperatorTimestamp.this.scheduler.now(), t));
            }
        };
    }
}
