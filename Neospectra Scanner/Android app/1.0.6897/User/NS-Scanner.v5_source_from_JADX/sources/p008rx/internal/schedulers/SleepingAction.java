package p008rx.internal.schedulers;

import p008rx.Scheduler.Worker;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;

/* renamed from: rx.internal.schedulers.SleepingAction */
class SleepingAction implements Action0 {
    private final long execTime;
    private final Worker innerScheduler;
    private final Action0 underlying;

    public SleepingAction(Action0 underlying2, Worker scheduler, long execTime2) {
        this.underlying = underlying2;
        this.innerScheduler = scheduler;
        this.execTime = execTime2;
    }

    public void call() {
        if (!this.innerScheduler.isUnsubscribed()) {
            long delay = this.execTime - this.innerScheduler.now();
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Exceptions.propagate(e);
                }
            }
            if (!this.innerScheduler.isUnsubscribed()) {
                this.underlying.call();
            }
        }
    }
}
