package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.single.SingleHide */
public final class SingleHide<T> extends Single<T> {
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleHide$HideSingleObserver */
    static final class HideSingleObserver<T> implements SingleObserver<T>, Disposable {
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f419d;

        HideSingleObserver(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f419d.dispose();
        }

        public boolean isDisposed() {
            return this.f419d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f419d, d)) {
                this.f419d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }
    }

    public SingleHide(SingleSource<? extends T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> subscriber) {
        this.source.subscribe(new HideSingleObserver(subscriber));
    }
}
