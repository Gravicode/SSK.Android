package p008rx.internal.producers;

import java.util.concurrent.atomic.AtomicInteger;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.internal.producers.SingleDelayedProducer */
public final class SingleDelayedProducer<T> extends AtomicInteger implements Producer {
    static final int HAS_REQUEST_HAS_VALUE = 3;
    static final int HAS_REQUEST_NO_VALUE = 2;
    static final int NO_REQUEST_HAS_VALUE = 1;
    static final int NO_REQUEST_NO_VALUE = 0;
    private static final long serialVersionUID = -2873467947112093874L;
    final Subscriber<? super T> child;
    T value;

    public SingleDelayedProducer(Subscriber<? super T> child2) {
        this.child = child2;
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n != 0) {
            while (true) {
                int s = get();
                if (s == 0) {
                    if (compareAndSet(0, 2)) {
                        break;
                    }
                } else if (s == 1 && compareAndSet(1, 3)) {
                    emit(this.child, this.value);
                }
            }
        }
    }

    public void setValue(T value2) {
        do {
            int s = get();
            if (s == 0) {
                this.value = value2;
            } else if (s == 2 && compareAndSet(2, 3)) {
                emit(this.child, value2);
                return;
            } else {
                return;
            }
        } while (!compareAndSet(0, 1));
    }

    private static <T> void emit(Subscriber<? super T> c, T v) {
        if (!c.isUnsubscribed()) {
            try {
                c.onNext(v);
                if (!c.isUnsubscribed()) {
                    c.onCompleted();
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, (Observer<?>) c, (Object) v);
            }
        }
    }
}
