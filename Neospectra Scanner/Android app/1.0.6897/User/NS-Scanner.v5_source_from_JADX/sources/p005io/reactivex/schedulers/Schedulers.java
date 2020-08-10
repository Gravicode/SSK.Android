package p005io.reactivex.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import p005io.reactivex.Scheduler;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.internal.schedulers.ComputationScheduler;
import p005io.reactivex.internal.schedulers.ExecutorScheduler;
import p005io.reactivex.internal.schedulers.IoScheduler;
import p005io.reactivex.internal.schedulers.NewThreadScheduler;
import p005io.reactivex.internal.schedulers.SchedulerPoolFactory;
import p005io.reactivex.internal.schedulers.SingleScheduler;
import p005io.reactivex.internal.schedulers.TrampolineScheduler;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.schedulers.Schedulers */
public final class Schedulers {
    @NonNull
    static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new ComputationTask());
    @NonNull

    /* renamed from: IO */
    static final Scheduler f470IO = RxJavaPlugins.initIoScheduler(new IOTask());
    @NonNull
    static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new NewThreadTask());
    @NonNull
    static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new SingleTask());
    @NonNull
    static final Scheduler TRAMPOLINE = TrampolineScheduler.instance();

    /* renamed from: io.reactivex.schedulers.Schedulers$ComputationHolder */
    static final class ComputationHolder {
        static final Scheduler DEFAULT = new ComputationScheduler();

        ComputationHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$ComputationTask */
    static final class ComputationTask implements Callable<Scheduler> {
        ComputationTask() {
        }

        public Scheduler call() throws Exception {
            return ComputationHolder.DEFAULT;
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$IOTask */
    static final class IOTask implements Callable<Scheduler> {
        IOTask() {
        }

        public Scheduler call() throws Exception {
            return IoHolder.DEFAULT;
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$IoHolder */
    static final class IoHolder {
        static final Scheduler DEFAULT = new IoScheduler();

        IoHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$NewThreadHolder */
    static final class NewThreadHolder {
        static final Scheduler DEFAULT = new NewThreadScheduler();

        NewThreadHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$NewThreadTask */
    static final class NewThreadTask implements Callable<Scheduler> {
        NewThreadTask() {
        }

        public Scheduler call() throws Exception {
            return NewThreadHolder.DEFAULT;
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$SingleHolder */
    static final class SingleHolder {
        static final Scheduler DEFAULT = new SingleScheduler();

        SingleHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$SingleTask */
    static final class SingleTask implements Callable<Scheduler> {
        SingleTask() {
        }

        public Scheduler call() throws Exception {
            return SingleHolder.DEFAULT;
        }
    }

    private Schedulers() {
        throw new IllegalStateException("No instances!");
    }

    @NonNull
    public static Scheduler computation() {
        return RxJavaPlugins.onComputationScheduler(COMPUTATION);
    }

    @NonNull
    /* renamed from: io */
    public static Scheduler m40io() {
        return RxJavaPlugins.onIoScheduler(f470IO);
    }

    @NonNull
    public static Scheduler trampoline() {
        return TRAMPOLINE;
    }

    @NonNull
    public static Scheduler newThread() {
        return RxJavaPlugins.onNewThreadScheduler(NEW_THREAD);
    }

    @NonNull
    public static Scheduler single() {
        return RxJavaPlugins.onSingleScheduler(SINGLE);
    }

    @NonNull
    public static Scheduler from(@NonNull Executor executor) {
        return new ExecutorScheduler(executor);
    }

    public static void shutdown() {
        computation().shutdown();
        m40io().shutdown();
        newThread().shutdown();
        single().shutdown();
        trampoline().shutdown();
        SchedulerPoolFactory.shutdown();
    }

    public static void start() {
        computation().start();
        m40io().start();
        newThread().start();
        single().start();
        trampoline().start();
        SchedulerPoolFactory.start();
    }
}
