package p008rx.schedulers;

import p008rx.Scheduler;
import p008rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.TrampolineScheduler */
public final class TrampolineScheduler extends Scheduler {
    private TrampolineScheduler() {
        throw new IllegalStateException("No instances!");
    }

    public Worker createWorker() {
        return null;
    }
}
