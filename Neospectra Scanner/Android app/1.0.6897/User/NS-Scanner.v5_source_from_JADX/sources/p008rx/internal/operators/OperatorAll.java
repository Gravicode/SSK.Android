package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.internal.producers.SingleDelayedProducer;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorAll */
public final class OperatorAll<T> implements Operator<Boolean, T> {
    final Func1<? super T, Boolean> predicate;

    public OperatorAll(Func1<? super T, Boolean> predicate2) {
        this.predicate = predicate2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer<>(child);
        Subscriber<T> s = new Subscriber<T>() {
            boolean done;

            public void onNext(T t) {
                if (!this.done) {
                    try {
                        if (!((Boolean) OperatorAll.this.predicate.call(t)).booleanValue()) {
                            this.done = true;
                            producer.setValue(Boolean.valueOf(false));
                            unsubscribe();
                        }
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, (Observer<?>) this, (Object) t);
                    }
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    this.done = true;
                    child.onError(e);
                    return;
                }
                RxJavaHooks.onError(e);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    producer.setValue(Boolean.valueOf(true));
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
