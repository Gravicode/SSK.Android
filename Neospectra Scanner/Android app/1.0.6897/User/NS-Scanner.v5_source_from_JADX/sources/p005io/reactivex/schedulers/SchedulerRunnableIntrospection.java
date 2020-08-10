package p005io.reactivex.schedulers;

import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;

@Experimental
/* renamed from: io.reactivex.schedulers.SchedulerRunnableIntrospection */
public interface SchedulerRunnableIntrospection {
    @NonNull
    Runnable getWrappedRunnable();
}
