package p008rx.subjects;

import java.util.concurrent.TimeUnit;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Scheduler.Worker;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.internal.operators.NotificationLite;
import p008rx.schedulers.TestScheduler;

/* renamed from: rx.subjects.TestSubject */
public final class TestSubject<T> extends Subject<T, T> {
    private final Worker innerScheduler;
    private final SubjectSubscriptionManager<T> state;

    public static <T> TestSubject<T> create(TestScheduler scheduler) {
        final SubjectSubscriptionManager<T> state2 = new SubjectSubscriptionManager<>();
        state2.onAdded = new Action1<SubjectObserver<T>>() {
            public void call(SubjectObserver<T> o) {
                o.emitFirst(state2.getLatest());
            }
        };
        state2.onTerminated = state2.onAdded;
        return new TestSubject<>(state2, state2, scheduler);
    }

    protected TestSubject(OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state2, TestScheduler scheduler) {
        super(onSubscribe);
        this.state = state2;
        this.innerScheduler = scheduler.createWorker();
    }

    public void onCompleted() {
        onCompleted(0);
    }

    /* access modifiers changed from: 0000 */
    public void internalOnCompleted() {
        if (this.state.active) {
            for (SubjectObserver<T> bo : this.state.terminate(NotificationLite.completed())) {
                bo.onCompleted();
            }
        }
    }

    public void onCompleted(long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this.internalOnCompleted();
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public void onError(Throwable e) {
        onError(e, 0);
    }

    /* access modifiers changed from: 0000 */
    public void internalOnError(Throwable e) {
        if (this.state.active) {
            for (SubjectObserver<T> bo : this.state.terminate(NotificationLite.error(e))) {
                bo.onError(e);
            }
        }
    }

    public void onError(final Throwable e, long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this.internalOnError(e);
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public void onNext(T v) {
        onNext(v, 0);
    }

    /* access modifiers changed from: 0000 */
    public void internalOnNext(T v) {
        for (Observer<? super T> o : this.state.observers()) {
            o.onNext(v);
        }
    }

    public void onNext(final T v, long delayTime) {
        this.innerScheduler.schedule(new Action0() {
            public void call() {
                TestSubject.this.internalOnNext(v);
            }
        }, delayTime, TimeUnit.MILLISECONDS);
    }

    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }
}
