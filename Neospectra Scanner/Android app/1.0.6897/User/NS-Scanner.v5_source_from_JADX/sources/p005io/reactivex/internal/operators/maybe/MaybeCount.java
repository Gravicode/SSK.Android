package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeCount */
public final class MaybeCount<T> extends Single<Long> implements HasUpstreamMaybeSource<T> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeCount$CountMaybeObserver */
    static final class CountMaybeObserver implements MaybeObserver<Object>, Disposable {
        final SingleObserver<? super Long> actual;

        /* renamed from: d */
        Disposable f240d;

        CountMaybeObserver(SingleObserver<? super Long> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f240d, d)) {
                this.f240d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object value) {
            this.f240d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Long.valueOf(1));
        }

        public void onError(Throwable e) {
            this.f240d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f240d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(Long.valueOf(0));
        }

        public boolean isDisposed() {
            return this.f240d.isDisposed();
        }

        public void dispose() {
            this.f240d.dispose();
            this.f240d = DisposableHelper.DISPOSED;
        }
    }

    public MaybeCount(MaybeSource<T> source2) {
        this.source = source2;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Long> observer) {
        this.source.subscribe(new CountMaybeObserver(observer));
    }
}
