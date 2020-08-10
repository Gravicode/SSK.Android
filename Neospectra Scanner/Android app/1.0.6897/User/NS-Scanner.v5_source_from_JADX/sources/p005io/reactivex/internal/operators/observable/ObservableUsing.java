package p005io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableUsing */
public final class ObservableUsing<T, D> extends Observable<T> {
    final Consumer<? super D> disposer;
    final boolean eager;
    final Callable<? extends D> resourceSupplier;
    final Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableUsing$UsingObserver */
    static final class UsingObserver<T, D> extends AtomicBoolean implements Observer<T>, Disposable {
        private static final long serialVersionUID = 5904473792286235046L;
        final Observer<? super T> actual;
        final Consumer<? super D> disposer;
        final boolean eager;
        final D resource;

        /* renamed from: s */
        Disposable f370s;

        UsingObserver(Observer<? super T> actual2, D resource2, Consumer<? super D> disposer2, boolean eager2) {
            this.actual = actual2;
            this.resource = resource2;
            this.disposer = disposer2;
            this.eager = eager2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f370s, s)) {
                this.f370s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        t = new CompositeException(t, e);
                    }
                }
                this.f370s.dispose();
                this.actual.onError(t);
                return;
            }
            this.actual.onError(t);
            this.f370s.dispose();
            disposeAfter();
        }

        public void onComplete() {
            if (this.eager) {
                if (compareAndSet(false, true)) {
                    try {
                        this.disposer.accept(this.resource);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        this.actual.onError(e);
                        return;
                    }
                }
                this.f370s.dispose();
                this.actual.onComplete();
            } else {
                this.actual.onComplete();
                this.f370s.dispose();
                disposeAfter();
            }
        }

        public void dispose() {
            disposeAfter();
            this.f370s.dispose();
        }

        public boolean isDisposed() {
            return get();
        }

        /* access modifiers changed from: 0000 */
        public void disposeAfter() {
            if (compareAndSet(false, true)) {
                try {
                    this.disposer.accept(this.resource);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    RxJavaPlugins.onError(e);
                }
            }
        }
    }

    public ObservableUsing(Callable<? extends D> resourceSupplier2, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier2, Consumer<? super D> disposer2, boolean eager2) {
        this.resourceSupplier = resourceSupplier2;
        this.sourceSupplier = sourceSupplier2;
        this.disposer = disposer2;
        this.eager = eager2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            D resource = this.resourceSupplier.call();
            try {
                ((ObservableSource) ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null ObservableSource")).subscribe(new UsingObserver<>(s, resource, this.disposer, this.eager));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptyDisposable.error((Throwable) new CompositeException(e, ex), s);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, s);
        }
    }
}
