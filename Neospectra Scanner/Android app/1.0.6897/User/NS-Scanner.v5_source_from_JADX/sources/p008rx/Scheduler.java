package p008rx;

import java.util.concurrent.TimeUnit;
import p008rx.annotations.Experimental;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.schedulers.SchedulerWhen;
import p008rx.internal.subscriptions.SequentialSubscription;

/* renamed from: rx.Scheduler */
public abstract class Scheduler {
    static final long CLOCK_DRIFT_TOLERANCE_NANOS = TimeUnit.MINUTES.toNanos(Long.getLong("rx.scheduler.drift-tolerance", 15).longValue());

    /* renamed from: rx.Scheduler$Worker */
    public static abstract class Worker implements Subscription {
        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(Action0 action, long initialDelay, long period, TimeUnit unit) {
            long j = initialDelay;
            TimeUnit timeUnit = unit;
            long periodInNanos = timeUnit.toNanos(period);
            long firstNowNanos = TimeUnit.MILLISECONDS.toNanos(now());
            long firstStartInNanos = firstNowNanos + timeUnit.toNanos(j);
            SequentialSubscription first = new SequentialSubscription();
            SequentialSubscription mas = new SequentialSubscription(first);
            final long j2 = firstNowNanos;
            final long j3 = firstStartInNanos;
            final Action0 action0 = action;
            final SequentialSubscription sequentialSubscription = mas;
            SequentialSubscription first2 = first;
            final long j4 = periodInNanos;
            C14821 r5 = new Action0() {
                long count;
                long lastNowNanos = j2;
                long startInNanos = j3;

                public void call() {
                    long nextTick;
                    action0.call();
                    if (!sequentialSubscription.isUnsubscribed()) {
                        long nowNanos = TimeUnit.MILLISECONDS.toNanos(Worker.this.now());
                        if (Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS + nowNanos < this.lastNowNanos || nowNanos >= this.lastNowNanos + j4 + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOS) {
                            nextTick = j4 + nowNanos;
                            long j = j4;
                            long j2 = this.count + 1;
                            this.count = j2;
                            this.startInNanos = nextTick - (j * j2);
                        } else {
                            long j3 = this.startInNanos;
                            long j4 = this.count + 1;
                            this.count = j4;
                            nextTick = j3 + (j4 * j4);
                        }
                        long nextTick2 = nextTick;
                        this.lastNowNanos = nowNanos;
                        sequentialSubscription.replace(Worker.this.schedule(this, nextTick2 - nowNanos, TimeUnit.NANOSECONDS));
                    }
                }
            };
            first2.replace(schedule(r5, j, timeUnit));
            return mas;
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public abstract Worker createWorker();

    public long now() {
        return System.currentTimeMillis();
    }

    @Experimental
    public <S extends Scheduler & Subscription> S when(Func1<Observable<Observable<Completable>>, Completable> combine) {
        return new SchedulerWhen(combine, this);
    }
}
