package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.completable.CompletableDoFinally */
public final class CompletableDoFinally extends Completable {
    final Action onFinally;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver extends AtomicInteger implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f104d;
        final Action onFinally;

        DoFinallyObserver(CompletableObserver actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f104d, d)) {
                this.f104d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.f104d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f104d.isDisposed();
        }

        /* access modifiers changed from: 0000 */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    public CompletableDoFinally(CompletableSource source2, Action onFinally2) {
        this.source = source2;
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new DoFinallyObserver(s, this.onFinally));
    }
}
