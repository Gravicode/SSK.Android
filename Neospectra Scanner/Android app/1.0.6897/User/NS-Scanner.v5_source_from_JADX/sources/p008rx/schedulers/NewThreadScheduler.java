package p008rx.schedulers;

import p008rx.Scheduler;
import p008rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.NewThreadScheduler */
public final class NewThreadScheduler extends Scheduler {
    private NewThreadScheduler() {
        throw new IllegalStateException("No instances!");
    }

    public Worker createWorker() {
        return null;
    }
}
