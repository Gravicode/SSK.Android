package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeHide */
public final class MaybeHide<T> extends AbstractMaybeWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeHide$HideMaybeObserver */
    static final class HideMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f257d;

        HideMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.f257d.dispose();
            this.f257d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f257d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f257d, d)) {
                this.f257d = d;
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
            this.actual.onComplete();
        }
    }

    public MaybeHide(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new HideMaybeObserver(observer));
    }
}
