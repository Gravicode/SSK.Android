package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p008rx.Observable.Operator;
import p008rx.Observer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Func2;
import p008rx.internal.producers.SingleDelayedProducer;

/* renamed from: rx.internal.operators.OperatorToObservableSortedList */
public final class OperatorToObservableSortedList<T> implements Operator<List<T>, T> {
    private static final Comparator DEFAULT_SORT_FUNCTION = new DefaultComparableFunction();
    final int initialCapacity;
    final Comparator<? super T> sortFunction;

    /* renamed from: rx.internal.operators.OperatorToObservableSortedList$DefaultComparableFunction */
    static final class DefaultComparableFunction implements Comparator<Object> {
        DefaultComparableFunction() {
        }

        public int compare(Object t1, Object t2) {
            return ((Comparable) t1).compareTo((Comparable) t2);
        }
    }

    public OperatorToObservableSortedList(int initialCapacity2) {
        this.sortFunction = DEFAULT_SORT_FUNCTION;
        this.initialCapacity = initialCapacity2;
    }

    public OperatorToObservableSortedList(final Func2<? super T, ? super T, Integer> sortFunction2, int initialCapacity2) {
        this.initialCapacity = initialCapacity2;
        this.sortFunction = new Comparator<T>() {
            public int compare(T o1, T o2) {
                return ((Integer) sortFunction2.call(o1, o2)).intValue();
            }
        };
    }

    public Subscriber<? super T> call(final Subscriber<? super List<T>> child) {
        final SingleDelayedProducer<List<T>> producer = new SingleDelayedProducer<>(child);
        Subscriber<T> result = new Subscriber<T>() {
            boolean completed;
            List<T> list = new ArrayList(OperatorToObservableSortedList.this.initialCapacity);

            public void onStart() {
                request(Long.MAX_VALUE);
            }

            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    List<T> a = this.list;
                    this.list = null;
                    try {
                        Collections.sort(a, OperatorToObservableSortedList.this.sortFunction);
                        producer.setValue(a);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, (Observer<?>) this);
                    }
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
            }

            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        child.add(result);
        child.setProducer(producer);
        return result;
    }
}
