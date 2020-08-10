package p005io.reactivex.internal.schedulers;

import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport */
public interface SchedulerMultiWorkerSupport {

    /* renamed from: io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport$WorkerCallback */
    public interface WorkerCallback {
        void onWorker(int i, @NonNull Worker worker);
    }

    void createWorkers(int i, @NonNull WorkerCallback workerCallback);
}
