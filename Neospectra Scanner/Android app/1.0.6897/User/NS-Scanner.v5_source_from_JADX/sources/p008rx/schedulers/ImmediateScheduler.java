package p008rx.schedulers;

import p008rx.Scheduler;
import p008rx.Scheduler.Worker;

@Deprecated
/* renamed from: rx.schedulers.ImmediateScheduler */
public final class ImmediateScheduler extends Scheduler {
    private ImmediateScheduler() {
        throw new IllegalStateException("No instances!");
    }

    public Worker createWorker() {
        return null;
    }
}
