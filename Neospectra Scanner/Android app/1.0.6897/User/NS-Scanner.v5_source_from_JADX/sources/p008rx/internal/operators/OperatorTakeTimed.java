package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable.Operator;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorTakeTimed */
public final class OperatorTakeTimed<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorTakeTimed$TakeSubscriber */
    static final class TakeSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;

        public TakeSubscriber(Subscriber<? super T> child2) {
            super(child2);
            this.child = child2;
        }

        public void onNext(T t) {
            this.child.onNext(t);
        }

        public void onError(Throwable e) {
            this.child.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.child.onCompleted();
            unsubscribe();
        }

        public void call() {
            onCompleted();
        }
    }

    public OperatorTakeTimed(long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        TakeSubscriber<T> ts = new TakeSubscriber<>(new SerializedSubscriber(child));
        worker.schedule(ts, this.time, this.unit);
        return ts;
    }
}
