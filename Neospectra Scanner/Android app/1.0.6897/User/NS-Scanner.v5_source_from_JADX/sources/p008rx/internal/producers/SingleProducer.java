package p008rx.internal.producers;

import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;

/* renamed from: rx.internal.producers.SingleProducer */
public final class SingleProducer<T> extends AtomicBoolean implements Producer {
    private static final long serialVersionUID = -3353584923995471404L;
    final Subscriber<? super T> child;
    final T value;

    public SingleProducer(Subscriber<? super T> child2, T value2) {
        this.child = child2;
        this.value = value2;
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        }
        if (n != 0 && compareAndSet(false, true)) {
            Subscriber<? super T> c = this.child;
            if (!c.isUnsubscribed()) {
                T v = this.value;
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
}
