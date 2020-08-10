package p005io.reactivex.internal.operators.single;

import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoAfterTerminate */
public final class SingleDoAfterTerminate<T> extends Single<T> {
    final Action onAfterTerminate;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoAfterTerminate$DoAfterTerminateObserver */
    static final class DoAfterTerminateObserver<T> implements SingleObserver<T>, Disposable {
        final SingleObserver<? super T> actual;

        /* renamed from: d */
        Disposable f407d;
        final Action onAfterTerminate;

        DoAfterTerminateObserver(SingleObserver<? super T> actual2, Action onAfterTerminate2) {
            this.actual = actual2;
            this.onAfterTerminate = onAfterTerminate2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.f407d, d)) {
                this.f407d = d;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            onAfterTerminate();
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
            onAfterTerminate();
        }

        public void dispose() {
            this.f407d.dispose();
        }

        public boolean isDisposed() {
            return this.f407d.isDisposed();
        }

        private void onAfterTerminate() {
            try {
                this.onAfterTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }
    }

    public SingleDoAfterTerminate(SingleSource<T> source2, Action onAfterTerminate2) {
        this.source = source2;
        this.onAfterTerminate = onAfterTerminate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoAfterTerminateObserver(s, this.onAfterTerminate));
    }
}
