package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable */
public final class ObservableIgnoreElementsCompletable<T> extends Completable implements FuseToObservable<T> {
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable$IgnoreObservable */
    static final class IgnoreObservable<T> implements Observer<T>, Disposable {
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f320d;

        IgnoreObservable(CompletableObserver t) {
            this.actual = t;
        }

        public void onSubscribe(Disposable s) {
            this.f320d = s;
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
            this.f320d.dispose();
        }

        public boolean isDisposed() {
            return this.f320d.isDisposed();
        }
    }

    public ObservableIgnoreElementsCompletable(ObservableSource<T> source2) {
        this.source = source2;
    }

    public void subscribeActual(CompletableObserver t) {
        this.source.subscribe(new IgnoreObservable(t));
    }

    public Observable<T> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableIgnoreElements<T>(this.source));
    }
}
