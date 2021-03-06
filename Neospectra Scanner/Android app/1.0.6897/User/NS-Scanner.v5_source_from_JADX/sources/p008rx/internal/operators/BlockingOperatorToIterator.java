package p008rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import p008rx.Notification;
import p008rx.Observable;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.internal.util.RxRingBuffer;

/* renamed from: rx.internal.operators.BlockingOperatorToIterator */
public final class BlockingOperatorToIterator {

    /* renamed from: rx.internal.operators.BlockingOperatorToIterator$SubscriberIterator */
    public static final class SubscriberIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        static final int LIMIT = ((RxRingBuffer.SIZE * 3) / 4);
        private Notification<? extends T> buf;
        private final BlockingQueue<Notification<? extends T>> notifications = new LinkedBlockingQueue();
        private int received;

        public void onStart() {
            request((long) RxRingBuffer.SIZE);
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
            this.notifications.offer(Notification.createOnError(e));
        }

        public void onNext(Notification<? extends T> args) {
            this.notifications.offer(args);
        }

        public boolean hasNext() {
            if (this.buf == null) {
                this.buf = take();
                this.received++;
                if (this.received >= LIMIT) {
                    request((long) this.received);
                    this.received = 0;
                }
            }
            if (!this.buf.isOnError()) {
                return !this.buf.isOnCompleted();
            }
            throw Exceptions.propagate(this.buf.getThrowable());
        }

        public T next() {
            if (hasNext()) {
                T result = this.buf.getValue();
                this.buf = null;
                return result;
            }
            throw new NoSuchElementException();
        }

        private Notification<? extends T> take() {
            try {
                Notification<? extends T> poll = (Notification) this.notifications.poll();
                if (poll != null) {
                    return poll;
                }
                return (Notification) this.notifications.take();
            } catch (InterruptedException e) {
                unsubscribe();
                throw Exceptions.propagate(e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator");
        }
    }

    private BlockingOperatorToIterator() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterator<T> toIterator(Observable<? extends T> source) {
        SubscriberIterator<T> subscriber = new SubscriberIterator<>();
        source.materialize().subscribe((Subscriber<? super T>) subscriber);
        return subscriber;
    }
}
