package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty */
public final class MaybeIsEmpty<T> extends AbstractMaybeWithUpstream<T, Boolean> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super Boolean> actual;

        /* renamed from: d */
        Disposable f260d;

        IsEmptyMaybeObserver(MaybeObserver<? super Boolean> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f260d.dispose();
        }

        public boolean isDisposed() {
            return this.f260d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f260d, d)) {
                this.f260d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmpty(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super Boolean> observer) {
        this.source.subscribe(new IsEmptyMaybeObserver(observer));
    }
}
