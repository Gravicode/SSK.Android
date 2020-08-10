package p008rx.internal.operators;

import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.functions.Action0;

/* renamed from: rx.internal.operators.SingleObserveOn */
public final class SingleObserveOn<T> implements OnSubscribe<T> {
    final Scheduler scheduler;
    final OnSubscribe<T> source;

    /* renamed from: rx.internal.operators.SingleObserveOn$ObserveOnSingleSubscriber */
    static final class ObserveOnSingleSubscriber<T> extends SingleSubscriber<T> implements Action0 {
        final SingleSubscriber<? super T> actual;
        Throwable error;
        T value;

        /* renamed from: w */
        final Worker f912w;

        public ObserveOnSingleSubscriber(SingleSubscriber<? super T> actual2, Worker w) {
            this.actual = actual2;
            this.f912w = w;
        }

        public void onSuccess(T value2) {
            this.value = value2;
            this.f912w.schedule(this);
        }

        public void onError(Throwable error2) {
            this.error = error2;
            this.f912w.schedule(this);
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
                this.f912w.unsubscribe();
            }
        }
    }

    public SingleObserveOn(OnSubscribe<T> source2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
    }

    public void call(SingleSubscriber<? super T> t) {
        Worker w = this.scheduler.createWorker();
        ObserveOnSingleSubscriber<T> parent = new ObserveOnSingleSubscriber<>(t, w);
        t.add(w);
        t.add(parent);
        this.source.call(parent);
    }
}
