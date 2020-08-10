package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableAmb */
public final class ObservableAmb<T> extends Observable<T> {
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableAmb$AmbCoordinator */
    static final class AmbCoordinator<T> implements Disposable {
        final Observer<? super T> actual;
        final AmbInnerObserver<T>[] observers;
        final AtomicInteger winner = new AtomicInteger();

        AmbCoordinator(Observer<? super T> actual2, int count) {
            this.actual = actual2;
            this.observers = new AmbInnerObserver[count];
        }

        public void subscribe(ObservableSource<? extends T>[] sources) {
            AmbInnerObserver<T>[] as = this.observers;
            int len = as.length;
            for (int i = 0; i < len; i++) {
                as[i] = new AmbInnerObserver<>(this, i + 1, this.actual);
            }
            this.winner.lazySet(0);
            this.actual.onSubscribe(this);
            for (int i2 = 0; i2 < len && this.winner.get() == 0; i2++) {
                sources[i2].subscribe(as[i2]);
            }
        }

        public boolean win(int index) {
            int w = this.winner.get();
            boolean z = true;
            if (w != 0) {
                if (w != index) {
                    z = false;
                }
                return z;
            } else if (!this.winner.compareAndSet(0, index)) {
                return false;
            } else {
                AmbInnerObserver<T>[] a = this.observers;
                int n = a.length;
                for (int i = 0; i < n; i++) {
                    if (i + 1 != index) {
                        a[i].dispose();
                    }
                }
                return true;
            }
        }

        public void dispose() {
            if (this.winner.get() != -1) {
                this.winner.lazySet(-1);
                for (AmbInnerObserver<T> a : this.observers) {
                    a.dispose();
                }
            }
        }

        public boolean isDisposed() {
            return this.winner.get() == -1;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableAmb$AmbInnerObserver */
    static final class AmbInnerObserver<T> extends AtomicReference<Disposable> implements Observer<T> {
        private static final long serialVersionUID = -1185974347409665484L;
        final Observer<? super T> actual;
        final int index;
        final AmbCoordinator<T> parent;
        boolean won;

        AmbInnerObserver(AmbCoordinator<T> parent2, int index2, Observer<? super T> actual2) {
            this.parent = parent2;
            this.index = index2;
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s) {
            DisposableHelper.setOnce(this, s);
        }

        public void onNext(T t) {
            if (this.won) {
                this.actual.onNext(t);
            } else if (this.parent.win(this.index)) {
                this.won = true;
                this.actual.onNext(t);
            } else {
                ((Disposable) get()).dispose();
            }
        }

        public void onError(Throwable t) {
            if (this.won) {
                this.actual.onError(t);
            } else if (this.parent.win(this.index)) {
                this.won = true;
                this.actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (this.won) {
                this.actual.onComplete();
            } else if (this.parent.win(this.index)) {
                this.won = true;
                this.actual.onComplete();
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }

    public ObservableAmb(ObservableSource<? extends T>[] sources2, Iterable<? extends ObservableSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    public void subscribeActual(Observer<? super T> s) {
        ObservableSource<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new Observable[8];
            try {
                for (ObservableSource<? extends T> p : this.sourcesIterable) {
                    if (p == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), s);
                        return;
                    }
                    if (count == sources2.length) {
                        ObservableSource<? extends T>[] b = new ObservableSource[((count >> 2) + count)];
                        System.arraycopy(sources2, 0, b, 0, count);
                        sources2 = b;
                    }
                    int count2 = count + 1;
                    try {
                        sources2[count] = p;
                        count = count2;
                    } catch (Throwable th) {
                        e = th;
                        int i = count2;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, s);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, s);
                return;
            }
        } else {
            count = sources2.length;
        }
        if (count == 0) {
            EmptyDisposable.complete(s);
        } else if (count == 1) {
            sources2[0].subscribe(s);
        } else {
            new AmbCoordinator<>(s, count).subscribe(sources2);
        }
    }
}
