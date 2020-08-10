package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable */
public final class ObservableFlatMapCompletableCompletable<T> extends Completable implements FuseToObservable<T> {
    final boolean delayErrors;
    final Function<? super T, ? extends CompletableSource> mapper;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable$FlatMapCompletableMainObserver */
    static final class FlatMapCompletableMainObserver<T> extends AtomicInteger implements Disposable, Observer<T> {
        private static final long serialVersionUID = 8443155186132538303L;
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f311d;
        final boolean delayErrors;
        volatile boolean disposed;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends CompletableSource> mapper;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable$FlatMapCompletableMainObserver$InnerObserver */
        final class InnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 8606673141535671828L;

            InnerObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onComplete() {
                FlatMapCompletableMainObserver.this.innerComplete(this);
            }

            public void onError(Throwable e) {
                FlatMapCompletableMainObserver.this.innerError(this, e);
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }
        }

        FlatMapCompletableMainObserver(CompletableObserver observer, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2) {
            this.actual = observer;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            lazySet(1);
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f311d, d)) {
                this.f311d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T value) {
            try {
                CompletableSource cs = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
                getAndIncrement();
                InnerObserver inner = new InnerObserver<>();
                if (!this.disposed && this.set.add(inner)) {
                    cs.subscribe(inner);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f311d.dispose();
                onError(ex);
            }
        }

        public void onError(Throwable e) {
            if (!this.errors.addThrowable(e)) {
                RxJavaPlugins.onError(e);
            } else if (!this.delayErrors) {
                dispose();
                if (getAndSet(0) > 0) {
                    this.actual.onError(this.errors.terminate());
                }
            } else if (decrementAndGet() == 0) {
                this.actual.onError(this.errors.terminate());
            }
        }

        public void onComplete() {
            if (decrementAndGet() == 0) {
                Throwable ex = this.errors.terminate();
                if (ex != null) {
                    this.actual.onError(ex);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void dispose() {
            this.disposed = true;
            this.f311d.dispose();
            this.set.dispose();
        }

        public boolean isDisposed() {
            return this.f311d.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(InnerObserver inner) {
            this.set.delete(inner);
            onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver inner, Throwable e) {
            this.set.delete(inner);
            onError(e);
        }
    }

    public ObservableFlatMapCompletableCompletable(ObservableSource<T> source2, Function<? super T, ? extends CompletableSource> mapper2, boolean delayErrors2) {
        this.source = source2;
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new FlatMapCompletableMainObserver(observer, this.mapper, this.delayErrors));
    }

    public Observable<T> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFlatMapCompletable<T>(this.source, this.mapper, this.delayErrors));
    }
}
