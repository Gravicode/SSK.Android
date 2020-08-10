package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnDispose */
public final class SingleDoOnDispose<T> extends Single<T> {
    final Action onDispose;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnDispose$DoOnDisposeObserver */
    static final class DoOnDisposeObserver<T> extends AtomicReference<Action> implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -8583764624474935784L;
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f409d;

        DoOnDisposeObserver(SingleObserver<? super T> actual2, Action onDispose) {
            this.actual = actual2;
            lazySet(onDispose);
        }

        public void dispose() {
            Action a = (Action) getAndSet(null);
            if (a != null) {
                try {
                    a.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
                this.f409d.dispose();
            }
        }

        public boolean isDisposed() {
            return this.f409d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f409d, d)) {
                this.f409d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }
    }

    public SingleDoOnDispose(SingleSource<T> source2, Action onDispose2) {
        this.source = source2;
        this.onDispose = onDispose2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnDisposeObserver(s, this.onDispose));
    }
}
