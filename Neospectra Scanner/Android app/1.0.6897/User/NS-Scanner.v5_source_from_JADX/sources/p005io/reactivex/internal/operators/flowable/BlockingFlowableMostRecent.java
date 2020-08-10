package p005io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.subscribers.DefaultSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent */
public final class BlockingFlowableMostRecent<T> implements Iterable<T> {
    final T initialValue;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent$MostRecentSubscriber */
    static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
        volatile Object value;

        /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent$MostRecentSubscriber$Iterator */
        final class Iterator implements java.util.Iterator<T> {
            private Object buf;

            Iterator() {
            }

            public boolean hasNext() {
                this.buf = MostRecentSubscriber.this.value;
                return !NotificationLite.isComplete(this.buf);
            }

            public T next() {
                Object obj = null;
                try {
                    if (this.buf == null) {
                        obj = MostRecentSubscriber.this.value;
                    }
                    if (NotificationLite.isComplete(this.buf)) {
                        throw new NoSuchElementException();
                    } else if (NotificationLite.isError(this.buf)) {
                        throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
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
        }

        MostRecentSubscriber(T value2) {
            this.value = NotificationLite.next(value2);
        }

        public void onComplete() {
            this.value = NotificationLite.complete();
        }

        public void onError(Throwable e) {
            this.value = NotificationLite.error(e);
        }

        public void onNext(T args) {
            this.value = NotificationLite.next(args);
        }

        public Iterator getIterable() {
            return new Iterator<>();
        }
    }

    public BlockingFlowableMostRecent(Flowable<T> source2, T initialValue2) {
        this.source = source2;
        this.initialValue = initialValue2;
    }

    public Iterator<T> iterator() {
        MostRecentSubscriber<T> mostRecentSubscriber = new MostRecentSubscriber<>(this.initialValue);
        this.source.subscribe((FlowableSubscriber<? super T>) mostRecentSubscriber);
        return mostRecentSubscriber.getIterable();
    }
}
