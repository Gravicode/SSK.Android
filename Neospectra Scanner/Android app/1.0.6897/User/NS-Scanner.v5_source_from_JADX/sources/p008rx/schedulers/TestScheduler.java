package p008rx.schedulers;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.functions.Action0;
import p008rx.subscriptions.BooleanSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.schedulers.TestScheduler */
public class TestScheduler extends Scheduler {
    static long counter;
    final Queue<TimedAction> queue = new PriorityQueue(11, new CompareActionsByTime());
    long time;

    /* renamed from: rx.schedulers.TestScheduler$CompareActionsByTime */
    static final class CompareActionsByTime implements Comparator<TimedAction> {
        CompareActionsByTime() {
        }

        public int compare(TimedAction action1, TimedAction action2) {
            int i = 0;
            if (action1.time == action2.time) {
                if (action1.count < action2.count) {
                    i = -1;
                } else if (action1.count > action2.count) {
                    i = 1;
                }
                return i;
            }
            if (action1.time < action2.time) {
                i = -1;
            } else if (action1.time > action2.time) {
                i = 1;
            }
            return i;
        }
    }

    /* renamed from: rx.schedulers.TestScheduler$InnerTestScheduler */
    final class InnerTestScheduler extends Worker {

        /* renamed from: s */
        private final BooleanSubscription f932s = new BooleanSubscription();

        InnerTestScheduler() {
        }

        public void unsubscribe() {
            this.f932s.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.f932s.isUnsubscribed();
        }

        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            final TimedAction timedAction = new TimedAction(this, TestScheduler.this.time + unit.toNanos(delayTime), action);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new Action0() {
                public void call() {
                    TestScheduler.this.queue.remove(timedAction);
                }
            });
        }

        public Subscription schedule(Action0 action) {
            final TimedAction timedAction = new TimedAction(this, 0, action);
            TestScheduler.this.queue.add(timedAction);
            return Subscriptions.create(new Action0() {
                public void call() {
                    TestScheduler.this.queue.remove(timedAction);
                }
            });
        }

        public long now() {
            return TestScheduler.this.now();
        }
    }

    /* renamed from: rx.schedulers.TestScheduler$TimedAction */
    static final class TimedAction {
        final Action0 action;
        /* access modifiers changed from: private */
        public final long count;
        final Worker scheduler;
        final long time;

        TimedAction(Worker scheduler2, long time2, Action0 action2) {
            long j = TestScheduler.counter;
            TestScheduler.counter = 1 + j;
            this.count = j;
            this.time = time2;
            this.action = action2;
            this.scheduler = scheduler2;
        }

        public String toString() {
            return String.format("TimedAction(time = %d, action = %s)", new Object[]{Long.valueOf(this.time), this.action.toString()});
        }
    }

    public long now() {
        return TimeUnit.NANOSECONDS.toMillis(this.time);
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

    private void triggerActions(long targetTimeInNanos) {
        while (!this.queue.isEmpty()) {
            TimedAction current = (TimedAction) this.queue.peek();
            if (current.time > targetTimeInNanos) {
                break;
            }
            this.time = current.time == 0 ? this.time : current.time;
            this.queue.remove();
            if (!current.scheduler.isUnsubscribed()) {
                current.action.call();
            }
        }
        this.time = targetTimeInNanos;
    }

    public Worker createWorker() {
        return new InnerTestScheduler();
    }
}
