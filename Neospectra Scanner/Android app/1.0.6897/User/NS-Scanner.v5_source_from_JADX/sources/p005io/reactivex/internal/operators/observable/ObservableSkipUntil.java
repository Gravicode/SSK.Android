package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil */
public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil$SkipUntil */
    final class SkipUntil implements Observer<U> {
        private final ArrayCompositeDisposable frc;

        /* renamed from: s */
        Disposable f354s;
        private final SerializedObserver<T> serial;
        private final SkipUntilObserver<T> sus;

        SkipUntil(ArrayCompositeDisposable frc2, SkipUntilObserver<T> sus2, SerializedObserver<T> serial2) {
            this.frc = frc2;
            this.sus = sus2;
            this.serial = serial2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f354s, s)) {
                this.f354s = s;
                this.frc.setResource(1, s);
            }
        }

        public void onNext(U u) {
            this.f354s.dispose();
            this.sus.notSkipping = true;
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.serial.onError(t);
        }

        public void onComplete() {
            this.sus.notSkipping = true;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil$SkipUntilObserver */
    static final class SkipUntilObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;

        /* renamed from: s */
        Disposable f355s;

        SkipUntilObserver(Observer<? super T> actual2, ArrayCompositeDisposable frc2) {
            this.actual = actual2;
            this.frc = frc2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f355s, s)) {
                this.f355s = s;
                this.frc.setResource(0, s);
            }
        }

        public void onNext(T t) {
            if (this.notSkippingLocal) {
                this.actual.onNext(t);
            } else if (this.notSkipping) {
                this.notSkippingLocal = true;
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }
    }

    public ObservableSkipUntil(ObservableSource<T> source, ObservableSource<U> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        SerializedObserver<T> serial = new SerializedObserver<>(child);
        ArrayCompositeDisposable frc = new ArrayCompositeDisposable(2);
        serial.onSubscribe(frc);
        SkipUntilObserver<T> sus = new SkipUntilObserver<>(serial, frc);
        this.other.subscribe(new SkipUntil(frc, sus, serial));
        this.source.subscribe(sus);
    }
}
