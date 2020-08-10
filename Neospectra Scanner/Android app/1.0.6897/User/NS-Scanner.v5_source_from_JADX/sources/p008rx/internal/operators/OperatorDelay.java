package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.OperatorDelay */
public final class OperatorDelay<T> implements Operator<T, T> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    public OperatorDelay(long delay2, TimeUnit unit2, Scheduler scheduler2) {
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final Worker worker = this.scheduler.createWorker();
        child.add(worker);
        return new Subscriber<T>(child) {
            boolean done;

            public void onCompleted() {
                worker.schedule(new Action0() {
                    public void call() {
                        if (!C15931.this.done) {
                            C15931.this.done = true;
                            child.onCompleted();
                        }
                    }
                }, OperatorDelay.this.delay, OperatorDelay.this.unit);
            }

            public void onError(final Throwable e) {
                worker.schedule(new Action0() {
                    public void call() {
                        if (!C15931.this.done) {
                            C15931.this.done = true;
                            child.onError(e);
                            worker.unsubscribe();
                        }
                    }
                });
            }

            public void onNext(final T t) {
                worker.schedule(new Action0() {
                    public void call() {
                        if (!C15931.this.done) {
                            child.onNext(t);
                        }
                    }
                }, OperatorDelay.this.delay, OperatorDelay.this.unit);
            }
        };
    }
}
