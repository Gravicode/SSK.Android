package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe */
public final class SingleDoOnSubscribe<T> extends Single<T> {
    final Consumer<? super Disposable> onSubscribe;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe$DoOnSubscribeSingleObserver */
    static final class DoOnSubscribeSingleObserver<T> implements SingleObserver<T> {
        final SingleObserver<? super T> actual;
        boolean done;
        final Consumer<? super Disposable> onSubscribe;

        DoOnSubscribeSingleObserver(SingleObserver<? super T> actual2, Consumer<? super Disposable> onSubscribe2) {
            this.actual = actual2;
            this.onSubscribe = onSubscribe2;
        }

        public void onSubscribe(Disposable d) {
            try {
                this.onSubscribe.accept(d);
                this.actual.onSubscribe(d);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.done = true;
                d.dispose();
                EmptyDisposable.error(ex, this.actual);
            }
        }

        public void onSuccess(T value) {
            if (!this.done) {
                this.actual.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
            } else {
                this.actual.onError(e);
            }
        }
    }

    public SingleDoOnSubscribe(SingleSource<T> source2, Consumer<? super Disposable> onSubscribe2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnSubscribeSingleObserver(s, this.onSubscribe));
    }
}
