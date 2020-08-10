package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToMaybe;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable */
public final class MaybeIgnoreElementCompletable<T> extends Completable implements FuseToMaybe<T> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable$IgnoreMaybeObserver */
    static final class IgnoreMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f259d;

        IgnoreMaybeObserver(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f259d, d)) {
                this.f259d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f259d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public void onError(Throwable e) {
            this.f259d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f259d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public boolean isDisposed() {
            return this.f259d.isDisposed();
        }

        public void dispose() {
            this.f259d.dispose();
            this.f259d = DisposableHelper.DISPOSED;
        }
    }

    public MaybeIgnoreElementCompletable(MaybeSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new IgnoreMaybeObserver(observer));
    }

    public Maybe<T> fuseToMaybe() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeIgnoreElement<T>(this.source));
    }
}
