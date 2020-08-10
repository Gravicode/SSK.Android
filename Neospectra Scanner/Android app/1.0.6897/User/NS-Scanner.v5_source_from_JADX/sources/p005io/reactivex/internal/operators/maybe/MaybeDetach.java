package p005io.reactivex.internal.operators.maybe;

import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDetach */
public final class MaybeDetach<T> extends AbstractMaybeWithUpstream<T, T> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDetach$DetachMaybeObserver */
    static final class DetachMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        MaybeObserver<? super T> actual;

        /* renamed from: d */
        Disposable f243d;

        DetachMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.actual = null;
            this.f243d.dispose();
            this.f243d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.f243d.isDisposed();
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f243d, d)) {
                this.f243d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.f243d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            this.f243d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onError(e);
            }
        }

        public void onComplete() {
            this.f243d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onComplete();
            }
        }
    }

    public MaybeDetach(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new DetachMaybeObserver(observer));
    }
}
