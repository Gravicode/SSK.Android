package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.internal.producers.SingleDelayedProducer;

/* renamed from: rx.internal.operators.OperatorToObservableList */
public final class OperatorToObservableList<T> implements Operator<List<T>, T> {

    /* renamed from: rx.internal.operators.OperatorToObservableList$Holder */
    static final class Holder {
        static final OperatorToObservableList<Object> INSTANCE = new OperatorToObservableList<>();

        Holder() {
        }
    }

    public static <T> OperatorToObservableList<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorToObservableList() {
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> o) {
        final SingleDelayedProducer<List<T>> producer = new SingleDelayedProducer<>(o);
        Subscriber<T> result = new Subscriber<T>() {
            boolean completed;
            List<T> list = new LinkedList();

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    try {
                        List<T> result = new ArrayList<>(this.list);
                        this.list = null;
                        producer.setValue(result);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, (Observer<?>) this);
                    }
                }
            }

            public void onError(Throwable e) {
                o.onError(e);
            }

            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        o.add(result);
        o.setProducer(producer);
        return result;
    }
}
