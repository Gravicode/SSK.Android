package p005io.reactivex.internal.operators.observable;

import java.util.NoSuchElementException;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtSingle */
public final class ObservableElementAtSingle<T> extends Single<T> implements FuseToObservable<T> {
    final T defaultValue;
    final long index;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtSingle$ElementAtObserver */
    static final class ElementAtObserver<T> implements Observer<T>, Disposable {
        final SingleObserver<? super T> actual;
        long count;
        final T defaultValue;
        boolean done;
        final long index;

        /* renamed from: s */
        Disposable f307s;

        ElementAtObserver(SingleObserver<? super T> actual2, long index2, T defaultValue2) {
            this.actual = actual2;
            this.index = index2;
            this.defaultValue = defaultValue2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f307s, s)) {
                this.f307s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f307s.dispose();
        }

        public boolean isDisposed() {
            return this.f307s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.f307s.dispose();
                    this.actual.onSuccess(t);
                    return;
                }
                this.count = 1 + c;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T v = this.defaultValue;
                if (v != null) {
                    this.actual.onSuccess(v);
                } else {
                    this.actual.onError(new NoSuchElementException());
                }
            }
        }
    }

    public ObservableElementAtSingle(ObservableSource<T> source2, long index2, T defaultValue2) {
        this.source = source2;
        this.index = index2;
        this.defaultValue = defaultValue2;
    }

    public void subscribeActual(SingleObserver<? super T> t) {
        this.source.subscribe(new ElementAtObserver(t, this.index, this.defaultValue));
    }

    public Observable<T> fuseToObservable() {
        ObservableElementAt observableElementAt = new ObservableElementAt(this.source, this.index, this.defaultValue, true);
        return RxJavaPlugins.onAssembly((Observable<T>) observableElementAt);
    }
}
