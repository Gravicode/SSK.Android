package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.OnSubscribeTimerOnce */
public final class OnSubscribeTimerOnce implements OnSubscribe<Long> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OnSubscribeTimerOnce(long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void call(final Subscriber<? super Long> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        worker.schedule(new Action0() {
            public void call() {
                try {
                    child.onNext(Long.valueOf(0));
                    child.onCompleted();
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer<?>) child);
                }
            }
        }, this.time, this.unit);
    }
}
