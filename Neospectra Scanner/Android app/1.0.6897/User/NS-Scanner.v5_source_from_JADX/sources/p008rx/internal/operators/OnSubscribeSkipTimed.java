package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.OnSubscribeSkipTimed */
public final class OnSubscribeSkipTimed<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final Observable<T> source;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OnSubscribeSkipTimed$SkipTimedSubscriber */
    static final class SkipTimedSubscriber<T> extends Subscriber<T> implements Action0 {
        final Subscriber<? super T> child;
        volatile boolean gate;

        SkipTimedSubscriber(Subscriber<? super T> child2) {
            this.child = child2;
        }

        public void call() {
            this.gate = true;
        }

        public void onNext(T t) {
            if (this.gate) {
                this.child.onNext(t);
            }
        }

        public void onError(Throwable e) {
            try {
                this.child.onError(e);
            } finally {
                unsubscribe();
            }
        }

        public void onCompleted() {
            try {
                this.child.onCompleted();
            } finally {
                unsubscribe();
            }
        }
    }

    public OnSubscribeSkipTimed(Observable<T> source2, long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void call(Subscriber<? super T> child) {
        Worker worker = this.scheduler.createWorker();
        SkipTimedSubscriber<T> subscriber = new SkipTimedSubscriber<>(child);
        subscriber.add(worker);
        child.add(subscriber);
        worker.schedule(subscriber, this.time, this.unit);
        this.source.unsafeSubscribe(subscriber);
    }
}
