package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableHide */
public final class ObservableHide<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableHide$HideDisposable */
    static final class HideDisposable<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f318d;

        HideDisposable(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f318d.dispose();
        }

        public boolean isDisposed() {
            return this.f318d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f318d, d)) {
                this.f318d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableHide(ObservableSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> o) {
        this.source.subscribe(new HideDisposable(o));
    }
}
