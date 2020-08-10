package p005io.reactivex.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.disposables.ListCompositeDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport.WorkerCallback;

/* renamed from: io.reactivex.internal.schedulers.ComputationScheduler */
public final class ComputationScheduler extends Scheduler implements SchedulerMultiWorkerSupport {
    private static final String KEY_COMPUTATION_PRIORITY = "rx2.computation-priority";
    static final String KEY_MAX_THREADS = "rx2.computation-threads";
    static final int MAX_THREADS = cap(Runtime.getRuntime().availableProcessors(), Integer.getInteger(KEY_MAX_THREADS, 0).intValue());
    static final FixedSchedulerPool NONE = new FixedSchedulerPool(0, THREAD_FACTORY);
    static final PoolWorker SHUTDOWN_WORKER = new PoolWorker(new RxThreadFactory("RxComputationShutdown"));
    static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX, Math.max(1, Math.min(10, Integer.getInteger(KEY_COMPUTATION_PRIORITY, 5).intValue())), true);
    private static final String THREAD_NAME_PREFIX = "RxComputationThreadPool";
    final AtomicReference<FixedSchedulerPool> pool;
    final ThreadFactory threadFactory;

    /* renamed from: io.reactivex.internal.schedulers.ComputationScheduler$EventLoopWorker */
    static final class EventLoopWorker extends Worker {
        private final ListCompositeDisposable both = new ListCompositeDisposable();
        volatile boolean disposed;
        private final PoolWorker poolWorker;
        private final ListCompositeDisposable serial = new ListCompositeDisposable();
        private final CompositeDisposable timed = new CompositeDisposable();

        EventLoopWorker(PoolWorker poolWorker2) {
            this.poolWorker = poolWorker2;
            this.both.add(this.serial);
            this.both.add(this.timed);
        }

        public void dispose() {
            if (!this.disposed) {
                this.disposed = true;
                this.both.dispose();
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable action) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            return this.poolWorker.scheduleActual(action, 0, TimeUnit.MILLISECONDS, this.serial);
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            return this.poolWorker.scheduleActual(action, delayTime, unit, this.timed);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.ComputationScheduler$FixedSchedulerPool */
    static final class FixedSchedulerPool implements SchedulerMultiWorkerSupport {
        final int cores;
        final PoolWorker[] eventLoops;

        /* renamed from: n */
        long f425n;

        FixedSchedulerPool(int maxThreads, ThreadFactory threadFactory) {
            this.cores = maxThreads;
            this.eventLoops = new PoolWorker[maxThreads];
            for (int i = 0; i < maxThreads; i++) {
                this.eventLoops[i] = new PoolWorker(threadFactory);
            }
        }

        public PoolWorker getEventLoop() {
            int c = this.cores;
            if (c == 0) {
                return ComputationScheduler.SHUTDOWN_WORKER;
            }
            PoolWorker[] poolWorkerArr = this.eventLoops;
            long j = this.f425n;
            this.f425n = 1 + j;
            return poolWorkerArr[(int) (j % ((long) c))];
        }

        public void shutdown() {
            for (PoolWorker w : this.eventLoops) {
                w.dispose();
            }
        }

        public void createWorkers(int number, WorkerCallback callback) {
            int c = this.cores;
            int i = 0;
            if (c == 0) {
                while (i < number) {
                    callback.onWorker(i, ComputationScheduler.SHUTDOWN_WORKER);
                    i++;
                }
                return;
            }
            int index = ((int) this.f425n) % c;
            while (i < number) {
                callback.onWorker(i, new EventLoopWorker(this.eventLoops[index]));
                index++;
                if (index == c) {
                    index = 0;
                }
                i++;
            }
            this.f425n = (long) index;
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.ComputationScheduler$PoolWorker */
    static final class PoolWorker extends NewThreadWorker {
        PoolWorker(ThreadFactory threadFactory) {
            super(threadFactory);
        }
    }

    static {
        SHUTDOWN_WORKER.dispose();
        NONE.shutdown();
    }

    static int cap(int cpuCount, int paramThreads) {
        return (paramThreads <= 0 || paramThreads > cpuCount) ? cpuCount : paramThreads;
    }

    public ComputationScheduler() {
        this(THREAD_FACTORY);
    }

    public ComputationScheduler(ThreadFactory threadFactory2) {
        this.threadFactory = threadFactory2;
        this.pool = new AtomicReference<>(NONE);
        start();
    }

    @NonNull
    public Worker createWorker() {
        return new EventLoopWorker(((FixedSchedulerPool) this.pool.get()).getEventLoop());
    }

    public void createWorkers(int number, WorkerCallback callback) {
        ObjectHelper.verifyPositive(number, "number > 0 required");
        ((FixedSchedulerPool) this.pool.get()).createWorkers(number, callback);
    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
        return ((FixedSchedulerPool) this.pool.get()).getEventLoop().scheduleDirect(run, delay, unit);
    }

    @NonNull
    public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
        return ((FixedSchedulerPool) this.pool.get()).getEventLoop().schedulePeriodicallyDirect(run, initialDelay, period, unit);
    }

    public void start() {
        FixedSchedulerPool update = new FixedSchedulerPool(MAX_THREADS, this.threadFactory);
        if (!this.pool.compareAndSet(NONE, update)) {
            update.shutdown();
        }
    }

    public void shutdown() {
        FixedSchedulerPool curr;
        do {
            curr = (FixedSchedulerPool) this.pool.get();
            if (curr == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(curr, NONE));
        curr.shutdown();
    }
}
