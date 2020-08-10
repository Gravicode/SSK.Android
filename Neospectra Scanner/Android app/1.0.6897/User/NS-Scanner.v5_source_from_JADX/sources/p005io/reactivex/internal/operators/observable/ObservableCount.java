package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCount */
public final class ObservableCount<T> extends AbstractObservableWithUpstream<T, Long> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCount$CountObserver */
    static final class CountObserver implements Observer<Object>, Disposable {
        final Observer<? super Long> actual;
        long count;

        /* renamed from: s */
        Disposable f293s;

        CountObserver(Observer<? super Long> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f293s, s)) {
                this.f293s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f293s.dispose();
        }

        public boolean isDisposed() {
            return this.f293s.isDisposed();
        }

        public void onNext(Object t) {
            this.count++;
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onNext(Long.valueOf(this.count));
            this.actual.onComplete();
        }
    }

    public ObservableCount(ObservableSource<T> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super Long> t) {
        this.source.subscribe(new CountObserver(t));
    }
}
