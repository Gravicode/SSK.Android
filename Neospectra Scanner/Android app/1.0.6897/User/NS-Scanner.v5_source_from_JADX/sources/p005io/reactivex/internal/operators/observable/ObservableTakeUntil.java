package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntil */
public final class ObservableTakeUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntil$TakeUntil */
    final class TakeUntil implements Observer<U> {
        private final ArrayCompositeDisposable frc;
        private final SerializedObserver<T> serial;

        TakeUntil(ArrayCompositeDisposable frc2, SerializedObserver<T> serial2) {
            this.frc = frc2;
            this.serial = serial2;
        }

        public void onSubscribe(Disposable s) {
            this.frc.setResource(1, s);
        }

        public void onNext(U u) {
            this.frc.dispose();
            this.serial.onComplete();
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.serial.onError(t);
        }

        public void onComplete() {
            this.frc.dispose();
            this.serial.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntil$TakeUntilObserver */
    static final class TakeUntilObserver<T> extends AtomicBoolean implements Observer<T> {
        private static final long serialVersionUID = 3451719290311127173L;
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;

        /* renamed from: s */
        Disposable f362s;

        TakeUntilObserver(Observer<? super T> actual2, ArrayCompositeDisposable frc2) {
            this.actual = actual2;
            this.frc = frc2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f362s, s)) {
                this.f362s = s;
                this.frc.setResource(0, s);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
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

    public ObservableTakeUntil(ObservableSource<T> source, ObservableSource<? extends U> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        SerializedObserver<T> serial = new SerializedObserver<>(child);
        ArrayCompositeDisposable frc = new ArrayCompositeDisposable(2);
        TakeUntilObserver<T> tus = new TakeUntilObserver<>(serial, frc);
        child.onSubscribe(frc);
        this.other.subscribe(new TakeUntil(frc, serial));
        this.source.subscribe(tus);
    }
}
