package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamCompletableSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFromCompletable */
public final class MaybeFromCompletable<T> extends Maybe<T> implements HasUpstreamCompletableSource {
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFromCompletable$FromCompletableObserver */
    static final class FromCompletableObserver<T> implements CompletableObserver, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f255d;

        FromCompletableObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f255d.dispose();
            this.f255d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f255d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f255d, d)) {
                this.f255d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onComplete() {
            this.f255d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public void onError(Throwable e) {
            this.f255d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }
    }

    public MaybeFromCompletable(CompletableSource source2) {
        this.source = source2;
    }

    public CompletableSource source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new FromCompletableObserver(observer));
    }
}
