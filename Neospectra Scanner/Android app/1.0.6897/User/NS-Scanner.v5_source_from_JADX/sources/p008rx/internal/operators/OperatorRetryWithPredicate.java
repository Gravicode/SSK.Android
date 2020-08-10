package p008rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Scheduler.Worker;
import p008rx.Subscriber;
import p008rx.functions.Action0;
import p008rx.functions.Func2;
import p008rx.internal.producers.ProducerArbiter;
import p008rx.schedulers.Schedulers;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OperatorRetryWithPredicate */
public final class OperatorRetryWithPredicate<T> implements Operator<T, Observable<T>> {
    final Func2<Integer, Throwable, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorRetryWithPredicate$SourceSubscriber */
    static final class SourceSubscriber<T> extends Subscriber<Observable<T>> {
        final AtomicInteger attempts = new AtomicInteger();
        final Subscriber<? super T> child;
        final Worker inner;

        /* renamed from: pa */
        final ProducerArbiter f909pa;
        final Func2<Integer, Throwable, Boolean> predicate;
        final SerialSubscription serialSubscription;

        public SourceSubscriber(Subscriber<? super T> child2, Func2<Integer, Throwable, Boolean> predicate2, Worker inner2, SerialSubscription serialSubscription2, ProducerArbiter pa) {
            this.child = child2;
            this.predicate = predicate2;
            this.inner = inner2;
            this.serialSubscription = serialSubscription2;
            this.f909pa = pa;
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }

        public void onNext(final Observable<T> o) {
            this.inner.schedule(new Action0() {
                public void call() {
                    SourceSubscriber.this.attempts.incrementAndGet();
                    Subscriber<T> subscriber = new Subscriber<T>() {
                        boolean done;

                        public void onCompleted() {
                            if (!this.done) {
                                this.done = true;
                                SourceSubscriber.this.child.onCompleted();
                            }
                        }

                        public void onError(Throwable e) {
                            if (!this.done) {
                                this.done = true;
                                if (!((Boolean) SourceSubscriber.this.predicate.call(Integer.valueOf(SourceSubscriber.this.attempts.get()), e)).booleanValue() || SourceSubscriber.this.inner.isUnsubscribed()) {
                                    SourceSubscriber.this.child.onError(e);
                                } else {
                                    SourceSubscriber.this.inner.schedule(this);
                                }
                            }
                        }

                        public void onNext(T v) {
                            if (!this.done) {
                                SourceSubscriber.this.child.onNext(v);
                                SourceSubscriber.this.f909pa.produced(1);
                            }
                        }

                        public void setProducer(Producer p) {
                            SourceSubscriber.this.f909pa.setProducer(p);
                        }
                    };
                    SourceSubscriber.this.serialSubscription.set(subscriber);
                    o.unsafeSubscribe(subscriber);
                }
            });
        }
    }

    public OperatorRetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super Observable<T>> call(Subscriber<? super T> child) {
        Worker inner = Schedulers.trampoline().createWorker();
        child.add(inner);
        SerialSubscription serialSubscription = new SerialSubscription();
        child.add(serialSubscription);
        ProducerArbiter pa = new ProducerArbiter();
        child.setProducer(pa);
        SourceSubscriber sourceSubscriber = new SourceSubscriber(child, this.predicate, inner, serialSubscription, pa);
        return sourceSubscriber;
    }
}
