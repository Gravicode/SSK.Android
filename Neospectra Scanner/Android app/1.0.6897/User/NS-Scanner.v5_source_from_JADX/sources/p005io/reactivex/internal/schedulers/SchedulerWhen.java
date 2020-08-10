package p005io.reactivex.internal.schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.Flowable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.processors.FlowableProcessor;
import p005io.reactivex.processors.UnicastProcessor;

@Experimental
/* renamed from: io.reactivex.internal.schedulers.SchedulerWhen */
public class SchedulerWhen extends Scheduler implements Disposable {
    static final Disposable DISPOSED = Disposables.disposed();
    static final Disposable SUBSCRIBED = new SubscribedDisposable();
    private final Scheduler actualScheduler;
    private Disposable disposable;
    private final FlowableProcessor<Flowable<Completable>> workerProcessor = UnicastProcessor.create().toSerialized();

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$CreateWorkerFunction */
    static final class CreateWorkerFunction implements Function<ScheduledAction, Completable> {
        final Worker actualWorker;

        /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$CreateWorkerFunction$WorkerCompletable */
        final class WorkerCompletable extends Completable {
            final ScheduledAction action;

            WorkerCompletable(ScheduledAction action2) {
                this.action = action2;
            }

            /* access modifiers changed from: protected */
            public void subscribeActual(CompletableObserver actionCompletable) {
                actionCompletable.onSubscribe(this.action);
                this.action.call(CreateWorkerFunction.this.actualWorker, actionCompletable);
            }
        }

        CreateWorkerFunction(Worker actualWorker2) {
            this.actualWorker = actualWorker2;
        }

        public Completable apply(ScheduledAction action) {
            return new WorkerCompletable(action);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$DelayedAction */
    static class DelayedAction extends ScheduledAction {
        private final Runnable action;
        private final long delayTime;
        private final TimeUnit unit;

        DelayedAction(Runnable action2, long delayTime2, TimeUnit unit2) {
            this.action = action2;
            this.delayTime = delayTime2;
            this.unit = unit2;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker actualWorker, CompletableObserver actionCompletable) {
            return actualWorker.schedule(new OnCompletedAction(this.action, actionCompletable), this.delayTime, this.unit);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ImmediateAction */
    static class ImmediateAction extends ScheduledAction {
        private final Runnable action;

        ImmediateAction(Runnable action2) {
            this.action = action2;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker actualWorker, CompletableObserver actionCompletable) {
            return actualWorker.schedule(new OnCompletedAction(this.action, actionCompletable));
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$OnCompletedAction */
    static class OnCompletedAction implements Runnable {
        final Runnable action;
        final CompletableObserver actionCompletable;

        OnCompletedAction(Runnable action2, CompletableObserver actionCompletable2) {
            this.action = action2;
            this.actionCompletable = actionCompletable2;
        }

        public void run() {
            try {
                this.action.run();
            } finally {
                this.actionCompletable.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$QueueWorker */
    static final class QueueWorker extends Worker {
        private final FlowableProcessor<ScheduledAction> actionProcessor;
        private final Worker actualWorker;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        QueueWorker(FlowableProcessor<ScheduledAction> actionProcessor2, Worker actualWorker2) {
            this.actionProcessor = actionProcessor2;
            this.actualWorker = actualWorker2;
        }

        public void dispose() {
            if (this.unsubscribed.compareAndSet(false, true)) {
                this.actionProcessor.onComplete();
                this.actualWorker.dispose();
            }
        }

        public boolean isDisposed() {
            return this.unsubscribed.get();
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
            DelayedAction delayedAction = new DelayedAction(action, delayTime, unit);
            this.actionProcessor.onNext(delayedAction);
            return delayedAction;
        }

        @NonNull
        public Disposable schedule(@NonNull Runnable action) {
            ImmediateAction immediateAction = new ImmediateAction(action);
            this.actionProcessor.onNext(immediateAction);
            return immediateAction;
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ScheduledAction */
    static abstract class ScheduledAction extends AtomicReference<Disposable> implements Disposable {
        /* access modifiers changed from: protected */
        public abstract Disposable callActual(Worker worker, CompletableObserver completableObserver);

        ScheduledAction() {
            super(SchedulerWhen.SUBSCRIBED);
        }

        /* access modifiers changed from: 0000 */
        public void call(Worker actualWorker, CompletableObserver actionCompletable) {
            Disposable oldState = (Disposable) get();
            if (oldState != SchedulerWhen.DISPOSED && oldState == SchedulerWhen.SUBSCRIBED) {
                Disposable newState = callActual(actualWorker, actionCompletable);
                if (!compareAndSet(SchedulerWhen.SUBSCRIBED, newState)) {
                    newState.dispose();
                }
            }
        }

        public boolean isDisposed() {
            return ((Disposable) get()).isDisposed();
        }

        public void dispose() {
            Disposable oldState;
            Disposable newState = SchedulerWhen.DISPOSED;
            do {
                oldState = (Disposable) get();
                if (oldState == SchedulerWhen.DISPOSED) {
                    return;
                }
            } while (!compareAndSet(oldState, newState));
            if (oldState != SchedulerWhen.SUBSCRIBED) {
                oldState.dispose();
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$SubscribedDisposable */
    static final class SubscribedDisposable implements Disposable {
        SubscribedDisposable() {
        }

        public void dispose() {
        }

        public boolean isDisposed() {
            return false;
        }
    }

    public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> combine, Scheduler actualScheduler2) {
        this.actualScheduler = actualScheduler2;
        try {
            this.disposable = ((Completable) combine.apply(this.workerProcessor)).subscribe();
        } catch (Throwable e) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public void dispose() {
        this.disposable.dispose();
    }

    public boolean isDisposed() {
        return this.disposable.isDisposed();
    }

    @NonNull
    public Worker createWorker() {
        Worker actualWorker = this.actualScheduler.createWorker();
        FlowableProcessor<ScheduledAction> actionProcessor = UnicastProcessor.create().toSerialized();
        Flowable<Completable> actions = actionProcessor.map(new CreateWorkerFunction(actualWorker));
        Worker worker = new QueueWorker(actionProcessor, actualWorker);
        this.workerProcessor.onNext(actions);
        return worker;
    }
}
