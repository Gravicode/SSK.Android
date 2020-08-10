package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.Subscribers;

/* renamed from: rx.internal.operators.OnSubscribeDelaySubscription */
public final class OnSubscribeDelaySubscription<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<? extends T> source;
    final long time;
    final TimeUnit unit;

    public OnSubscribeDelaySubscription(Observable<? extends T> source2, long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super T> s) {
        Worker worker = this.scheduler.createWorker();
        s.add(worker);
        worker.schedule(new Action0() {
            public void call() {
                if (!s.isUnsubscribed()) {
                    OnSubscribeDelaySubscription.this.source.unsafeSubscribe(Subscribers.wrap(s));
                }
            }
        }, this.time, this.unit);
    }
}
