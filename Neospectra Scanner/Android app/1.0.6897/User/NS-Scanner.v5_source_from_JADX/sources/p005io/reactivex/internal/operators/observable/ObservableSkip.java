package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkip */
public final class ObservableSkip<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: n */
    final long f350n;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkip$SkipObserver */
    static final class SkipObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f351d;
        long remaining;

        SkipObserver(Observer<? super T> actual2, long n) {
            this.actual = actual2;
            this.remaining = n;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f351d, d)) {
                this.f351d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.remaining != 0) {
                this.remaining--;
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f351d.dispose();
        }

        public boolean isDisposed() {
            return this.f351d.isDisposed();
        }
    }

    public ObservableSkip(ObservableSource<T> source, long n) {
        super(source);
        this.f350n = n;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new SkipObserver(s, this.f350n));
    }
}
