package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTimeInterval */
public final class ObservableTimeInterval<T> extends AbstractObservableWithUpstream<T, Timed<T>> {
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeInterval$TimeIntervalObserver */
    static final class TimeIntervalObserver<T> implements Observer<T>, Disposable {
        final Observer<? super Timed<T>> actual;
        long lastTime;

        /* renamed from: s */
        Disposable f366s;
        final Scheduler scheduler;
        final TimeUnit unit;

        TimeIntervalObserver(Observer<? super Timed<T>> actual2, TimeUnit unit2, Scheduler scheduler2) {
            this.actual = actual2;
            this.scheduler = scheduler2;
            this.unit = unit2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f366s, s)) {
                this.f366s = s;
                this.lastTime = this.scheduler.now(this.unit);
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f366s.dispose();
        }

        public boolean isDisposed() {
            return this.f366s.isDisposed();
        }

        public void onNext(T t) {
            long now = this.scheduler.now(this.unit);
            long last = this.lastTime;
            this.lastTime = now;
            this.actual.onNext(new Timed(t, now - last, this.unit));
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableTimeInterval(ObservableSource<T> source, TimeUnit unit2, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
        this.unit = unit2;
    }

    public void subscribeActual(Observer<? super Timed<T>> t) {
        this.source.subscribe(new TimeIntervalObserver(t, this.unit, this.scheduler));
    }
}
