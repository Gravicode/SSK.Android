package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithMaybe */
public final class ObservableConcatWithMaybe<T> extends AbstractObservableWithUpstream<T, T> {
    final MaybeSource<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableConcatWithMaybe$ConcatWithObserver */
    static final class ConcatWithObserver<T> extends AtomicReference<Disposable> implements Observer<T>, MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = -1953724749712440952L;
        final Observer<? super T> actual;
        boolean inMaybe;
        MaybeSource<? extends T> other;

        ConcatWithObserver(Observer<? super T> actual2, MaybeSource<? extends T> other2) {
            this.actual = actual2;
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d) && !this.inMaybe) {
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
            if (this.inMaybe) {
                this.actual.onComplete();
                return;
            }
            this.inMaybe = true;
            DisposableHelper.replace(this, null);
            MaybeSource<? extends T> ms = this.other;
            this.other = null;
            ms.subscribe(this);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public ObservableConcatWithMaybe(Observable<T> source, MaybeSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ConcatWithObserver(observer, this.other));
    }
}
