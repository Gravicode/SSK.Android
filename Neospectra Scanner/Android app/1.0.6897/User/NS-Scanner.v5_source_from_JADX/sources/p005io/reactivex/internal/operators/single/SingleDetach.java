package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

@Experimental
/* renamed from: io.reactivex.internal.operators.single.SingleDetach */
public final class SingleDetach<T> extends Single<T> {
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDetach$DetachSingleObserver */
    static final class DetachSingleObserver<T> implements SingleObserver<T>, Disposable {
        SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f405d;

        DetachSingleObserver(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.actual = null;
            this.f405d.dispose();
            this.f405d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f405d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f405d, d)) {
                this.f405d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f405d = DisposableHelper.DISPOSED;
            SingleObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            this.f405d = DisposableHelper.DISPOSED;
            SingleObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onError(e);
            }
        }
    }

    public SingleDetach(SingleSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new DetachSingleObserver(observer));
    }
}
