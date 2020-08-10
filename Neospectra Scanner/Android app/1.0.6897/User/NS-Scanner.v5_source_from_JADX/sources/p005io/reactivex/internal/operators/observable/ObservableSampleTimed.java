package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed */
public final class ObservableSampleTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final boolean emitLast;
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedEmitLast */
    static final class SampleTimedEmitLast<T> extends SampleTimedObserver<T> {
        private static final long serialVersionUID = -7139995637533111443L;
        final AtomicInteger wip = new AtomicInteger(1);

        SampleTimedEmitLast(Observer<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
            super(actual, period, unit, scheduler);
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            emit();
            if (this.wip.decrementAndGet() == 0) {
                this.actual.onComplete();
            }
        }

        public void run() {
            if (this.wip.incrementAndGet() == 2) {
                emit();
                if (this.wip.decrementAndGet() == 0) {
                    this.actual.onComplete();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedNoLast */
    static final class SampleTimedNoLast<T> extends SampleTimedObserver<T> {
        private static final long serialVersionUID = -7139995637533111443L;

        SampleTimedNoLast(Observer<? super T> actual, long period, TimeUnit unit, Scheduler scheduler) {
            super(actual, period, unit, scheduler);
        }

        /* access modifiers changed from: 0000 */
        public void complete() {
            this.actual.onComplete();
        }

        public void run() {
            emit();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedObserver */
    static abstract class SampleTimedObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer<? super T> actual;
        final long period;

        /* renamed from: s */
        Disposable f340s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;

        /* access modifiers changed from: 0000 */
        public abstract void complete();

        SampleTimedObserver(Observer<? super T> actual2, long period2, TimeUnit unit2, Scheduler scheduler2) {
            this.actual = actual2;
            this.period = period2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f340s, s)) {
                this.f340s = s;
                this.actual.onSubscribe(this);
                DisposableHelper.replace(this.timer, this.scheduler.schedulePeriodicallyDirect(this, this.period, this.period, this.unit));
            }
        }

        public void onNext(T t) {
            lazySet(t);
        }

        public void onError(Throwable t) {
            cancelTimer();
            this.actual.onError(t);
        }

        public void onComplete() {
            cancelTimer();
            complete();
        }

        /* access modifiers changed from: 0000 */
        public void cancelTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public void dispose() {
            cancelTimer();
            this.f340s.dispose();
        }

        public boolean isDisposed() {
            return this.f340s.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            T value = getAndSet(null);
            if (value != null) {
                this.actual.onNext(value);
            }
        }
    }

    public ObservableSampleTimed(ObservableSource<T> source, long period2, TimeUnit unit2, Scheduler scheduler2, boolean emitLast2) {
        super(source);
        this.period = period2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.emitLast = emitLast2;
    }

    public void subscribeActual(Observer<? super T> t) {
        SerializedObserver<T> serial = new SerializedObserver<>(t);
        if (this.emitLast) {
            ObservableSource observableSource = this.source;
            SampleTimedEmitLast sampleTimedEmitLast = new SampleTimedEmitLast(serial, this.period, this.unit, this.scheduler);
            observableSource.subscribe(sampleTimedEmitLast);
            return;
        }
        ObservableSource observableSource2 = this.source;
        SampleTimedNoLast sampleTimedNoLast = new SampleTimedNoLast(serial, this.period, this.unit, this.scheduler);
        observableSource2.subscribe(sampleTimedNoLast);
    }
}
