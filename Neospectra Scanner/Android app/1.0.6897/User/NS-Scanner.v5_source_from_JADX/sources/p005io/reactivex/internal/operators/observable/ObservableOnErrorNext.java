package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorNext */
public final class ObservableOnErrorNext<T> extends AbstractObservableWithUpstream<T, T> {
    final boolean allowFatal;
    final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorNext$OnErrorNextObserver */
    static final class OnErrorNextObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final boolean allowFatal;
        final SequentialDisposable arbiter = new SequentialDisposable();
        boolean done;
        final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
        boolean once;

        OnErrorNextObserver(Observer<? super T> actual2, Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier2, boolean allowFatal2) {
            this.actual = actual2;
            this.nextSupplier = nextSupplier2;
            this.allowFatal = allowFatal2;
        }

        public void onSubscribe(Disposable s) {
            this.arbiter.replace(s);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (!this.once) {
                this.once = true;
                if (!this.allowFatal || (t instanceof Exception)) {
                    try {
                        ObservableSource observableSource = (ObservableSource) this.nextSupplier.apply(t);
                        if (observableSource == null) {
                            NullPointerException npe = new NullPointerException("Observable is null");
                            npe.initCause(t);
                            this.actual.onError(npe);
                            return;
                        }
                        observableSource.subscribe(this);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.actual.onError(new CompositeException(t, e));
                    }
                } else {
                    this.actual.onError(t);
                }
            } else if (this.done) {
                RxJavaPlugins.onError(t);
            } else {
                this.actual.onError(t);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.once = true;
                this.actual.onComplete();
            }
        }
    }

    public ObservableOnErrorNext(ObservableSource<T> source, Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier2, boolean allowFatal2) {
        super(source);
        this.nextSupplier = nextSupplier2;
        this.allowFatal = allowFatal2;
    }

    public void subscribeActual(Observer<? super T> t) {
        OnErrorNextObserver<T> parent = new OnErrorNextObserver<>(t, this.nextSupplier, this.allowFatal);
        t.onSubscribe(parent.arbiter);
        this.source.subscribe(parent);
    }
}
