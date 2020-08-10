package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther */
public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
    final ObservableSource<? extends T> main;
    final ObservableSource<U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther$DelayObserver */
    final class DelayObserver implements Observer<U> {
        final Observer<? super T> child;
        boolean done;
        final SequentialDisposable serial;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther$DelayObserver$OnComplete */
        final class OnComplete implements Observer<T> {
            OnComplete() {
            }

            public void onSubscribe(Disposable d) {
                DelayObserver.this.serial.update(d);
            }

            public void onNext(T value) {
                DelayObserver.this.child.onNext(value);
            }

            public void onError(Throwable e) {
                DelayObserver.this.child.onError(e);
            }

            public void onComplete() {
                DelayObserver.this.child.onComplete();
            }
        }

        DelayObserver(SequentialDisposable serial2, Observer<? super T> child2) {
            this.serial = serial2;
            this.child = child2;
        }

        public void onSubscribe(Disposable d) {
            this.serial.update(d);
        }

        public void onNext(U u) {
            onComplete();
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
                return;
            }
            this.done = true;
            this.child.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                ObservableDelaySubscriptionOther.this.main.subscribe(new OnComplete());
            }
        }
    }

    public ObservableDelaySubscriptionOther(ObservableSource<? extends T> main2, ObservableSource<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        SequentialDisposable serial = new SequentialDisposable();
        child.onSubscribe(serial);
        this.other.subscribe(new DelayObserver<>(serial, child));
    }
}
