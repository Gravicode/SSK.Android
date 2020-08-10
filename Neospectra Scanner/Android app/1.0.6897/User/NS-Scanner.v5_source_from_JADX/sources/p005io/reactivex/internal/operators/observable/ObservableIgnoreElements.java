package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElements */
public final class ObservableIgnoreElements<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElements$IgnoreObservable */
    static final class IgnoreObservable<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f319d;

        IgnoreObservable(Observer<? super T> t) {
            this.actual = t;
        }

        public void onSubscribe(Disposable s) {
            this.f319d = s;
            this.actual.onSubscribe(this);
        }

        public void onNext(T t) {
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f319d.dispose();
        }

        public boolean isDisposed() {
            return this.f319d.isDisposed();
        }
    }

    public ObservableIgnoreElements(ObservableSource<T> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new IgnoreObservable(t));
    }
}
