package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableUsing */
public final class CompletableUsing<R> extends Completable {
    final Function<? super R, ? extends CompletableSource> completableFunction;
    final Consumer<? super R> disposer;
    final boolean eager;
    final Callable<R> resourceSupplier;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableUsing$UsingObserver */
    static final class UsingObserver<R> extends AtomicReference<Object> implements CompletableObserver, Disposable {
        private static final long serialVersionUID = -674404550052917487L;
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f117d;
        final Consumer<? super R> disposer;
        final boolean eager;

        UsingObserver(CompletableObserver actual2, R resource, Consumer<? super R> disposer2, boolean eager2) {
            super(resource);
            this.actual = actual2;
            this.disposer = disposer2;
            this.eager = eager2;
        }

        public void dispose() {
            this.f117d.dispose();
            this.f117d = DisposableHelper.DISPOSED;
            disposeResourceAfter();
        }

        /* access modifiers changed from: 0000 */
        public void disposeResourceAfter() {
            Object resource = getAndSet(this);
            if (resource != this) {
                try {
                    this.disposer.accept(resource);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }

        public boolean isDisposed() {
            return this.f117d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f117d, d)) {
                this.f117d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.f117d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object resource = getAndSet(this);
                if (resource != this) {
                    try {
                        this.disposer.accept(resource);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        e = new CompositeException(e, ex);
                    }
                } else {
                    return;
                }
            }
            this.actual.onError(e);
            if (!this.eager) {
                disposeResourceAfter();
            }
        }

        public void onComplete() {
            this.f117d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object resource = getAndSet(this);
                if (resource != this) {
                    try {
                        this.disposer.accept(resource);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.actual.onError(ex);
                        return;
                    }
                } else {
                    return;
                }
            }
            this.actual.onComplete();
            if (!this.eager) {
                disposeResourceAfter();
            }
        }
    }

    public CompletableUsing(Callable<R> resourceSupplier2, Function<? super R, ? extends CompletableSource> completableFunction2, Consumer<? super R> disposer2, boolean eager2) {
        this.resourceSupplier = resourceSupplier2;
        this.completableFunction = completableFunction2;
        this.disposer = disposer2;
        this.eager = eager2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        try {
            R resource = this.resourceSupplier.call();
            try {
                ((CompletableSource) ObjectHelper.requireNonNull(this.completableFunction.apply(resource), "The completableFunction returned a null CompletableSource")).subscribe(new UsingObserver(observer, resource, this.disposer, this.eager));
            } catch (Throwable exc) {
                Exceptions.throwIfFatal(exc);
                RxJavaPlugins.onError(exc);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
        }
    }
}
