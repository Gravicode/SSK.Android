package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSingleMaybe */
public final class ObservableSingleMaybe<T> extends Maybe<T> {
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSingleMaybe$SingleElementObserver */
    static final class SingleElementObserver<T> implements Observer<T>, Disposable {
        final MaybeObserver<? super T> actual;
        boolean done;

        /* renamed from: s */
        Disposable f348s;
        T value;

        SingleElementObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f348s, s)) {
                this.f348s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f348s.dispose();
        }

        public boolean isDisposed() {
            return this.f348s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f348s.dispose();
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
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
                T v = this.value;
                this.value = null;
                if (v == null) {
                    this.actual.onComplete();
                } else {
                    this.actual.onSuccess(v);
                }
            }
        }
    }

    public ObservableSingleMaybe(ObservableSource<T> source2) {
        this.source = source2;
    }

    public void subscribeActual(MaybeObserver<? super T> t) {
        this.source.subscribe(new SingleElementObserver(t));
    }
}
