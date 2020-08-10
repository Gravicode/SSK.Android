package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p005io.reactivex.Emitter;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableGenerate */
public final class ObservableGenerate<T, S> extends Observable<T> {
    final Consumer<? super S> disposeState;
    final BiFunction<S, Emitter<T>, S> generator;
    final Callable<S> stateSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableGenerate$GeneratorDisposable */
    static final class GeneratorDisposable<T, S> implements Emitter<T>, Disposable {
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final Consumer<? super S> disposeState;
        final BiFunction<S, ? super Emitter<T>, S> generator;
        boolean hasNext;
        S state;
        boolean terminate;

        GeneratorDisposable(Observer<? super T> actual2, BiFunction<S, ? super Emitter<T>, S> generator2, Consumer<? super S> disposeState2, S initialState) {
            this.actual = actual2;
            this.generator = generator2;
            this.disposeState = disposeState2;
            this.state = initialState;
        }

        public void run() {
            S s = this.state;
            if (this.cancelled) {
                this.state = null;
                dispose(s);
                return;
            }
            BiFunction<S, ? super Emitter<T>, S> f = this.generator;
            while (!this.cancelled) {
                this.hasNext = false;
                try {
                    s = f.apply(s, this);
                    if (this.terminate) {
                        this.cancelled = true;
                        this.state = null;
                        dispose(s);
                        return;
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.state = null;
                    this.cancelled = true;
                    onError(ex);
                    dispose(s);
                    return;
                }
            }
            this.state = null;
            dispose(s);
        }

        private void dispose(S s) {
            try {
                this.disposeState.accept(s);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void dispose() {
            this.cancelled = true;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void onNext(T t) {
            if (this.terminate) {
                return;
            }
            if (this.hasNext) {
                onError(new IllegalStateException("onNext already called in this generate turn"));
            } else if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            } else {
                this.hasNext = true;
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (this.terminate) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            this.terminate = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.terminate) {
                this.terminate = true;
                this.actual.onComplete();
            }
        }
    }

    public ObservableGenerate(Callable<S> stateSupplier2, BiFunction<S, Emitter<T>, S> generator2, Consumer<? super S> disposeState2) {
        this.stateSupplier = stateSupplier2;
        this.generator = generator2;
        this.disposeState = disposeState2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            GeneratorDisposable<T, S> gd = new GeneratorDisposable<>(s, this.generator, this.disposeState, this.stateSupplier.call());
            s.onSubscribe(gd);
            gd.run();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, s);
        }
    }
}
