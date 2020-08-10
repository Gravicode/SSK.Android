package p008rx.android.schedulers;

import android.os.Handler;
import java.util.concurrent.TimeUnit;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.android.plugins.RxAndroidPlugins;
import p008rx.functions.Action0;
import p008rx.internal.schedulers.ScheduledAction;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.android.schedulers.HandlerScheduler */
public final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    /* renamed from: rx.android.schedulers.HandlerScheduler$HandlerWorker */
    static class HandlerWorker extends Worker {
        private final CompositeSubscription compositeSubscription = new CompositeSubscription();
        /* access modifiers changed from: private */
        public final Handler handler;

        HandlerWorker(Handler handler2) {
            this.handler = handler2;
        }

        public void unsubscribe() {
            this.compositeSubscription.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.compositeSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (this.compositeSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            final ScheduledAction scheduledAction = new ScheduledAction(RxAndroidPlugins.getInstance().getSchedulersHook().onSchedule(action));
            scheduledAction.addParent(this.compositeSubscription);
            this.compositeSubscription.add(scheduledAction);
            this.handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
            scheduledAction.add(Subscriptions.create(new Action0() {
                public void call() {
                    HandlerWorker.this.handler.removeCallbacks(scheduledAction);
                }
            }));
            return scheduledAction;
        }

        public Subscription schedule(Action0 action) {
            return schedule(action, 0, TimeUnit.MILLISECONDS);
        }
    }

    public static HandlerScheduler from(Handler handler2) {
        if (handler2 != null) {
            return new HandlerScheduler(handler2);
        }
        throw new NullPointerException("handler == null");
    }

    HandlerScheduler(Handler handler2) {
        this.handler = handler2;
    }

    public Worker createWorker() {
        return new HandlerWorker(this.handler);
    }
}
