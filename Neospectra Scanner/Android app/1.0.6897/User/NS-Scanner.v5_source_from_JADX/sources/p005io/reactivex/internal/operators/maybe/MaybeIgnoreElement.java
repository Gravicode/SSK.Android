package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIgnoreElement */
public final class MaybeIgnoreElement<T> extends AbstractMaybeWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIgnoreElement$IgnoreMaybeObserver */
    static final class IgnoreMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f258d;

        IgnoreMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f258d, d)) {
                this.f258d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.f258d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public void onError(Throwable e) {
            this.f258d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.f258d = DisposableHelper.DISPOSED;
            this.actual.onComplete();
        }

        public boolean isDisposed() {
            return this.f258d.isDisposed();
        }

        public void dispose() {
            this.f258d.dispose();
            this.f258d = DisposableHelper.DISPOSED;
        }
    }

    public MaybeIgnoreElement(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new IgnoreMaybeObserver(observer));
    }
}
