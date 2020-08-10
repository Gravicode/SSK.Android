package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Notification;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableMaterialize */
public final class ObservableMaterialize<T> extends AbstractObservableWithUpstream<T, Notification<T>> {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableMaterialize$MaterializeObserver */
    static final class MaterializeObserver<T> implements Observer<T>, Disposable {
        final Observer<? super Notification<T>> actual;

        /* renamed from: s */
        Disposable f325s;

        MaterializeObserver(Observer<? super Notification<T>> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f325s, s)) {
                this.f325s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f325s.dispose();
        }

        public boolean isDisposed() {
            return this.f325s.isDisposed();
        }

        public void onNext(T t) {
            this.actual.onNext(Notification.createOnNext(t));
        }

        public void onError(Throwable t) {
            this.actual.onNext(Notification.createOnError(t));
            this.actual.onComplete();
        }

        public void onComplete() {
            this.actual.onNext(Notification.createOnComplete());
            this.actual.onComplete();
        }
    }

    public ObservableMaterialize(ObservableSource<T> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super Notification<T>> t) {
        this.source.subscribe(new MaterializeObserver(t));
    }
}
