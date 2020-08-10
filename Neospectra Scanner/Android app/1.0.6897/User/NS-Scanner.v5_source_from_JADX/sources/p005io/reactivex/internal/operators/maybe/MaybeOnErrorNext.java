package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorNext */
public final class MaybeOnErrorNext<T> extends AbstractMaybeWithUpstream<T, T> {
    final boolean allowFatal;
    final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorNext$OnErrorNextMaybeObserver */
    static final class OnErrorNextMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 2026620218879969836L;
        final MaybeObserver<? super T> actual;
        final boolean allowFatal;
        final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;

        /* renamed from: io.reactivex.internal.operators.maybe.MaybeOnErrorNext$OnErrorNextMaybeObserver$NextMaybeObserver */
        static final class NextMaybeObserver<T> implements MaybeObserver<T> {
            final MaybeObserver<? super T> actual;

            /* renamed from: d */
            final AtomicReference<Disposable> f264d;

            NextMaybeObserver(MaybeObserver<? super T> actual2, AtomicReference<Disposable> d) {
                this.actual = actual2;
                this.f264d = d;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this.f264d, d);
            }

            public void onSuccess(T value) {
                this.actual.onSuccess(value);
            }

            public void onError(Throwable e) {
                this.actual.onError(e);
            }

            public void onComplete() {
                this.actual.onComplete();
            }
        }

        OnErrorNextMaybeObserver(MaybeObserver<? super T> actual2, Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction2, boolean allowFatal2) {
            this.actual = actual2;
            this.resumeFunction = resumeFunction2;
            this.allowFatal = allowFatal2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            if (this.allowFatal || (e instanceof Exception)) {
                try {
                    MaybeSource<? extends T> m = (MaybeSource) ObjectHelper.requireNonNull(this.resumeFunction.apply(e), "The resumeFunction returned a null MaybeSource");
                    DisposableHelper.replace(this, null);
                    m.subscribe(new NextMaybeObserver(this.actual, this));
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.actual.onError(new CompositeException(e, ex));
                }
            } else {
                this.actual.onError(e);
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public MaybeOnErrorNext(MaybeSource<T> source, Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction2, boolean allowFatal2) {
        super(source);
        this.resumeFunction = resumeFunction2;
        this.allowFatal = allowFatal2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new OnErrorNextMaybeObserver(observer, this.resumeFunction, this.allowFatal));
    }
}
