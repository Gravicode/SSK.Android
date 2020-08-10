package p008rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import p008rx.Observable.Operator;
import p008rx.Subscriber;

/* renamed from: rx.internal.operators.OperatorSkipLast */
public class OperatorSkipLast<T> implements Operator<T, T> {
    final int count;

    public OperatorSkipLast(int count2) {
        if (count2 < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.count = count2;
    }

    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            private final Deque<Object> deque = new ArrayDeque();

            public void onCompleted() {
                subscriber.onCompleted();
            }

            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            public void onNext(T value) {
                if (OperatorSkipLast.this.count == 0) {
                    subscriber.onNext(value);
                    return;
                }
                if (this.deque.size() == OperatorSkipLast.this.count) {
                    subscriber.onNext(NotificationLite.getValue(this.deque.removeFirst()));
                } else {
                    request(1);
                }
                this.deque.offerLast(NotificationLite.next(value));
            }
        };
    }
}
