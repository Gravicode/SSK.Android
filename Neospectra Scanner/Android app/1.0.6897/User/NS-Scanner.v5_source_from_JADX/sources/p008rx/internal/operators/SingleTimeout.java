package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.functions.Action0;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.SingleTimeout */
public final class SingleTimeout<T> implements OnSubscribe<T> {
    final OnSubscribe<? extends T> other;
    final Scheduler scheduler;
    final OnSubscribe<T> source;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.SingleTimeout$TimeoutSingleSubscriber */
    static final class TimeoutSingleSubscriber<T> extends SingleSubscriber<T> implements Action0 {
        final SingleSubscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final OnSubscribe<? extends T> other;

        /* renamed from: rx.internal.operators.SingleTimeout$TimeoutSingleSubscriber$OtherSubscriber */
        static final class OtherSubscriber<T> extends SingleSubscriber<T> {
            final SingleSubscriber<? super T> actual;

            OtherSubscriber(SingleSubscriber<? super T> actual2) {
                this.actual = actual2;
            }

            public void onSuccess(T value) {
                this.actual.onSuccess(value);
            }

            public void onError(Throwable error) {
                this.actual.onError(error);
            }
        }

        TimeoutSingleSubscriber(SingleSubscriber<? super T> actual2, OnSubscribe<? extends T> other2) {
            this.actual = actual2;
            this.other = other2;
        }

        public void onSuccess(T value) {
            if (this.once.compareAndSet(false, true)) {
                try {
                    this.actual.onSuccess(value);
                } finally {
                    unsubscribe();
                }
            }
        }

        public void onError(Throwable error) {
            if (this.once.compareAndSet(false, true)) {
                try {
                    this.actual.onError(error);
                } finally {
                    unsubscribe();
                }
            } else {
                RxJavaHooks.onError(error);
            }
        }

        public void call() {
            if (this.once.compareAndSet(false, true)) {
                try {
                    OnSubscribe<? extends T> o = this.other;
                    if (o == null) {
                        this.actual.onError(new TimeoutException());
                    } else {
                        OtherSubscriber<T> p = new OtherSubscriber<>(this.actual);
                        this.actual.add(p);
                        o.call(p);
                    }
                } finally {
                    unsubscribe();
                }
            }
        }
    }

    public SingleTimeout(OnSubscribe<T> source2, long timeout2, TimeUnit unit2, Scheduler scheduler2, OnSubscribe<? extends T> other2) {
        this.source = source2;
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void call(SingleSubscriber<? super T> t) {
        TimeoutSingleSubscriber<T> parent = new TimeoutSingleSubscriber<>(t, this.other);
        Worker w = this.scheduler.createWorker();
        parent.add(w);
        t.add(parent);
        w.schedule(parent, this.timeout, this.unit);
        this.source.call(parent);
    }
}
