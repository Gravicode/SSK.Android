package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnError */
public final class SingleDoOnError<T> extends Single<T> {
    final Consumer<? super Throwable> onError;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnError$DoOnError */
    final class DoOnError implements SingleObserver<T> {

        /* renamed from: s */
        private final SingleObserver<? super T> f410s;

        DoOnError(SingleObserver<? super T> s) {
            this.f410s = s;
        }

        public void onSubscribe(Disposable d) {
            this.f410s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            this.f410s.onSuccess(value);
        }

        public void onError(Throwable e) {
            try {
                SingleDoOnError.this.onError.accept(e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.f410s.onError(e);
        }
    }

    public SingleDoOnError(SingleSource<T> source2, Consumer<? super Throwable> onError2) {
        this.source = source2;
        this.onError = onError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnError(s));
    }
}
