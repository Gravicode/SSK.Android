package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.observers.DeferredScalarDisposable;

/* renamed from: io.reactivex.internal.operators.single.SingleToObservable */
public final class SingleToObservable<T> extends Observable<T> {
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleToObservable$SingleToObservableObserver */
    static final class SingleToObservableObserver<T> extends DeferredScalarDisposable<T> implements SingleObserver<T> {
        private static final long serialVersionUID = 3786543492451018833L;

        /* renamed from: d */
        Disposable f422d;

        SingleToObservableObserver(Observer<? super T> actual) {
            super(actual);
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f422d, d)) {
                this.f422d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            complete(value);
        }

        public void onError(Throwable e) {
            error(e);
        }

        public void dispose() {
            super.dispose();
            this.f422d.dispose();
        }
    }

    public SingleToObservable(SingleSource<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(create(s));
    }

    @Experimental
    public static <T> SingleObserver<T> create(Observer<? super T> downstream) {
        return new SingleToObservableObserver(downstream);
    }
}
