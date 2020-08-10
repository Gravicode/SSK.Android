package p008rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.observers.SerializedSubscriber;

/* renamed from: rx.internal.operators.OperatorSampleWithTime */
public final class OperatorSampleWithTime<T> implements Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    /* renamed from: rx.internal.operators.OperatorSampleWithTime$SamplerSubscriber */
    static final class SamplerSubscriber<T> extends Subscriber<T> implements Action0 {
        private static final Object EMPTY_TOKEN = new Object();
        private final Subscriber<? super T> subscriber;
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY_TOKEN);

        public SamplerSubscriber(Subscriber<? super T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onNext(T t) {
            this.value.set(t);
        }

        public void onError(Throwable e) {
            this.subscriber.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            emitIfNonEmpty();
            this.subscriber.onCompleted();
            unsubscribe();
        }

        public void call() {
            emitIfNonEmpty();
        }

        private void emitIfNonEmpty() {
            Object localValue = this.value.getAndSet(EMPTY_TOKEN);
            if (localValue != EMPTY_TOKEN) {
                try {
                    this.subscriber.onNext(localValue);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, (Observer<?>) this);
                }
            }
        }
    }

    public OperatorSampleWithTime(long time2, TimeUnit unit2, Scheduler scheduler2) {
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        Worker worker = this.scheduler.createWorker();
        child.add(worker);
        SamplerSubscriber samplerSubscriber = new SamplerSubscriber(s);
        child.add(samplerSubscriber);
        worker.schedulePeriodically(samplerSubscriber, this.time, this.time, this.unit);
        return samplerSubscriber;
    }
}
