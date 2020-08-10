package p008rx.internal.schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Completable;
import p008rx.Completable.OnSubscribe;
import p008rx.CompletableSubscriber;
import p008rx.Observable;
import p008rx.Observer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.annotations.Experimental;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.operators.BufferUntilSubscriber;
import p008rx.observers.SerializedObserver;
import p008rx.subjects.PublishSubject;
import p008rx.subscriptions.Subscriptions;

@Experimental
/* renamed from: rx.internal.schedulers.SchedulerWhen */
public class SchedulerWhen extends Scheduler implements Subscription {
    static final Subscription SUBSCRIBED = new Subscription() {
        public void unsubscribe() {
        }

        public boolean isUnsubscribed() {
            return false;
        }
    };
    static final Subscription UNSUBSCRIBED = Subscriptions.unsubscribed();
    private final Scheduler actualScheduler;
    private final Subscription subscription;
    private final Observer<Observable<Completable>> workerObserver;

    /* renamed from: rx.internal.schedulers.SchedulerWhen$DelayedAction */
    private static class DelayedAction extends ScheduledAction {
        private final Action0 action;
        private final long delayTime;
        private final TimeUnit unit;

        public DelayedAction(Action0 action2, long delayTime2, TimeUnit unit2) {
            this.action = action2;
            this.delayTime = delayTime2;
            this.unit = unit2;
        }

        /* access modifiers changed from: protected */
        public Subscription callActual(Worker actualWorker) {
            return actualWorker.schedule(this.action, this.delayTime, this.unit);
        }
    }

    /* renamed from: rx.internal.schedulers.SchedulerWhen$ImmediateAction */
    private static class ImmediateAction extends ScheduledAction {
        private final Action0 action;

        public ImmediateAction(Action0 action2) {
            this.action = action2;
        }

        /* access modifiers changed from: protected */
        public Subscription callActual(Worker actualWorker) {
            return actualWorker.schedule(this.action);
        }
    }

    /* renamed from: rx.internal.schedulers.SchedulerWhen$ScheduledAction */
    private static abstract class ScheduledAction extends AtomicReference<Subscription> implements Subscription {
        /* access modifiers changed from: protected */
        public abstract Subscription callActual(Worker worker);

        public ScheduledAction() {
            super(SchedulerWhen.SUBSCRIBED);
        }

        /* access modifiers changed from: private */
        public void call(Worker actualWorker) {
            Subscription oldState = (Subscription) get();
            if (oldState != SchedulerWhen.UNSUBSCRIBED && oldState == SchedulerWhen.SUBSCRIBED) {
                Subscription newState = callActual(actualWorker);
                if (!compareAndSet(SchedulerWhen.SUBSCRIBED, newState)) {
                    newState.unsubscribe();
                }
            }
        }

        public boolean isUnsubscribed() {
            return ((Subscription) get()).isUnsubscribed();
        }

        public void unsubscribe() {
            Subscription oldState;
            Subscription newState = SchedulerWhen.UNSUBSCRIBED;
            do {
                oldState = (Subscription) get();
                if (oldState == SchedulerWhen.UNSUBSCRIBED) {
                    return;
                }
            } while (!compareAndSet(oldState, newState));
            if (oldState != SchedulerWhen.SUBSCRIBED) {
                oldState.unsubscribe();
            }
        }
    }

    public SchedulerWhen(Func1<Observable<Observable<Completable>>, Completable> combine, Scheduler actualScheduler2) {
        this.actualScheduler = actualScheduler2;
        PublishSubject<Observable<Completable>> workerSubject = PublishSubject.create();
        this.workerObserver = new SerializedObserver(workerSubject);
        this.subscription = ((Completable) combine.call(workerSubject.onBackpressureBuffer())).subscribe();
    }

    public void unsubscribe() {
        this.subscription.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return this.subscription.isUnsubscribed();
    }

    public Worker createWorker() {
        final Worker actualWorker = this.actualScheduler.createWorker();
        BufferUntilSubscriber<ScheduledAction> actionSubject = BufferUntilSubscriber.create();
        final Observer<ScheduledAction> actionObserver = new SerializedObserver<>(actionSubject);
        Observable<Completable> actions = actionSubject.map(new Func1<ScheduledAction, Completable>() {
            public Completable call(final ScheduledAction action) {
                return Completable.create(new OnSubscribe() {
                    public void call(CompletableSubscriber actionCompletable) {
                        actionCompletable.onSubscribe(action);
                        action.call(actualWorker);
                        actionCompletable.onCompleted();
                    }
                });
            }
        });
        Worker worker = new Worker() {
            private final AtomicBoolean unsubscribed = new AtomicBoolean();

            public void unsubscribe() {
                if (this.unsubscribed.compareAndSet(false, true)) {
                    actualWorker.unsubscribe();
                    actionObserver.onCompleted();
                }
            }

            public boolean isUnsubscribed() {
                return this.unsubscribed.get();
            }

            public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
                DelayedAction delayedAction = new DelayedAction(action, delayTime, unit);
                actionObserver.onNext(delayedAction);
                return delayedAction;
            }

            public Subscription schedule(Action0 action) {
                ImmediateAction immediateAction = new ImmediateAction(action);
                actionObserver.onNext(immediateAction);
                return immediateAction;
            }
        };
        this.workerObserver.onNext(actions);
        return worker;
    }
}
