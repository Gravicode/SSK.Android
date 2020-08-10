package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat */
public final class ObservableRepeat<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat$RepeatObserver */
    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        long remaining;

        /* renamed from: sd */
        final SequentialDisposable f333sd;
        final ObservableSource<? extends T> source;

        RepeatObserver(Observer<? super T> actual2, long count, SequentialDisposable sd, ObservableSource<? extends T> source2) {
            this.actual = actual2;
            this.f333sd = sd;
            this.source = source2;
            this.remaining = count;
        }

        public void onSubscribe(Disposable s) {
            this.f333sd.replace(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            long r = this.remaining;
            if (r != Long.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.f333sd.isDisposed()) {
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                    }
                }
            }
        }
    }

    public ObservableRepeat(Observable<T> source, long count2) {
        super(source);
        this.count = count2;
    }

    public void subscribeActual(Observer<? super T> s) {
        SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        long j = Long.MAX_VALUE;
        if (this.count != Long.MAX_VALUE) {
            j = this.count - 1;
        }
        RepeatObserver<T> rs = new RepeatObserver<>(s, j, sd, this.source);
        rs.subscribeNext();
    }
}
