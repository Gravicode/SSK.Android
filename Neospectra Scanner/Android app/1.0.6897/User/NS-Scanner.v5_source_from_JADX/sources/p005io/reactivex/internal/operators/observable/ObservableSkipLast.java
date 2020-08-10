package p005io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLast */
public final class ObservableSkipLast<T> extends AbstractObservableWithUpstream<T, T> {
    final int skip;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLast$SkipLastObserver */
    static final class SkipLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -3807491841935125653L;
        final Observer<? super T> actual;

        /* renamed from: s */
        Disposable f352s;
        final int skip;

        SkipLastObserver(Observer<? super T> actual2, int skip2) {
            super(skip2);
            this.actual = actual2;
            this.skip = skip2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f352s, s)) {
                this.f352s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f352s.dispose();
        }

        public boolean isDisposed() {
            return this.f352s.isDisposed();
        }

        public void onNext(T t) {
            if (this.skip == size()) {
                this.actual.onNext(poll());
            }
            offer(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableSkipLast(ObservableSource<T> source, int skip2) {
        super(source);
        this.skip = skip2;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new SkipLastObserver(s, this.skip));
    }
}
