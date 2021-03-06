package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.observers.SerializedObserver;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed */
public final class ObservableDebounceTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed$DebounceEmitter */
    static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
        private static final long serialVersionUID = 6812032969491025141L;
        final long idx;
        final AtomicBoolean once = new AtomicBoolean();
        final DebounceTimedObserver<T> parent;
        final T value;

        DebounceEmitter(T value2, long idx2, DebounceTimedObserver<T> parent2) {
            this.value = value2;
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.emit(this.idx, this.value, this);
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void setResource(Disposable d) {
            DisposableHelper.replace(this, d);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed$DebounceTimedObserver */
    static final class DebounceTimedObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean done;
        volatile long index;

        /* renamed from: s */
        Disposable f296s;
        final long timeout;
        Disposable timer;
        final TimeUnit unit;
        final Worker worker;

        DebounceTimedObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f296s, s)) {
                this.f296s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                Disposable d = this.timer;
                if (d != null) {
                    d.dispose();
                }
                DebounceEmitter<T> de = new DebounceEmitter<>(t, idx, this);
                this.timer = de;
                de.setResource(this.worker.schedule(de, this.timeout, this.unit));
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            Disposable d = this.timer;
            if (d != null) {
                d.dispose();
            }
            this.done = true;
            this.actual.onError(t);
            this.worker.dispose();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable d = this.timer;
                if (d != null) {
                    d.dispose();
                }
                DebounceEmitter<T> de = (DebounceEmitter) d;
                if (de != null) {
                    de.run();
                }
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void dispose() {
            this.f296s.dispose();
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void emit(long idx, T t, DebounceEmitter<T> emitter) {
            if (idx == this.index) {
                this.actual.onNext(t);
                emitter.dispose();
            }
        }
    }

    public ObservableDebounceTimed(ObservableSource<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> t) {
        ObservableSource observableSource = this.source;
        DebounceTimedObserver debounceTimedObserver = new DebounceTimedObserver(new SerializedObserver(t), this.timeout, this.unit, this.scheduler.createWorker());
        observableSource.subscribe(debounceTimedObserver);
    }
}
