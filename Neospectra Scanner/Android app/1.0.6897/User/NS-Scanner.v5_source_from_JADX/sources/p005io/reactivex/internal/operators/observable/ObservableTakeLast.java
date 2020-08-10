package p005io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLast */
public final class ObservableTakeLast<T> extends AbstractObservableWithUpstream<T, T> {
    final int count;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLast$TakeLastObserver */
    static final class TakeLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 7240042530241604978L;
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final int count;

        /* renamed from: s */
        Disposable f359s;

        TakeLastObserver(Observer<? super T> actual2, int count2) {
            this.actual = actual2;
            this.count = count2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f359s, s)) {
                this.f359s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.count == size()) {
                poll();
            }
            offer(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            Observer<? super T> a = this.actual;
            while (!this.cancelled) {
                T v = poll();
                if (v == null) {
                    if (!this.cancelled) {
                        a.onComplete();
                    }
                    return;
                }
                a.onNext(v);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f359s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }
    }

    public ObservableTakeLast(ObservableSource<T> source, int count2) {
        super(source);
        this.count = count2;
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new TakeLastObserver(t, this.count));
    }
}
