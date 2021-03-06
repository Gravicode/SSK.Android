package p008rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import p008rx.Observable;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.internal.operators.BlockingOperatorMostRecent */
public final class BlockingOperatorMostRecent {

    /* renamed from: rx.internal.operators.BlockingOperatorMostRecent$MostRecentObserver */
    static final class MostRecentObserver<T> extends Subscriber<T> {
        volatile Object value;

        MostRecentObserver(T value2) {
            this.value = NotificationLite.next(value2);
        }

        public void onCompleted() {
            this.value = NotificationLite.completed();
        }

        public void onError(Throwable e) {
            this.value = NotificationLite.error(e);
        }

        public void onNext(T args) {
            this.value = NotificationLite.next(args);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                private Object buf;

                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !NotificationLite.isCompleted(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentObserver.this.value;
                        }
                        if (NotificationLite.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (NotificationLite.isError(this.buf)) {
                            throw Exceptions.propagate(NotificationLite.getError(this.buf));
                        } else {
                            T value = NotificationLite.getValue(this.buf);
                            this.buf = obj;
                            return value;
                        }
                    } finally {
                        this.buf = obj;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }

    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> source, final T initialValue) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                MostRecentObserver<T> mostRecentObserver = new MostRecentObserver<>(initialValue);
                source.subscribe((Subscriber<? super T>) mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }
}
