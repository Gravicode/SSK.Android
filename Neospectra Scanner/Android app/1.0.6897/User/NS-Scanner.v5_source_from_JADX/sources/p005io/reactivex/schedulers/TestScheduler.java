package p005io.reactivex.schedulers;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.schedulers.TestScheduler */
public final class TestScheduler extends Scheduler {
    long counter;
    final Queue<TimedRunnable> queue = new PriorityBlockingQueue(11);
    volatile long time;

    /* renamed from: io.reactivex.schedulers.TestScheduler$TestWorker */
    final class TestWorker extends Worker {
        volatile boolean disposed;

        /* renamed from: io.reactivex.schedulers.TestScheduler$TestWorker$QueueRemove */
        final class QueueRemove implements Runnable {
            final TimedRunnable timedAction;

            QueueRemove(TimedRunnable timedAction2) {
                this.timedAction = timedAction2;
            }

            public void run() {
                TestScheduler.this.queue.remove(this.timedAction);
            }
        }

        TestWorker() {
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable run, long delayTime, @NonNull TimeUnit unit) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            long nanos = unit.toNanos(delayTime) + TestScheduler.this.time;
            TestScheduler testScheduler = TestScheduler.this;
            long j = testScheduler.counter;
            testScheduler.counter = 1 + j;
            TimedRunnable timedRunnable = new TimedRunnable(this, nanos, run, j);
            TestScheduler.this.queue.add(timedRunnable);
            return Disposables.fromRunnable(new QueueRemove(timedRunnable));
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable run) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            TestScheduler testScheduler = TestScheduler.this;
            long j = testScheduler.counter;
            testScheduler.counter = 1 + j;
            TimedRunnable timedRunnable = new TimedRunnable(this, 0, run, j);
            TestScheduler.this.queue.add(timedRunnable);
            return Disposables.fromRunnable(new QueueRemove(timedRunnable));
        }

        public long now(@NonNull TimeUnit unit) {
            return TestScheduler.this.now(unit);
        }
    }

    /* renamed from: io.reactivex.schedulers.TestScheduler$TimedRunnable */
    static final class TimedRunnable implements Comparable<TimedRunnable> {
        final long count;
        final Runnable run;
        final TestWorker scheduler;
        final long time;

        TimedRunnable(TestWorker scheduler2, long time2, Runnable run2, long count2) {
            this.time = time2;
            this.run = run2;
            this.scheduler = scheduler2;
            this.count = count2;
        }

        public String toString() {
            return String.format("TimedRunnable(time = %d, run = %s)", new Object[]{Long.valueOf(this.time), this.run.toString()});
        }

        public int compareTo(TimedRunnable o) {
            if (this.time == o.time) {
                return ObjectHelper.compare(this.count, o.count);
            }
            return ObjectHelper.compare(this.time, o.time);
        }
    }

    public TestScheduler() {
    }

    public TestScheduler(long delayTime, TimeUnit unit) {
        this.time = unit.toNanos(delayTime);
    }

    public long now(@NonNull TimeUnit unit) {
        return unit.convert(this.time, TimeUnit.NANOSECONDS);
    }

    public void advanceTimeBy(long delayTime, TimeUnit unit) {
        advanceTimeTo(this.time + unit.toNanos(delayTime), TimeUnit.NANOSECONDS);
    }

    public void advanceTimeTo(long delayTime, TimeUnit unit) {
        triggerActions(unit.toNanos(delayTime));
    }

    public void triggerActions() {
        triggerActions(this.time);
    }

    private void triggerActions(long targetTimeInNanoseconds) {
        while (true) {
            TimedRunnable current = (TimedRunnable) this.queue.peek();
            if (current == null || current.time > targetTimeInNanoseconds) {
                this.time = targetTimeInNanoseconds;
            } else {
                this.time = current.time == 0 ? this.time : current.time;
                this.queue.remove(current);
                if (!current.scheduler.disposed) {
                    current.run.run();
                }
            }
        }
        this.time = targetTimeInNanoseconds;
    }

    @NonNull
    public Worker createWorker() {
        return new TestWorker();
    }
}
