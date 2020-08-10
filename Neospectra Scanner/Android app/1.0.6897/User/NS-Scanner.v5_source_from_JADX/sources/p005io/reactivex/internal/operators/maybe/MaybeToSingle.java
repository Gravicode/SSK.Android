package p005io.reactivex.internal.operators.maybe;

import java.util.NoSuchElementException;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeToSingle */
public final class MaybeToSingle<T> extends Single<T> implements HasUpstreamMaybeSource<T> {
    final T defaultValue;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeToSingle$ToSingleMaybeSubscriber */
    static final class ToSingleMaybeSubscriber<T> implements MaybeObserver<T>, Disposable {
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f269d;
        final T defaultValue;

        ToSingleMaybeSubscriber(SingleObserver<? super T> actual2, T defaultValue2) {
            this.actual = actual2;
            this.defaultValue = defaultValue2;
        }

        public void dispose() {
            this.f269d.dispose();
            this.f269d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f269d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f269d, d)) {
                this.f269d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f269d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.f269d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f269d = DisposableHelper.DISPOSED;
            if (this.defaultValue != null) {
                this.actual.onSuccess(this.defaultValue);
            } else {
                this.actual.onError(new NoSuchElementException("The MaybeSource is empty"));
            }
        }
    }

    public MaybeToSingle(MaybeSource<T> source2, T defaultValue2) {
        this.source = source2;
        this.defaultValue = defaultValue2;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new ToSingleMaybeSubscriber(observer, this.defaultValue));
    }
}
