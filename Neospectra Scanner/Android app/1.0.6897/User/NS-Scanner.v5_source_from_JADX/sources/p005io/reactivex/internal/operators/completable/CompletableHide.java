package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableHide */
public final class CompletableHide extends Completable {
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableHide$HideCompletableObserver */
    static final class HideCompletableObserver implements CompletableObserver, Disposable {
        final CompletableObserver actual;

        /* renamed from: d */
        Disposable f109d;

        HideCompletableObserver(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f109d.dispose();
            this.f109d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f109d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f109d, d)) {
                this.f109d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public CompletableHide(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new HideCompletableObserver(observer));
    }
}
