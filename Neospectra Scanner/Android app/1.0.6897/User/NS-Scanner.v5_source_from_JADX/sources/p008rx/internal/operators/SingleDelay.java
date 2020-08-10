package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.SingleDelay */
public final class SingleDelay<T> implements OnSubscribe<T> {
    final long delay;
    final Scheduler scheduler;
    final OnSubscribe<T> source;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.SingleDelay$ObserveOnSingleSubscriber */
    static final class ObserveOnSingleSubscriber<T> extends SingleSubscriber<T> implements Action0 {
        final SingleSubscriber<? super T> actual;
        final long delay;
        Throwable error;
        final TimeUnit unit;
        T value;

        /* renamed from: w */
        final Worker f911w;

        public ObserveOnSingleSubscriber(SingleSubscriber<? super T> actual2, Worker w, long delay2, TimeUnit unit2) {
            this.actual = actual2;
            this.f911w = w;
            this.delay = delay2;
            this.unit = unit2;
        }

        public void onSuccess(T value2) {
            this.value = value2;
            this.f911w.schedule(this, this.delay, this.unit);
        }

        public void onError(Throwable error2) {
            this.error = error2;
            this.f911w.schedule(this, this.delay, this.unit);
        }

        public void call() {
            try {
                Throwable ex = this.error;
                if (ex != null) {
                    this.error = null;
                    this.actual.onError(ex);
                } else {
                    T v = this.value;
                    this.value = null;
                    this.actual.onSuccess(v);
                }
            } finally {
                this.f911w.unsubscribe();
            }
        }
    }

    public SingleDelay(OnSubscribe<T> source2, long delay2, TimeUnit unit2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
        this.delay = delay2;
        this.unit = unit2;
    }

    public void call(SingleSubscriber<? super T> t) {
        Worker w = this.scheduler.createWorker();
        ObserveOnSingleSubscriber<T> parent = new ObserveOnSingleSubscriber<>(t, w, this.delay, this.unit);
        t.add(w);
        t.add(parent);
        this.source.call(parent);
    }
}
