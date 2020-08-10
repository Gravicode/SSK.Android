package p008rx.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

/* renamed from: rx.internal.schedulers.GenericScheduledExecutorService */
public final class GenericScheduledExecutorService implements SchedulerLifecycle {
    public static final GenericScheduledExecutorService INSTANCE = new GenericScheduledExecutorService();
    private static final ScheduledExecutorService[] NONE = new ScheduledExecutorService[0];
    private static final ScheduledExecutorService SHUTDOWN = Executors.newScheduledThreadPool(0);
    private static int roundRobin;
    private final AtomicReference<ScheduledExecutorService[]> executor = new AtomicReference<>(NONE);

    static {
        SHUTDOWN.shutdown();
    }

    private GenericScheduledExecutorService() {
        start();
    }

    public void start() {
        int count = Runtime.getRuntime().availableProcessors();
        if (count > 4) {
            count /= 2;
        }
        if (count > 8) {
            count = 8;
        }
        ScheduledExecutorService[] execs = new ScheduledExecutorService[count];
        int i$ = 0;
        for (int i = 0; i < count; i++) {
            execs[i] = GenericScheduledExecutorServiceFactory.create();
        }
        if (this.executor.compareAndSet(NONE, execs)) {
            ScheduledExecutorService[] arr$ = execs;
            int len$ = arr$.length;
            while (i$ < len$) {
                ScheduledExecutorService exec = arr$[i$];
                if (!NewThreadWorker.tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
                    NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) exec);
                }
                i$++;
            }
            return;
        }
        ScheduledExecutorService[] arr$2 = execs;
        int len$2 = arr$2.length;
        while (i$ < len$2) {
            arr$2[i$].shutdownNow();
            i$++;
        }
    }

    public void shutdown() {
        ScheduledExecutorService[] execs;
        ScheduledExecutorService[] arr$;
        do {
            execs = (ScheduledExecutorService[]) this.executor.get();
            if (execs == NONE) {
                return;
            }
        } while (!this.executor.compareAndSet(execs, NONE));
        for (ScheduledExecutorService exec : execs) {
            NewThreadWorker.deregisterExecutor(exec);
            exec.shutdownNow();
        }
    }

    public static ScheduledExecutorService getInstance() {
        ScheduledExecutorService[] execs = (ScheduledExecutorService[]) INSTANCE.executor.get();
        if (execs == NONE) {
            return SHUTDOWN;
        }
        int r = roundRobin + 1;
        if (r >= execs.length) {
            r = 0;
        }
        roundRobin = r;
        return execs[r];
    }
}
