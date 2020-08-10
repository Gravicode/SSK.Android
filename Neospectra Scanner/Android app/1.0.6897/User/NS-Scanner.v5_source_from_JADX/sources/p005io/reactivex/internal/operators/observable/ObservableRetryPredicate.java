package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRetryPredicate */
public final class ObservableRetryPredicate<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;
    final Predicate<? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryPredicate$RepeatObserver */
    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        final Predicate<? super Throwable> predicate;
        long remaining;

        /* renamed from: sa */
        final SequentialDisposable f338sa;
        final ObservableSource<? extends T> source;

        RepeatObserver(Observer<? super T> actual2, long count, Predicate<? super Throwable> predicate2, SequentialDisposable sa, ObservableSource<? extends T> source2) {
            this.actual = actual2;
            this.f338sa = sa;
            this.source = source2;
            this.predicate = predicate2;
            this.remaining = count;
        }

        public void onSubscribe(Disposable s) {
            this.f338sa.update(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            long r = this.remaining;
            if (r != Long.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r == 0) {
                this.actual.onError(t);
            } else {
                try {
                    if (!this.predicate.test(t)) {
                        this.actual.onError(t);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.actual.onError(new CompositeException(t, e));
                }
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f338sa.isDisposed()) {
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryPredicate(Observable<T> source, long count2, Predicate<? super Throwable> predicate2) {
        super(source);
        this.predicate = predicate2;
        this.count = count2;
    }

    public void subscribeActual(Observer<? super T> s) {
        SequentialDisposable sa = new SequentialDisposable();
        s.onSubscribe(sa);
        RepeatObserver<T> rs = new RepeatObserver<>(s, this.count, this.predicate, sa, this.source);
        rs.subscribeNext();
    }
}
