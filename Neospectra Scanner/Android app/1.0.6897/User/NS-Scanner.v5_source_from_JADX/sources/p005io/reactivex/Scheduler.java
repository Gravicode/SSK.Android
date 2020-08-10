package p005io.reactivex;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.schedulers.NewThreadWorker;
import p005io.reactivex.internal.schedulers.SchedulerWhen;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.SchedulerRunnableIntrospection;

/* renamed from: io.reactivex.Scheduler */
public abstract class Scheduler {
    static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(Long.getLong("rx2.scheduler.drift-tolerance", 15).longValue());

    /* renamed from: io.reactivex.Scheduler$DisposeTask */
    static final class DisposeTask implements Disposable, Runnable, SchedulerRunnableIntrospection {
        @NonNull
        final Runnable decoratedRun;
        @Nullable
        Thread runner;
        @NonNull

        /* renamed from: w */
        final Worker f66w;

        DisposeTask(@NonNull Runnable decoratedRun2, @NonNull Worker w) {
            this.decoratedRun = decoratedRun2;
            this.f66w = w;
        }

        public void run() {
            this.runner = Thread.currentThread();
            try {
                this.decoratedRun.run();
            } finally {
                dispose();
                this.runner = null;
            }
        }

        public void dispose() {
            if (this.runner != Thread.currentThread() || !(this.f66w instanceof NewThreadWorker)) {
                this.f66w.dispose();
            } else {
                ((NewThreadWorker) this.f66w).shutdown();
            }
        }

        public boolean isDisposed() {
            return this.f66w.isDisposed();
        }

        public Runnable getWrappedRunnable() {
            return this.decoratedRun;
        }
    }

    /* renamed from: io.reactivex.Scheduler$PeriodicDirectTask */
    static final class PeriodicDirectTask implements Disposable, Runnable, SchedulerRunnableIntrospection {
        volatile boolean disposed;
        @NonNull
        final Runnable run;
        @NonNull
        final Worker worker;

        PeriodicDirectTask(@NonNull Runnable run2, @NonNull Worker worker2) {
            this.run = run2;
            this.worker = worker2;
        }

        public void run() {
            if (!this.disposed) {
                try {
                    this.run.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.worker.dispose();
                    throw ExceptionHelper.wrapOrThrow(ex);
                }
            }
        }

        public void dispose() {
            this.disposed = true;
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public Runnable getWrappedRunnable() {
            return this.run;
        }
    }

    /* renamed from: io.reactivex.Scheduler$Worker */
    public static abstract class Worker implements Disposable {

        /* renamed from: io.reactivex.Scheduler$Worker$PeriodicTask */
        final class PeriodicTask implements Runnable, SchedulerRunnableIntrospection {
            long count;
            @NonNull
            final Runnable decoratedRun;
            long lastNowNanoseconds;
            final long periodInNanoseconds;
            @NonNull

            /* renamed from: sd */
            final SequentialDisposable f67sd;
            long startInNanoseconds;

            PeriodicTask(long firstStartInNanoseconds, @NonNull Runnable decoratedRun2, long firstNowNanoseconds, @NonNull SequentialDisposable sd, long periodInNanoseconds2) {
                this.decoratedRun = decoratedRun2;
                this.f67sd = sd;
                this.periodInNanoseconds = periodInNanoseconds2;
                this.lastNowNanoseconds = firstNowNanoseconds;
                this.startInNanoseconds = firstStartInNanoseconds;
            }

            public void run() {
                long nextTick;
                this.decoratedRun.run();
                if (!this.f67sd.isDisposed()) {
                    long nowNanoseconds = Worker.this.now(TimeUnit.NANOSECONDS);
                    if (Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS + nowNanoseconds < this.lastNowNanoseconds || nowNanoseconds >= this.lastNowNanoseconds + this.periodInNanoseconds + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
                        nextTick = this.periodInNanoseconds + nowNanoseconds;
                        long j = this.periodInNanoseconds;
                        long j2 = this.count + 1;
                        this.count = j2;
                        this.startInNanoseconds = nextTick - (j * j2);
                    } else {
                        long j3 = this.startInNanoseconds;
                        long j4 = this.count + 1;
                        this.count = j4;
                        nextTick = j3 + (j4 * this.periodInNanoseconds);
                    }
                    long nextTick2 = nextTick;
                    this.lastNowNanoseconds = nowNanoseconds;
                    this.f67sd.replace(Worker.this.schedule(this, nextTick2 - nowNanoseconds, TimeUnit.NANOSECONDS));
                }
            }

            public Runnable getWrappedRunnable() {
                return this.decoratedRun;
            }
        }

        @NonNull
        public abstract Disposable schedule(@NonNull Runnable runnable, long j, @NonNull TimeUnit timeUnit);

        @NonNull
        public Disposable schedule(@NonNull Runnable run) {
            return schedule(run, 0, TimeUnit.NANOSECONDS);
        }

        @NonNull
        public Disposable schedulePeriodically(@NonNull Runnable run, long initialDelay, long period, @NonNull TimeUnit unit) {
            long j = initialDelay;
            TimeUnit timeUnit = unit;
            SequentialDisposable first = new SequentialDisposable();
            SequentialDisposable sd = new SequentialDisposable(first);
            Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
            long periodInNanoseconds = timeUnit.toNanos(period);
            long firstNowNanoseconds = now(TimeUnit.NANOSECONDS);
            SequentialDisposable first2 = first;
            PeriodicTask periodicTask = r0;
            PeriodicTask periodicTask2 = new PeriodicTask(firstNowNanoseconds + timeUnit.toNanos(j), decoratedRun, firstNowNanoseconds, sd, periodInNanoseconds);
            Disposable d = schedule(periodicTask, j, timeUnit);
            if (d == EmptyDisposable.INSTANCE) {
                return d;
            }
            first2.replace(d);
            return sd;
        }

        public long now(@NonNull TimeUnit unit) {
            return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @NonNull
    public abstract Worker createWorker();

    public static long clockDriftTolerance() {
        return CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
    }

    public long now(@NonNull TimeUnit unit) {
        return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public void start() {
    }

    public void shutdown() {
    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable run) {
        return scheduleDirect(run, 0, TimeUnit.NANOSECONDS);
    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
        Worker w = createWorker();
        DisposeTask task = new DisposeTask(RxJavaPlugins.onSchedule(run), w);
        w.schedule(task, delay, unit);
        return task;
    }

    @NonNull
    public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, @NonNull TimeUnit unit) {
        Worker w = createWorker();
        PeriodicDirectTask periodicTask = new PeriodicDirectTask(RxJavaPlugins.onSchedule(run), w);
        Disposable d = w.schedulePeriodically(periodicTask, initialDelay, period, unit);
        if (d == EmptyDisposable.INSTANCE) {
            return d;
        }
        return periodicTask;
    }

    @NonNull
    public <S extends Scheduler & Disposable> S when(@NonNull Function<Flowable<Flowable<Completable>>, Completable> combine) {
        return new SchedulerWhen(combine, this);
    }
}
