package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess */
public final class MaybeDoAfterSuccess<T> extends AbstractMaybeWithUpstream<T, T> {
    final Consumer<? super T> onAfterSuccess;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess$DoAfterObserver */
    static final class DoAfterObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f244d;
        final Consumer<? super T> onAfterSuccess;

        DoAfterObserver(MaybeObserver<? super T> actual2, Consumer<? super T> onAfterSuccess2) {
            this.actual = actual2;
            this.onAfterSuccess = onAfterSuccess2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f244d, d)) {
                this.f244d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            try {
                this.onAfterSuccess.accept(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f244d.dispose();
        }

        public boolean isDisposed() {
            return this.f244d.isDisposed();
        }
    }

    public MaybeDoAfterSuccess(MaybeSource<T> source, Consumer<? super T> onAfterSuccess2) {
        super(source);
        this.onAfterSuccess = onAfterSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> s) {
        this.source.subscribe(new DoAfterObserver(s, this.onAfterSuccess));
    }
}
