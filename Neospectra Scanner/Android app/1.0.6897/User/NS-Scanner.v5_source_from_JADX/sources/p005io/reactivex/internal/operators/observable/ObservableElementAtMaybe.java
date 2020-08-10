package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtMaybe */
public final class ObservableElementAtMaybe<T> extends Maybe<T> implements FuseToObservable<T> {
    final long index;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableElementAtMaybe$ElementAtObserver */
    static final class ElementAtObserver<T> implements Observer<T>, Disposable {
        final MaybeObserver<? super T> actual;
        long count;
        boolean done;
        final long index;

        /* renamed from: s */
        Disposable f306s;

        ElementAtObserver(MaybeObserver<? super T> actual2, long index2) {
            this.actual = actual2;
            this.index = index2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f306s, s)) {
                this.f306s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f306s.dispose();
        }

        public boolean isDisposed() {
            return this.f306s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.f306s.dispose();
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
                this.actual.onComplete();
            }
        }
    }

    public ObservableElementAtMaybe(ObservableSource<T> source2, long index2) {
        this.source = source2;
        this.index = index2;
    }

    public void subscribeActual(MaybeObserver<? super T> t) {
        this.source.subscribe(new ElementAtObserver(t, this.index));
    }

    public Observable<T> fuseToObservable() {
        ObservableElementAt observableElementAt = new ObservableElementAt(this.source, this.index, null, false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableElementAt);
    }
}
