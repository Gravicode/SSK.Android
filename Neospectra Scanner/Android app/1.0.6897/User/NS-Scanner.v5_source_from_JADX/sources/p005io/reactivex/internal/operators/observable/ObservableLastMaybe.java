package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableLastMaybe */
public final class ObservableLastMaybe<T> extends Maybe<T> {
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableLastMaybe$LastObserver */
    static final class LastObserver<T> implements Observer<T>, Disposable {
        final MaybeObserver<? super T> actual;
        T item;

        /* renamed from: s */
        Disposable f322s;

        LastObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f322s.dispose();
            this.f322s = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f322s == DisposableHelper.DISPOSED;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f322s, s)) {
                this.f322s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.item = t;
        }

        public void onError(Throwable t) {
            this.f322s = DisposableHelper.DISPOSED;
            this.item = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.f322s = DisposableHelper.DISPOSED;
            T v = this.item;
            if (v != null) {
                this.item = null;
                this.actual.onSuccess(v);
                return;
            }
            this.actual.onComplete();
        }
    }

    public ObservableLastMaybe(ObservableSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new LastObserver(observer));
    }
}
