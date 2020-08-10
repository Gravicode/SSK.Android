package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeUsing */
public final class MaybeUsing<T, D> extends Maybe<T> {
    final boolean eager;
    final Consumer<? super D> resourceDisposer;
    final Callable<? extends D> resourceSupplier;
    final Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeUsing$UsingObserver */
    static final class UsingObserver<T, D> extends AtomicReference<Object> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = -674404550052917487L;
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f271d;
        final Consumer<? super D> disposer;
        final boolean eager;

        UsingObserver(MaybeObserver<? super T> actual2, D resource, Consumer<? super D> disposer2, boolean eager2) {
            super(resource);
            this.actual = actual2;
            this.disposer = disposer2;
            this.eager = eager2;
        }

        public void dispose() {
            this.f271d.dispose();
            this.f271d = DisposableHelper.DISPOSED;
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
            return this.f271d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f271d, d)) {
                this.f271d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f271d = DisposableHelper.DISPOSED;
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
            this.actual.onSuccess(value);
            if (!this.eager) {
                disposeResourceAfter();
            }
        }

        public void onError(Throwable e) {
            this.f271d = DisposableHelper.DISPOSED;
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
            this.f271d = DisposableHelper.DISPOSED;
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

    public MaybeUsing(Callable<? extends D> resourceSupplier2, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier2, Consumer<? super D> resourceDisposer2, boolean eager2) {
        this.resourceSupplier = resourceSupplier2;
        this.sourceSupplier = sourceSupplier2;
        this.resourceDisposer = resourceDisposer2;
        this.eager = eager2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        try {
            D resource = this.resourceSupplier.call();
            try {
                ((MaybeSource) ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null MaybeSource")).subscribe(new UsingObserver(observer, resource, this.resourceDisposer, this.eager));
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
