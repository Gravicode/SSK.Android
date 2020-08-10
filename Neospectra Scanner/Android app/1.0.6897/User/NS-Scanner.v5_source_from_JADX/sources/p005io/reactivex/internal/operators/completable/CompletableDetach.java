package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

@Experimental
/* renamed from: io.reactivex.internal.operators.completable.CompletableDetach */
public final class CompletableDetach extends Completable {
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableDetach$DetachCompletableObserver */
    static final class DetachCompletableObserver implements CompletableObserver, Disposable {
        CompletableObserver actual;

        /* renamed from: d */
        Disposable f101d;

        DetachCompletableObserver(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.actual = null;
            this.f101d.dispose();
            this.f101d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f101d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f101d, d)) {
                this.f101d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.f101d = DisposableHelper.DISPOSED;
            CompletableObserver a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onError(e);
            }
        }

        public void onComplete() {
            this.f101d = DisposableHelper.DISPOSED;
            CompletableObserver a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onComplete();
            }
        }
    }

    public CompletableDetach(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new DetachCompletableObserver(observer));
    }
}
