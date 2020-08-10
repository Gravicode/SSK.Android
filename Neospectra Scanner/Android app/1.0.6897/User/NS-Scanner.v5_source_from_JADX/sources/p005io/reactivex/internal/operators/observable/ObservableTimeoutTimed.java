package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed */
public final class ObservableTimeoutTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$FallbackObserver */
    static final class FallbackObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final AtomicReference<Disposable> arbiter;

        FallbackObserver(Observer<? super T> actual2, AtomicReference<Disposable> arbiter2) {
            this.actual = actual2;
            this.arbiter = arbiter2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.replace(this.arbiter, s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutFallbackObserver */
    static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Observer<? super T> actual;
        ObservableSource<? extends T> fallback;
        final AtomicLong index = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Disposable> upstream = new AtomicReference<>();
        final Worker worker;

        TimeoutFallbackObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2, ObservableSource<? extends T> fallback2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.fallback = fallback2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this.upstream, s);
        }

        public void onNext(T t) {
            long idx = this.index.get();
            if (idx != Long.MAX_VALUE && this.index.compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.actual.onNext(t);
                startTimeout(1 + idx);
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this.upstream);
                ObservableSource<? extends T> f = this.fallback;
                this.fallback = null;
                f.subscribe(new FallbackObserver(this.actual, this));
                this.worker.dispose();
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            DisposableHelper.dispose(this);
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutObserver */
    static final class TimeoutObserver<T> extends AtomicLong implements Observer<T>, Disposable, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Observer<? super T> actual;
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Disposable> upstream = new AtomicReference<>();
        final Worker worker;

        TimeoutObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this.upstream, s);
        }

        public void onNext(T t) {
            long idx = get();
            if (idx != Long.MAX_VALUE && compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.actual.onNext(t);
                startTimeout(1 + idx);
            }
        }

        /* access modifiers changed from: 0000 */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this.upstream);
                this.actual.onError(new TimeoutException());
                this.worker.dispose();
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.upstream.get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutSupport */
    interface TimeoutSupport {
        void onTimeout(long j);
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutTask */
    static final class TimeoutTask implements Runnable {
        final long idx;
        final TimeoutSupport parent;

        TimeoutTask(long idx2, TimeoutSupport parent2) {
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            this.parent.onTimeout(this.idx);
        }
    }

    public ObservableTimeoutTimed(Observable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, ObservableSource<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        if (this.other == null) {
            TimeoutObserver timeoutObserver = new TimeoutObserver(s, this.timeout, this.unit, this.scheduler.createWorker());
            s.onSubscribe(timeoutObserver);
            timeoutObserver.startTimeout(0);
            this.source.subscribe(timeoutObserver);
            return;
        }
        TimeoutFallbackObserver timeoutFallbackObserver = new TimeoutFallbackObserver(s, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
        s.onSubscribe(timeoutFallbackObserver);
        timeoutFallbackObserver.startTimeout(0);
        this.source.subscribe(timeoutFallbackObserver);
    }
}
