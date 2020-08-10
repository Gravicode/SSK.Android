package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.CompositeException;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;

/* renamed from: io.reactivex.internal.operators.single.SingleOnErrorReturn */
public final class SingleOnErrorReturn<T> extends Single<T> {
    final SingleSource<? extends T> source;
    final T value;
    final Function<? super Throwable, ? extends T> valueSupplier;

    /* renamed from: io.reactivex.internal.operators.single.SingleOnErrorReturn$OnErrorReturn */
    final class OnErrorReturn implements SingleObserver<T> {
        private final SingleObserver<? super T> observer;

        OnErrorReturn(SingleObserver<? super T> observer2) {
            this.observer = observer2;
        }

        public void onError(Throwable e) {
            T v;
            if (SingleOnErrorReturn.this.valueSupplier != null) {
                try {
                    v = SingleOnErrorReturn.this.valueSupplier.apply(e);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.observer.onError(new CompositeException(e, ex));
                    return;
                }
            } else {
                v = SingleOnErrorReturn.this.value;
            }
            if (v == null) {
                NullPointerException npe = new NullPointerException("Value supplied was null");
                npe.initCause(e);
                this.observer.onError(npe);
                return;
            }
            this.observer.onSuccess(v);
        }

        public void onSubscribe(Disposable d) {
            this.observer.onSubscribe(d);
        }

        public void onSuccess(T value) {
            this.observer.onSuccess(value);
        }
    }

    public SingleOnErrorReturn(SingleSource<? extends T> source2, Function<? super Throwable, ? extends T> valueSupplier2, T value2) {
        this.source = source2;
        this.valueSupplier = valueSupplier2;
        this.value = value2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new OnErrorReturn(s));
    }
}
