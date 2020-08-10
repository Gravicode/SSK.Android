package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.Notification;
import p005io.reactivex.internal.util.BlockingHelper;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.subscribers.DisposableSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext */
public final class BlockingFlowableNext<T> implements Iterable<T> {
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext$NextIterator */
    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error;
        private boolean hasNext = true;
        private boolean isNextConsumed = true;
        private final Publisher<? extends T> items;
        private T next;
        private final NextSubscriber<T> observer;
        private boolean started;

        NextIterator(Publisher<? extends T> items2, NextSubscriber<T> observer2) {
            this.items = items2;
            this.observer = observer2;
        }

        public boolean hasNext() {
            if (this.error != null) {
                throw ExceptionHelper.wrapOrThrow(this.error);
            }
            boolean z = false;
            if (!this.hasNext) {
                return false;
            }
            if (!this.isNextConsumed || moveToNext()) {
                z = true;
            }
            return z;
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting();
                    Flowable.fromPublisher(this.items).materialize().subscribe((FlowableSubscriber<? super T>) this.observer);
                }
                Notification<T> nextNotification = this.observer.takeNext();
                if (nextNotification.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = nextNotification.getValue();
                    return true;
                }
                this.hasNext = false;
                if (nextNotification.isOnComplete()) {
                    return false;
                }
                if (nextNotification.isOnError()) {
                    this.error = nextNotification.getError();
                    throw ExceptionHelper.wrapOrThrow(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (InterruptedException e) {
                this.observer.dispose();
                this.error = e;
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }

        public T next() {
            if (this.error != null) {
                throw ExceptionHelper.wrapOrThrow(this.error);
            } else if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            } else {
                throw new NoSuchElementException("No more elements");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext$NextSubscriber */
    static final class NextSubscriber<T> extends DisposableSubscriber<Notification<T>> {
        private final BlockingQueue<Notification<T>> buf = new ArrayBlockingQueue(1);
        final AtomicInteger waiting = new AtomicInteger();

        NextSubscriber() {
        }

        public void onComplete() {
        }

        public void onError(Throwable e) {
            RxJavaPlugins.onError(e);
        }

        public void onNext(Notification<T> args) {
            if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
                Notification<T> toOffer = args;
                while (!this.buf.offer(toOffer)) {
                    Notification<T> concurrentItem = (Notification) this.buf.poll();
                    if (concurrentItem != null && !concurrentItem.isOnNext()) {
                        toOffer = concurrentItem;
                    }
                }
            }
        }

        public Notification<T> takeNext() throws InterruptedException {
            setWaiting();
            BlockingHelper.verifyNonBlocking();
            return (Notification) this.buf.take();
        }

        /* access modifiers changed from: 0000 */
        public void setWaiting() {
            this.waiting.set(1);
        }
    }

    public BlockingFlowableNext(Publisher<? extends T> source2) {
        this.source = source2;
    }

    public Iterator<T> iterator() {
        return new NextIterator(this.source, new NextSubscriber<>());
    }
}
