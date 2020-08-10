package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Notification;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDematerialize */
public final class ObservableDematerialize<T> extends AbstractObservableWithUpstream<Notification<T>, T> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDematerialize$DematerializeObserver */
    static final class DematerializeObserver<T> implements Observer<Notification<T>>, Disposable {
        final Observer<? super T> actual;
        boolean done;

        /* renamed from: s */
        Disposable f300s;

        DematerializeObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f300s, s)) {
                this.f300s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f300s.dispose();
        }

        public boolean isDisposed() {
            return this.f300s.isDisposed();
        }

        public void onNext(Notification<T> t) {
            if (this.done) {
                if (t.isOnError()) {
                    RxJavaPlugins.onError(t.getError());
                }
                return;
            }
            if (t.isOnError()) {
                this.f300s.dispose();
                onError(t.getError());
            } else if (t.isOnComplete()) {
                this.f300s.dispose();
                onComplete();
            } else {
                this.actual.onNext(t.getValue());
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

    public ObservableDematerialize(ObservableSource<Notification<T>> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new DematerializeObserver(t));
    }
}
