package p008rx.internal.operators;

import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.internal.producers.ProducerArbiter;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorSwitchIfEmpty */
public final class OperatorSwitchIfEmpty<T> implements Operator<T, T> {
    private final Observable<? extends T> alternate;

    /* renamed from: rx.internal.operators.OperatorSwitchIfEmpty$AlternateSubscriber */
    static final class AlternateSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final Subscriber<? super T> child;

        AlternateSubscriber(Subscriber<? super T> child2, ProducerArbiter arbiter2) {
            this.child = child2;
            this.arbiter = arbiter2;
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        public void onCompleted() {
            this.child.onCompleted();
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(T t) {
            this.child.onNext(t);
            this.arbiter.produced(1);
        }
    }

    /* renamed from: rx.internal.operators.OperatorSwitchIfEmpty$ParentSubscriber */
    static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Observable<? extends T> alternate;
        private final ProducerArbiter arbiter;
        private final Subscriber<? super T> child;
        private boolean empty = true;
        private final SerialSubscription serial;

        ParentSubscriber(Subscriber<? super T> child2, SerialSubscription serial2, ProducerArbiter arbiter2, Observable<? extends T> alternate2) {
            this.child = child2;
            this.serial = serial2;
            this.arbiter = arbiter2;
            this.alternate = alternate2;
        }

        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        public void onCompleted() {
            if (!this.empty) {
                this.child.onCompleted();
            } else if (!this.child.isUnsubscribed()) {
                subscribeToAlternate();
            }
        }

        private void subscribeToAlternate() {
            AlternateSubscriber<T> as = new AlternateSubscriber<>(this.child, this.arbiter);
            this.serial.set(as);
            this.alternate.unsafeSubscribe(as);
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(T t) {
            this.empty = false;
            this.child.onNext(t);
            this.arbiter.produced(1);
        }
    }

    public OperatorSwitchIfEmpty(Observable<? extends T> alternate2) {
        this.alternate = alternate2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerialSubscription serial = new SerialSubscription();
        ProducerArbiter arbiter = new ProducerArbiter();
        ParentSubscriber<T> parent = new ParentSubscriber<>(child, serial, arbiter, this.alternate);
        serial.set(parent);
        child.add(serial);
        child.setProducer(arbiter);
        return parent;
    }
}
