package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamSingleSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFromSingle */
public final class MaybeFromSingle<T> extends Maybe<T> implements HasUpstreamSingleSource<T> {
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFromSingle$FromSingleObserver */
    static final class FromSingleObserver<T> implements SingleObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f256d;

        FromSingleObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f256d.dispose();
            this.f256d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f256d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f256d, d)) {
                this.f256d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f256d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.f256d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }
    }

    public MaybeFromSingle(SingleSource<T> source2) {
        this.source = source2;
    }

    public SingleSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new FromSingleObserver(observer));
    }
}
