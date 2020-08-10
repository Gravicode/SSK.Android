package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLastOne */
public final class ObservableTakeLastOne<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLastOne$TakeLastOneObserver */
    static final class TakeLastOneObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: s */
        Disposable f360s;
        T value;

        TakeLastOneObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f360s, s)) {
                this.f360s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onError(Throwable t) {
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            emit();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            T v = this.value;
            if (v != null) {
                this.value = null;
                this.actual.onNext(v);
            }
            this.actual.onComplete();
        }

        public void dispose() {
            this.value = null;
            this.f360s.dispose();
        }

        public boolean isDisposed() {
            return this.f360s.isDisposed();
        }
    }

    public ObservableTakeLastOne(ObservableSource<T> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new TakeLastOneObserver(s));
    }
}
