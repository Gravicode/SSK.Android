package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToMaybe;
import p005io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle */
public final class MaybeIsEmptySingle<T> extends Single<Boolean> implements HasUpstreamMaybeSource<T>, FuseToMaybe<Boolean> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final SingleObserver<? super Boolean> actual;

        /* renamed from: d */
        Disposable f261d;

        IsEmptyMaybeObserver(SingleObserver<? super Boolean> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f261d.dispose();
            this.f261d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f261d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f261d, d)) {
                this.f261d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f261d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable e) {
            this.f261d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f261d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmptySingle(MaybeSource<T> source2) {
        this.source = source2;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    public Maybe<Boolean> fuseToMaybe() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeIsEmpty<T>(this.source));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> observer) {
        this.source.subscribe(new IsEmptyMaybeObserver(observer));
    }
}
