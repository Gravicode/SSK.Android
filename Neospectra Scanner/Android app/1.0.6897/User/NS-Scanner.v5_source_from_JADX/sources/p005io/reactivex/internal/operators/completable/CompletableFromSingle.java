package p005io.reactivex.internal.operators.completable;

import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle */
public final class CompletableFromSingle<T> extends Completable {
    final SingleSource<T> single;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle$CompletableFromSingleObserver */
    static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {

        /* renamed from: co */
        final CompletableObserver f108co;

        CompletableFromSingleObserver(CompletableObserver co) {
            this.f108co = co;
        }

        public void onError(Throwable e) {
            this.f108co.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.f108co.onSubscribe(d);
        }

        public void onSuccess(T t) {
            this.f108co.onComplete();
        }
    }

    public CompletableFromSingle(SingleSource<T> single2) {
        this.single = single2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.single.subscribe(new CompletableFromSingleObserver(s));
    }
}
