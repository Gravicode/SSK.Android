package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle */
public final class MaybeSwitchIfEmptySingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
    final SingleSource<? extends T> other;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle$SwitchIfEmptyMaybeObserver */
    static final class SwitchIfEmptyMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 4603919676453758899L;
        final SingleObserver<? super T> actual;
        final SingleSource<? extends T> other;

        /* renamed from: io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle$SwitchIfEmptyMaybeObserver$OtherSingleObserver */
        static final class OtherSingleObserver<T> implements SingleObserver<T> {
            final SingleObserver<? super T> actual;
            final AtomicReference<Disposable> parent;

            OtherSingleObserver(SingleObserver<? super T> actual2, AtomicReference<Disposable> parent2) {
                this.actual = actual2;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this.parent, d);
            }

            public void onSuccess(T value) {
                this.actual.onSuccess(value);
            }

            public void onError(Throwable e) {
                this.actual.onError(e);
            }
        }

        SwitchIfEmptyMaybeObserver(SingleObserver<? super T> actual2, SingleSource<? extends T> other2) {
            this.actual = actual2;
            this.other = other2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            Disposable d = (Disposable) get();
            if (d != DisposableHelper.DISPOSED && compareAndSet(d, null)) {
                this.other.subscribe(new OtherSingleObserver(this.actual, this));
            }
        }
    }

    public MaybeSwitchIfEmptySingle(MaybeSource<T> source2, SingleSource<? extends T> other2) {
        this.source = source2;
        this.other = other2;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new SwitchIfEmptyMaybeObserver(observer, this.other));
    }
}
