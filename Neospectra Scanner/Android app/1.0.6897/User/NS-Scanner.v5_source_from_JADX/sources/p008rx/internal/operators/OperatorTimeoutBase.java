package p008rx.internal.operators;

import java.util.concurrent.TimeoutException;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.functions.Func3;
import p008rx.functions.Func4;
import p008rx.internal.producers.ProducerArbiter;
import p008rx.observers.SerializedSubscriber;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorTimeoutBase */
class OperatorTimeoutBase<T> implements Operator<T, T> {
    final FirstTimeoutStub<T> firstTimeoutStub;
    final Observable<? extends T> other;
    final Scheduler scheduler;
    final TimeoutStub<T> timeoutStub;

    /* renamed from: rx.internal.operators.OperatorTimeoutBase$FirstTimeoutStub */
    interface FirstTimeoutStub<T> extends Func3<TimeoutSubscriber<T>, Long, Worker, Subscription> {
    }

    /* renamed from: rx.internal.operators.OperatorTimeoutBase$TimeoutStub */
    interface TimeoutStub<T> extends Func4<TimeoutSubscriber<T>, Long, T, Worker, Subscription> {
    }

    /* renamed from: rx.internal.operators.OperatorTimeoutBase$TimeoutSubscriber */
    static final class TimeoutSubscriber<T> extends Subscriber<T> {
        long actual;
        final ProducerArbiter arbiter = new ProducerArbiter();
        final Worker inner;
        final Observable<? extends T> other;
        final SerialSubscription serial;
        final SerializedSubscriber<T> serializedSubscriber;
        boolean terminated;
        final TimeoutStub<T> timeoutStub;

        TimeoutSubscriber(SerializedSubscriber<T> serializedSubscriber2, TimeoutStub<T> timeoutStub2, SerialSubscription serial2, Observable<? extends T> other2, Worker inner2) {
            this.serializedSubscriber = serializedSubscriber2;
            this.timeoutStub = timeoutStub2;
            this.serial = serial2;
            this.other = other2;
            this.inner = inner2;
        }

        public void setProducer(Producer p) {
            this.arbiter.setProducer(p);
        }

        public void onNext(T value) {
            long a;
            boolean onNextWins = false;
            synchronized (this) {
                if (!this.terminated) {
                    long j = this.actual + 1;
                    this.actual = j;
                    a = j;
                    onNextWins = true;
                } else {
                    a = this.actual;
                }
            }
            if (onNextWins) {
                this.serializedSubscriber.onNext(value);
                this.serial.set((Subscription) this.timeoutStub.call(this, Long.valueOf(a), value, this.inner));
            }
        }

        public void onError(Throwable error) {
            boolean onErrorWins = false;
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    onErrorWins = true;
                }
            }
            if (onErrorWins) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onError(error);
            }
        }

        public void onCompleted() {
            boolean onCompletedWins = false;
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    onCompletedWins = true;
                }
            }
            if (onCompletedWins) {
                this.serial.unsubscribe();
                this.serializedSubscriber.onCompleted();
            }
        }

        public void onTimeout(long seqId) {
            long expected = seqId;
            boolean timeoutWins = false;
            synchronized (this) {
                if (expected == this.actual && !this.terminated) {
                    this.terminated = true;
                    timeoutWins = true;
                }
            }
            if (!timeoutWins) {
                return;
            }
            if (this.other == null) {
                this.serializedSubscriber.onError(new TimeoutException());
                return;
            }
            Subscriber<T> second = new Subscriber<T>() {
                public void onNext(T t) {
                    TimeoutSubscriber.this.serializedSubscriber.onNext(t);
                }

                public void onError(Throwable e) {
                    TimeoutSubscriber.this.serializedSubscriber.onError(e);
                }

                public void onCompleted() {
                    TimeoutSubscriber.this.serializedSubscriber.onCompleted();
                }

                public void setProducer(Producer p) {
                    TimeoutSubscriber.this.arbiter.setProducer(p);
                }
            };
            this.other.unsafeSubscribe(second);
            this.serial.set(second);
        }
    }

    OperatorTimeoutBase(FirstTimeoutStub<T> firstTimeoutStub2, TimeoutStub<T> timeoutStub2, Observable<? extends T> other2, Scheduler scheduler2) {
        this.firstTimeoutStub = firstTimeoutStub2;
        this.timeoutStub = timeoutStub2;
        this.other = other2;
        this.scheduler = scheduler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        Worker inner = this.scheduler.createWorker();
        subscriber.add(inner);
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        SerialSubscription serial = new SerialSubscription();
        serializedSubscriber.add(serial);
        TimeoutSubscriber<T> timeoutSubscriber = new TimeoutSubscriber<>(serializedSubscriber, this.timeoutStub, serial, this.other, inner);
        serializedSubscriber.add(timeoutSubscriber);
        serializedSubscriber.setProducer(timeoutSubscriber.arbiter);
        serial.set((Subscription) this.firstTimeoutStub.call(timeoutSubscriber, Long.valueOf(0), inner));
        return timeoutSubscriber;
    }
}
