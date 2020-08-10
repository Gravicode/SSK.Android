package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;

/* renamed from: io.reactivex.internal.operators.completable.CompletableToSingle */
public final class CompletableToSingle<T> extends Single<T> {
    final T completionValue;
    final Callable<? extends T> completionValueSupplier;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableToSingle$ToSingle */
    final class ToSingle implements CompletableObserver {
        private final SingleObserver<? super T> observer;

        ToSingle(SingleObserver<? super T> observer2) {
            this.observer = observer2;
        }

        public void onComplete() {
            T v;
            if (CompletableToSingle.this.completionValueSupplier != null) {
                try {
                    v = CompletableToSingle.this.completionValueSupplier.call();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.observer.onError(e);
                    return;
                }
            } else {
                v = CompletableToSingle.this.completionValue;
            }
            if (v == null) {
                this.observer.onError(new NullPointerException("The value supplied is null"));
            } else {
                this.observer.onSuccess(v);
            }
        }

        public void onError(Throwable e) {
            this.observer.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.observer.onSubscribe(d);
        }
    }

    public CompletableToSingle(CompletableSource source2, Callable<? extends T> completionValueSupplier2, T completionValue2) {
        this.source = source2;
        this.completionValue = completionValue2;
        this.completionValueSupplier = completionValueSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new ToSingle(s));
    }
}
