package p005io.reactivex.internal.operators.single;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import p005io.reactivex.Flowable;
import p005io.reactivex.Observable;
import p005io.reactivex.SingleSource;
import p005io.reactivex.functions.Function;

/* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper */
public final class SingleInternalHelper {

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$NoSuchElementCallable */
    enum NoSuchElementCallable implements Callable<NoSuchElementException> {
        INSTANCE;

        public NoSuchElementException call() throws Exception {
            return new NoSuchElementException();
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowable */
    enum ToFlowable implements Function<SingleSource, Publisher> {
        INSTANCE;

        public Publisher apply(SingleSource v) {
            return new SingleToFlowable(v);
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowableIterable */
    static final class ToFlowableIterable<T> implements Iterable<Flowable<T>> {
        private final Iterable<? extends SingleSource<? extends T>> sources;

        ToFlowableIterable(Iterable<? extends SingleSource<? extends T>> sources2) {
            this.sources = sources2;
        }

        public Iterator<Flowable<T>> iterator() {
            return new ToFlowableIterator(this.sources.iterator());
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowableIterator */
    static final class ToFlowableIterator<T> implements Iterator<Flowable<T>> {
        private final Iterator<? extends SingleSource<? extends T>> sit;

        ToFlowableIterator(Iterator<? extends SingleSource<? extends T>> sit2) {
            this.sit = sit2;
        }

        public boolean hasNext() {
            return this.sit.hasNext();
        }

        public Flowable<T> next() {
            return new SingleToFlowable((SingleSource) this.sit.next());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToObservable */
    enum ToObservable implements Function<SingleSource, Observable> {
        INSTANCE;

        public Observable apply(SingleSource v) {
            return new SingleToObservable(v);
        }
    }

    private SingleInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Callable<NoSuchElementException> emptyThrower() {
        return NoSuchElementCallable.INSTANCE;
    }

    public static <T> Function<SingleSource<? extends T>, Publisher<? extends T>> toFlowable() {
        return ToFlowable.INSTANCE;
    }

    public static <T> Iterable<? extends Flowable<T>> iterableToFlowable(Iterable<? extends SingleSource<? extends T>> sources) {
        return new ToFlowableIterable(sources);
    }

    public static <T> Function<SingleSource<? extends T>, Observable<? extends T>> toObservable() {
        return ToObservable.INSTANCE;
    }
}
