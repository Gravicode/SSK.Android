package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.operators.OperatorUnsubscribeOn */
public class OperatorUnsubscribeOn<T> implements Operator<T, T> {
    final Scheduler scheduler;

    public OperatorUnsubscribeOn(Scheduler scheduler2) {
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final Subscriber<T> parent = new Subscriber<T>() {
            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(T t) {
                subscriber.onNext(t);
            }
        };
        subscriber.add(Subscriptions.create(new Action0() {
            public void call() {
                final Worker inner = OperatorUnsubscribeOn.this.scheduler.createWorker();
                inner.schedule(new Action0() {
                    public void call() {
                        parent.unsubscribe();
                        inner.unsubscribe();
                    }
                });
            }
        }));
        return parent;
    }
}
