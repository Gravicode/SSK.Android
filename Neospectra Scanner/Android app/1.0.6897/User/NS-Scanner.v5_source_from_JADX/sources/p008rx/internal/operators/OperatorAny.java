package p008rx.internal.operators;

import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func1;
import p008rx.internal.producers.SingleDelayedProducer;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OperatorAny */
public final class OperatorAny<T> implements Operator<Boolean, T> {
    final Func1<? super T, Boolean> predicate;
    final boolean returnOnEmpty;

    public OperatorAny(Func1<? super T, Boolean> predicate2, boolean returnOnEmpty2) {
        this.predicate = predicate2;
        this.returnOnEmpty = returnOnEmpty2;
    }

    public Subscriber<? super T> call(final Subscriber<? super Boolean> child) {
        final SingleDelayedProducer<Boolean> producer = new SingleDelayedProducer<>(child);
        Subscriber<T> s = new Subscriber<T>() {
            boolean done;
            boolean hasElements;

            public void onNext(T t) {
                if (!this.done) {
                    this.hasElements = true;
                    try {
                        if (((Boolean) OperatorAny.this.predicate.call(t)).booleanValue()) {
                            this.done = true;
                            producer.setValue(Boolean.valueOf(true ^ OperatorAny.this.returnOnEmpty));
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
                    if (this.hasElements) {
                        producer.setValue(Boolean.valueOf(false));
                    } else {
                        producer.setValue(Boolean.valueOf(OperatorAny.this.returnOnEmpty));
                    }
                }
            }
        };
        child.add(s);
        child.setProducer(producer);
        return s;
    }
}
