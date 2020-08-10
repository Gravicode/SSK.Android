package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTimeout */
public final class ObservableTimeout<T, U, V> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> firstTimeoutIndicator;
    final Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator;
    final ObservableSource<? extends T> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeout$TimeoutConsumer */
    static final class TimeoutConsumer extends AtomicReference<Disposable> implements Observer<Object>, Disposable {
        private static final long serialVersionUID = 8708641127342403073L;
        final long idx;
        final TimeoutSelectorSupport parent;

        TimeoutConsumer(long idx2, TimeoutSelectorSupport parent2) {
            this.idx = idx2;
            this.parent = parent2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this, s);
        }

        public void onNext(Object t) {
            Disposable upstream = (Disposable) get();
            if (upstream != DisposableHelper.DISPOSED) {
                upstream.dispose();
                lazySet(DisposableHelper.DISPOSED);
                this.parent.onTimeout(this.idx);
            }
        }

        public void onError(Throwable t) {
            if (get() != DisposableHelper.DISPOSED) {
                lazySet(DisposableHelper.DISPOSED);
                this.parent.onTimeoutError(this.idx, t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (get() != DisposableHelper.DISPOSED) {
                lazySet(DisposableHelper.DISPOSED);
                this.parent.onTimeout(this.idx);
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeout$TimeoutFallbackObserver */
    static final class TimeoutFallbackObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, TimeoutSelectorSupport {
        private static final long serialVersionUID = -7508389464265974549L;
        final Observer<? super T> actual;
        ObservableSource<? extends T> fallback;
        final AtomicLong index;
        final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
        final SequentialDisposable task = new SequentialDisposable();
        final AtomicReference<Disposable> upstream;

        TimeoutFallbackObserver(Observer<? super T> actual2, Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator2, ObservableSource<? extends T> fallback2) {
            this.actual = actual2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
            this.fallback = fallback2;
            this.index = new AtomicLong();
            this.upstream = new AtomicReference<>();
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this.upstream, s);
        }

        public void onNext(T t) {
            long idx = this.index.get();
            if (idx != Long.MAX_VALUE && this.index.compareAndSet(idx, idx + 1)) {
                Disposable d = (Disposable) this.task.get();
                if (d != null) {
                    d.dispose();
                }
                this.actual.onNext(t);
                try {
                    ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The itemTimeoutIndicator returned a null ObservableSource.");
                    TimeoutConsumer consumer = new TimeoutConsumer(1 + idx, this);
                    if (this.task.replace(consumer)) {
                        observableSource.subscribe(consumer);
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    ((Disposable) this.upstream.get()).dispose();
                    this.index.getAndSet(Long.MAX_VALUE);
                    this.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startFirstTimeout(ObservableSource<?> firstTimeoutIndicator) {
            if (firstTimeoutIndicator != null) {
                TimeoutConsumer consumer = new TimeoutConsumer(0, this);
                if (this.task.replace(consumer)) {
                    firstTimeoutIndicator.subscribe(consumer);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.task.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.task.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this.upstream);
                ObservableSource<? extends T> f = this.fallback;
                this.fallback = null;
                f.subscribe(new FallbackObserver(this.actual, this));
            }
        }

        public void onTimeoutError(long idx, Throwable ex) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this);
                this.actual.onError(ex);
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            DisposableHelper.dispose(this);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeout$TimeoutObserver */
    static final class TimeoutObserver<T> extends AtomicLong implements Observer<T>, Disposable, TimeoutSelectorSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Observer<? super T> actual;
        final Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator;
        final SequentialDisposable task = new SequentialDisposable();
        final AtomicReference<Disposable> upstream = new AtomicReference<>();

        TimeoutObserver(Observer<? super T> actual2, Function<? super T, ? extends ObservableSource<?>> itemTimeoutIndicator2) {
            this.actual = actual2;
            this.itemTimeoutIndicator = itemTimeoutIndicator2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this.upstream, s);
        }

        public void onNext(T t) {
            long idx = get();
            if (idx != Long.MAX_VALUE && compareAndSet(idx, idx + 1)) {
                Disposable d = (Disposable) this.task.get();
                if (d != null) {
                    d.dispose();
                }
                this.actual.onNext(t);
                try {
                    ObservableSource observableSource = (ObservableSource) ObjectHelper.requireNonNull(this.itemTimeoutIndicator.apply(t), "The itemTimeoutIndicator returned a null ObservableSource.");
                    TimeoutConsumer consumer = new TimeoutConsumer(1 + idx, this);
                    if (this.task.replace(consumer)) {
                        observableSource.subscribe(consumer);
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    ((Disposable) this.upstream.get()).dispose();
                    getAndSet(Long.MAX_VALUE);
                    this.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void startFirstTimeout(ObservableSource<?> firstTimeoutIndicator) {
            if (firstTimeoutIndicator != null) {
                TimeoutConsumer consumer = new TimeoutConsumer(0, this);
                if (this.task.replace(consumer)) {
                    firstTimeoutIndicator.subscribe(consumer);
                }
            }
        }

        public void onError(Throwable t) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
            }
        }

        public void onTimeout(long idx) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this.upstream);
                this.actual.onError(new TimeoutException());
            }
        }

        public void onTimeoutError(long idx, Throwable ex) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                DisposableHelper.dispose(this.upstream);
                this.actual.onError(ex);
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.upstream.get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeout$TimeoutSelectorSupport */
    interface TimeoutSelectorSupport extends TimeoutSupport {
        void onTimeoutError(long j, Throwable th);
    }

    public ObservableTimeout(Observable<T> source, ObservableSource<U> firstTimeoutIndicator2, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator2, ObservableSource<? extends T> other2) {
        super(source);
        this.firstTimeoutIndicator = firstTimeoutIndicator2;
        this.itemTimeoutIndicator = itemTimeoutIndicator2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        if (this.other == null) {
            TimeoutObserver<T> parent = new TimeoutObserver<>(s, this.itemTimeoutIndicator);
            s.onSubscribe(parent);
            parent.startFirstTimeout(this.firstTimeoutIndicator);
            this.source.subscribe(parent);
            return;
        }
        TimeoutFallbackObserver<T> parent2 = new TimeoutFallbackObserver<>(s, this.itemTimeoutIndicator, this.other);
        s.onSubscribe(parent2);
        parent2.startFirstTimeout(this.firstTimeoutIndicator);
        this.source.subscribe(parent2);
    }
}
