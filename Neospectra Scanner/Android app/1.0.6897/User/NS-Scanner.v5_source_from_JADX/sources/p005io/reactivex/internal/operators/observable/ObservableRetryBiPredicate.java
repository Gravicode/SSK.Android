package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRetryBiPredicate */
public final class ObservableRetryBiPredicate<T> extends AbstractObservableWithUpstream<T, T> {
    final BiPredicate<? super Integer, ? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryBiPredicate$RetryBiObserver */
    static final class RetryBiObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        final BiPredicate<? super Integer, ? super Throwable> predicate;
        int retries;

        /* renamed from: sa */
        final SequentialDisposable f337sa;
        final ObservableSource<? extends T> source;

        RetryBiObserver(Observer<? super T> actual2, BiPredicate<? super Integer, ? super Throwable> predicate2, SequentialDisposable sa, ObservableSource<? extends T> source2) {
            this.actual = actual2;
            this.f337sa = sa;
            this.source = source2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            this.f337sa.update(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            try {
                BiPredicate<? super Integer, ? super Throwable> biPredicate = this.predicate;
                int i = this.retries + 1;
                this.retries = i;
                if (!biPredicate.test(Integer.valueOf(i), t)) {
                    this.actual.onError(t);
                } else {
                    subscribeNext();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f337sa.isDisposed()) {
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryBiPredicate(Observable<T> source, BiPredicate<? super Integer, ? super Throwable> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> s) {
        SequentialDisposable sa = new SequentialDisposable();
        s.onSubscribe(sa);
        new RetryBiObserver<>(s, this.predicate, sa, this.source).subscribeNext();
    }
}
