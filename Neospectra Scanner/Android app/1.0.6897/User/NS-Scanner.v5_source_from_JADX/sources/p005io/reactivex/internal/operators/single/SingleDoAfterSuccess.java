package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.single.SingleDoAfterSuccess */
public final class SingleDoAfterSuccess<T> extends Single<T> {
    final Consumer<? super T> onAfterSuccess;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoAfterSuccess$DoAfterObserver */
    static final class DoAfterObserver<T> implements SingleObserver<T>, Disposable {
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f406d;
        final Consumer<? super T> onAfterSuccess;

        DoAfterObserver(SingleObserver<? super T> actual2, Consumer<? super T> onAfterSuccess2) {
            this.actual = actual2;
            this.onAfterSuccess = onAfterSuccess2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f406d, d)) {
                this.f406d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            try {
                this.onAfterSuccess.accept(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void dispose() {
            this.f406d.dispose();
        }

        public boolean isDisposed() {
            return this.f406d.isDisposed();
        }
    }

    public SingleDoAfterSuccess(SingleSource<T> source2, Consumer<? super T> onAfterSuccess2) {
        this.source = source2;
        this.onAfterSuccess = onAfterSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoAfterObserver(s, this.onAfterSuccess));
    }
}
