package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnSuccess */
public final class SingleDoOnSuccess<T> extends Single<T> {
    final Consumer<? super T> onSuccess;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnSuccess$DoOnSuccess */
    final class DoOnSuccess implements SingleObserver<T> {

        /* renamed from: s */
        private final SingleObserver<? super T> f412s;

        DoOnSuccess(SingleObserver<? super T> s) {
            this.f412s = s;
        }

        public void onSubscribe(Disposable d) {
            this.f412s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                SingleDoOnSuccess.this.onSuccess.accept(value);
                this.f412s.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f412s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.f412s.onError(e);
        }
    }

    public SingleDoOnSuccess(SingleSource<T> source2, Consumer<? super T> onSuccess2) {
        this.source = source2;
        this.onSuccess = onSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnSuccess(s));
    }
}
