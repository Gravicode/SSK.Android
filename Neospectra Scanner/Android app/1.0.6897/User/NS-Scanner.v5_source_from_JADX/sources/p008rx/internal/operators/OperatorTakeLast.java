package p008rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.functions.Func1;

/* renamed from: rx.internal.operators.OperatorTakeLast */
public final class OperatorTakeLast<T> implements Operator<T, T> {
    final int count;

    /* renamed from: rx.internal.operators.OperatorTakeLast$TakeLastSubscriber */
    static final class TakeLastSubscriber<T> extends Subscriber<T> implements Func1<Object, T> {
        final Subscriber<? super T> actual;
        final int count;
        final ArrayDeque<Object> queue = new ArrayDeque<>();
        final AtomicLong requested = new AtomicLong();

        public TakeLastSubscriber(Subscriber<? super T> actual2, int count2) {
            this.actual = actual2;
            this.count = count2;
        }

        public void onNext(T t) {
            if (this.queue.size() == this.count) {
                this.queue.poll();
            }
            this.queue.offer(NotificationLite.next(t));
        }

        public void onError(Throwable e) {
            this.queue.clear();
            this.actual.onError(e);
        }

        public void onCompleted() {
            BackpressureUtils.postCompleteDone(this.requested, this.queue, this.actual, this);
        }

        public T call(Object t) {
            return NotificationLite.getValue(t);
        }

        /* access modifiers changed from: 0000 */
        public void requestMore(long n) {
            if (n > 0) {
                BackpressureUtils.postCompleteRequest(this.requested, n, this.queue, this.actual, this);
            }
        }
    }

    public OperatorTakeLast(int count2) {
        if (count2 < 0) {
            throw new IndexOutOfBoundsException("count cannot be negative");
        }
        this.count = count2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final TakeLastSubscriber<T> parent = new TakeLastSubscriber<>(subscriber, this.count);
        subscriber.add(parent);
        subscriber.setProducer(new Producer() {
            public void request(long n) {
                parent.requestMore(n);
            }
        });
        return parent;
    }
}
