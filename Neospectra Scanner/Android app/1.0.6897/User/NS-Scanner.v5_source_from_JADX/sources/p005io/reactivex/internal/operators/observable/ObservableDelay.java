package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDelay */
public final class ObservableDelay<T> extends AbstractObservableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDelay$DelayObserver */
    static final class DelayObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        final long delay;
        final boolean delayError;

        /* renamed from: s */
        Disposable f297s;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f298w;

        /* renamed from: io.reactivex.internal.operators.observable.ObservableDelay$DelayObserver$OnComplete */
        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onComplete();
                } finally {
                    DelayObserver.this.f298w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.observable.ObservableDelay$DelayObserver$OnError */
        final class OnError implements Runnable {
            private final Throwable throwable;

            OnError(Throwable throwable2) {
                this.throwable = throwable2;
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onError(this.throwable);
                } finally {
                    DelayObserver.this.f298w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.observable.ObservableDelay$DelayObserver$OnNext */
        final class OnNext implements Runnable {

            /* renamed from: t */
            private final T f299t;

            OnNext(T t) {
                this.f299t = t;
            }

            public void run() {
                DelayObserver.this.actual.onNext(this.f299t);
            }
        }

        DelayObserver(Observer<? super T> actual2, long delay2, TimeUnit unit2, Worker w, boolean delayError2) {
            this.actual = actual2;
            this.delay = delay2;
            this.unit = unit2;
            this.f298w = w;
            this.delayError = delayError2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f297s, s)) {
                this.f297s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.f298w.schedule(new OnNext(t), this.delay, this.unit);
        }

        public void onError(Throwable t) {
            this.f298w.schedule(new OnError(t), this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.f298w.schedule(new OnComplete(), this.delay, this.unit);
        }

        public void dispose() {
            this.f297s.dispose();
            this.f298w.dispose();
        }

        public boolean isDisposed() {
            return this.f298w.isDisposed();
        }
    }

    public ObservableDelay(ObservableSource<T> source, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        super(source);
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super T> t) {
        Observer<? super T> serializedObserver;
        if (this.delayError) {
            serializedObserver = t;
        } else {
            serializedObserver = new SerializedObserver<>(t);
        }
        Observer<? super T> observer = serializedObserver;
        Worker w = this.scheduler.createWorker();
        ObservableSource observableSource = this.source;
        DelayObserver delayObserver = new DelayObserver(observer, this.delay, this.unit, w, this.delayError);
        observableSource.subscribe(delayObserver);
    }
}
