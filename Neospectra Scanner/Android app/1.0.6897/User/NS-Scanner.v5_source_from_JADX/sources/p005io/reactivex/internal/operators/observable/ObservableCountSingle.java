package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCountSingle */
public final class ObservableCountSingle<T> extends Single<Long> implements FuseToObservable<Long> {
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCountSingle$CountObserver */
    static final class CountObserver implements Observer<Object>, Disposable {
        final SingleObserver<? super Long> actual;
        long count;

        /* renamed from: d */
        Disposable f294d;

        CountObserver(SingleObserver<? super Long> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f294d, d)) {
                this.f294d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f294d.dispose();
            this.f294d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f294d.isDisposed();
        }

        public void onNext(Object t) {
            this.count++;
        }

        public void onError(Throwable t) {
            this.f294d = DisposableHelper.DISPOSED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f294d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Long.valueOf(this.count));
        }
    }

    public ObservableCountSingle(ObservableSource<T> source2) {
        this.source = source2;
    }

    public void subscribeActual(SingleObserver<? super Long> t) {
        this.source.subscribe(new CountObserver(t));
    }

    public Observable<Long> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableCount<T>(this.source));
    }
}
