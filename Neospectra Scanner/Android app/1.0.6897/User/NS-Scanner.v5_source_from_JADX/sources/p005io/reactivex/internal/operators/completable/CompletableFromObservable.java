package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromObservable */
public final class CompletableFromObservable<T> extends Completable {
    final ObservableSource<T> observable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromObservable$CompletableFromObservableObserver */
    static final class CompletableFromObservableObserver<T> implements Observer<T> {

        /* renamed from: co */
        final CompletableObserver f105co;

        CompletableFromObservableObserver(CompletableObserver co) {
            this.f105co = co;
        }

        public void onSubscribe(Disposable d) {
            this.f105co.onSubscribe(d);
        }

        public void onNext(T t) {
        }

        public void onError(Throwable e) {
            this.f105co.onError(e);
        }

        public void onComplete() {
            this.f105co.onComplete();
        }
    }

    public CompletableFromObservable(ObservableSource<T> observable2) {
        this.observable = observable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.observable.subscribe(new CompletableFromObservableObserver(s));
    }
}
