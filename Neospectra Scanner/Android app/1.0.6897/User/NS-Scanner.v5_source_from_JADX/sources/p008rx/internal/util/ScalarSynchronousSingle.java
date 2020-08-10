package p008rx.internal.util;

import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Single;
import p008rx.Single.OnSubscribe;
import p008rx.SingleSubscriber;
import p008rx.functions.Action0;
import p008rx.functions.Func1;
import p008rx.internal.schedulers.EventLoopsScheduler;

/* renamed from: rx.internal.util.ScalarSynchronousSingle */
public final class ScalarSynchronousSingle<T> extends Single<T> {
    final T value;

    /* renamed from: rx.internal.util.ScalarSynchronousSingle$DirectScheduledEmission */
    static final class DirectScheduledEmission<T> implements OnSubscribe<T> {

        /* renamed from: es */
        private final EventLoopsScheduler f918es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler es, T value2) {
            this.f918es = es;
            this.value = value2;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            singleSubscriber.add(this.f918es.scheduleDirect(new ScalarSynchronousSingleAction(singleSubscriber, this.value)));
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousSingle$NormalScheduledEmission */
    static final class NormalScheduledEmission<T> implements OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler2, T value2) {
            this.scheduler = scheduler2;
            this.value = value2;
        }

        public void call(SingleSubscriber<? super T> singleSubscriber) {
            Worker worker = this.scheduler.createWorker();
            singleSubscriber.add(worker);
            worker.schedule(new ScalarSynchronousSingleAction(singleSubscriber, this.value));
        }
    }

    /* renamed from: rx.internal.util.ScalarSynchronousSingle$ScalarSynchronousSingleAction */
    static final class ScalarSynchronousSingleAction<T> implements Action0 {
        private final SingleSubscriber<? super T> subscriber;
        private final T value;

        ScalarSynchronousSingleAction(SingleSubscriber<? super T> subscriber2, T value2) {
            this.subscriber = subscriber2;
            this.value = value2;
        }

        public void call() {
            try {
                this.subscriber.onSuccess(this.value);
            } catch (Throwable t) {
                this.subscriber.onError(t);
            }
        }
    }

    public static <T> ScalarSynchronousSingle<T> create(T t) {
        return new ScalarSynchronousSingle<>(t);
    }

    protected ScalarSynchronousSingle(final T t) {
        super((OnSubscribe<T>) new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> te) {
                te.onSuccess(t);
            }
        });
        this.value = t;
    }

    public T get() {
        return this.value;
    }

    public Single<T> scalarScheduleOn(Scheduler scheduler) {
        if (scheduler instanceof EventLoopsScheduler) {
            return create(new DirectScheduledEmission((EventLoopsScheduler) scheduler, this.value));
        }
        return create(new NormalScheduledEmission(scheduler, this.value));
    }

    public <R> Single<R> scalarFlatMap(final Func1<? super T, ? extends Single<? extends R>> func) {
        return create(new OnSubscribe<R>() {
            public void call(final SingleSubscriber<? super R> child) {
                Single<? extends R> o = (Single) func.call(ScalarSynchronousSingle.this.value);
                if (o instanceof ScalarSynchronousSingle) {
                    child.onSuccess(((ScalarSynchronousSingle) o).value);
                    return;
                }
                SingleSubscriber<R> subscriber = new SingleSubscriber<R>() {
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    public void onSuccess(R r) {
                        child.onSuccess(r);
                    }
                };
                child.add(subscriber);
                o.subscribe(subscriber);
            }
        });
    }
}
