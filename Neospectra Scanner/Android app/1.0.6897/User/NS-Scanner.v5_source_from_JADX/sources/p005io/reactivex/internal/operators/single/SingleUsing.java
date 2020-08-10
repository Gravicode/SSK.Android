package p005io.reactivex.internal.operators.single;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleUsing */
public final class SingleUsing<T, U> extends Single<T> {
    final Consumer<? super U> disposer;
    final boolean eager;
    final Callable<U> resourceSupplier;
    final Function<? super U, ? extends SingleSource<? extends T>> singleFunction;

    /* renamed from: io.reactivex.internal.operators.single.SingleUsing$UsingSingleObserver */
    static final class UsingSingleObserver<T, U> extends AtomicReference<Object> implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -5331524057054083935L;
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f424d;
        final Consumer<? super U> disposer;
        final boolean eager;

        UsingSingleObserver(SingleObserver<? super T> actual2, U resource, boolean eager2, Consumer<? super U> disposer2) {
            super(resource);
            this.actual = actual2;
            this.eager = eager2;
            this.disposer = disposer2;
        }

        public void dispose() {
            this.f424d.dispose();
            this.f424d = DisposableHelper.DISPOSED;
            disposeAfter();
        }

        public boolean isDisposed() {
            return this.f424d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f424d, d)) {
                this.f424d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f424d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object u = getAndSet(this);
                if (u != this) {
                    try {
                        this.disposer.accept(u);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        this.actual.onError(ex);
                        return;
                    }
                } else {
                    return;
                }
            }
            this.actual.onSuccess(value);
            if (!this.eager) {
                disposeAfter();
            }
        }

        public void onError(Throwable e) {
            this.f424d = DisposableHelper.DISPOSED;
            if (this.eager) {
                Object u = getAndSet(this);
                if (u != this) {
                    try {
                        this.disposer.accept(u);
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
                disposeAfter();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeAfter() {
            Object u = getAndSet(this);
            if (u != this) {
                try {
                    this.disposer.accept(u);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public SingleUsing(Callable<U> resourceSupplier2, Function<? super U, ? extends SingleSource<? extends T>> singleFunction2, Consumer<? super U> disposer2, boolean eager2) {
        this.resourceSupplier = resourceSupplier2;
        this.singleFunction = singleFunction2;
        this.disposer = disposer2;
        this.eager = eager2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        try {
            U resource = this.resourceSupplier.call();
            try {
                ((SingleSource) ObjectHelper.requireNonNull(this.singleFunction.apply(resource), "The singleFunction returned a null SingleSource")).subscribe(new UsingSingleObserver(s, resource, this.eager, this.disposer));
                return;
            } catch (Throwable exc) {
                Exceptions.throwIfFatal(exc);
                RxJavaPlugins.onError(exc);
            }
            EmptyDisposable.error(ex, s);
            if (!this.eager) {
                this.disposer.accept(resource);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, s);
        }
    }
}
