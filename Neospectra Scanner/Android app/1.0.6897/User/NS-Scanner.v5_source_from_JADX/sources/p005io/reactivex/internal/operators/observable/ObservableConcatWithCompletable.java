package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithCompletable */
public final class ObservableConcatWithCompletable<T> extends AbstractObservableWithUpstream<T, T> {
    final CompletableSource other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithCompletable$ConcatWithObserver */
    static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, CompletableObserver, Disposable {
        private static final long serialVersionUID = -1953724749712440952L;
        final Observer<? super T> actual;
        boolean inCompletable;
        CompletableSource other;

        ConcatWithObserver(Observer<? super T> actual2, CompletableSource other2) {
            this.actual = actual2;
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d) && !this.inCompletable) {
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            if (this.inCompletable) {
                this.actual.onComplete();
                return;
            }
            this.inCompletable = true;
            DisposableHelper.replace(this, null);
            CompletableSource cs = this.other;
            this.other = null;
            cs.subscribe(this);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public ObservableConcatWithCompletable(Observable<T> source, CompletableSource other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ConcatWithObserver(observer, this.other));
    }
}
