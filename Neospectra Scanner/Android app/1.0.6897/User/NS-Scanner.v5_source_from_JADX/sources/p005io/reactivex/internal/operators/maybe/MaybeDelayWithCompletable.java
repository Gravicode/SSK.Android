package p005io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeObserver;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable */
public final class MaybeDelayWithCompletable<T> extends Maybe<T> {
    final CompletableSource other;
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable$DelayWithMainObserver */
    static final class DelayWithMainObserver<T> implements MaybeObserver<T> {
        final MaybeObserver<? super T> actual;
        final AtomicReference<Disposable> parent;

        DelayWithMainObserver(AtomicReference<Disposable> parent2, MaybeObserver<? super T> actual2) {
            this.parent = parent2;
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this.parent, d);
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

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable$OtherObserver */
    static final class OtherObserver<T> extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 703409937383992161L;
        final MaybeObserver<? super T> actual;
        final MaybeSource<T> source;

        OtherObserver(MaybeObserver<? super T> actual2, MaybeSource<T> source2) {
            this.actual = actual2;
            this.source = source2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.source.subscribe(new DelayWithMainObserver(this, this.actual));
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public MaybeDelayWithCompletable(MaybeSource<T> source2, CompletableSource other2) {
        this.source = source2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> subscriber) {
        this.other.subscribe(new OtherObserver(subscriber, this.source));
    }
}
