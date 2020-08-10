package p008rx.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;

/* renamed from: rx.internal.schedulers.NewThreadScheduler */
public final class NewThreadScheduler extends Scheduler {
    private final ThreadFactory threadFactory;

    public NewThreadScheduler(ThreadFactory threadFactory2) {
        this.threadFactory = threadFactory2;
    }

    public Worker createWorker() {
        return new NewThreadWorker(this.threadFactory);
    }
}
