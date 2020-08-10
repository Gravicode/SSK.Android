package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithSingle */
public final class ObservableConcatWithSingle<T> extends AbstractObservableWithUpstream<T, T> {
    final SingleSource<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithSingle$ConcatWithObserver */
    static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -1953724749712440952L;
        final Observer<? super T> actual;
        boolean inSingle;
        SingleSource<? extends T> other;

        ConcatWithObserver(Observer<? super T> actual2, SingleSource<? extends T> other2) {
            this.actual = actual2;
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d) && !this.inSingle) {
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onSuccess(T t) {
            this.actual.onNext(t);
            this.actual.onComplete();
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.inSingle = true;
            DisposableHelper.replace(this, null);
            SingleSource<? extends T> ss = this.other;
            this.other = null;
            ss.subscribe(this);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public ObservableConcatWithSingle(Observable<T> source, SingleSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ConcatWithObserver(observer, this.other));
    }
}
