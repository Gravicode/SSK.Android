package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.maybe.MaybeDoFinally */
public final class MaybeDoFinally<T> extends AbstractMaybeWithUpstream<T, T> {
    final Action onFinally;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDoFinally$DoFinallyObserver */
    static final class DoFinallyObserver<T> extends AtomicInteger implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f245d;
        final Action onFinally;

        DoFinallyObserver(MaybeObserver<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f245d, d)) {
                this.f245d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            runFinally();
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
            this.f245d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.f245d.isDisposed();
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

    public MaybeDoFinally(MaybeSource<T> source, Action onFinally2) {
        super(source);
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> s) {
        this.source.subscribe(new DoFinallyObserver(s, this.onFinally));
    }
}
