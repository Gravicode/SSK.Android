package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.observers.ResumeSingleObserver;

/* renamed from: io.reactivex.internal.operators.single.SingleDelayWithSingle */
public final class SingleDelayWithSingle<T, U> extends Single<T> {
    final SingleSource<U> other;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDelayWithSingle$OtherObserver */
    static final class OtherObserver<T, U> extends AtomicReference<Disposable> implements SingleObserver<U>, Disposable {
        private static final long serialVersionUID = -8565274649390031272L;
        final SingleObserver<? super T> actual;
        final SingleSource<T> source;

        OtherObserver(SingleObserver<? super T> actual2, SingleSource<T> source2) {
            this.actual = actual2;
            this.source = source2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(U u) {
            this.source.subscribe(new ResumeSingleObserver(this, this.actual));
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public SingleDelayWithSingle(SingleSource<T> source2, SingleSource<U> other2) {
        this.source = source2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> subscriber) {
        this.other.subscribe(new OtherObserver(subscriber, this.source));
    }
}
