package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Subscriber;
import p008rx.schedulers.TimeInterval;

/* renamed from: rx.internal.operators.OperatorTimeInterval */
public final class OperatorTimeInterval<T> implements Operator<TimeInterval<T>, T> {
    final Scheduler scheduler;

    public OperatorTimeInterval(Scheduler scheduler2) {
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super TimeInterval<T>> subscriber) {
        return new Subscriber<T>(subscriber) {
            private long lastTimestamp = OperatorTimeInterval.this.scheduler.now();

            public void onNext(T args) {
                long nowTimestamp = OperatorTimeInterval.this.scheduler.now();
                subscriber.onNext(new TimeInterval(nowTimestamp - this.lastTimestamp, args));
                this.lastTimestamp = nowTimestamp;
            }

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        };
    }
}
