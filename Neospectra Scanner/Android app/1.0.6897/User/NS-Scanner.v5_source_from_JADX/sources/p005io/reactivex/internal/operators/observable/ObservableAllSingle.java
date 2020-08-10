package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableAllSingle */
public final class ObservableAllSingle<T> extends Single<Boolean> implements FuseToObservable<Boolean> {
    final Predicate<? super T> predicate;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableAllSingle$AllObserver */
    static final class AllObserver<T> implements Observer<T>, Disposable {
        final SingleObserver<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f273s;

        AllObserver(SingleObserver<? super Boolean> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f273s, s)) {
                this.f273s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (!this.predicate.test(t)) {
                        this.done = true;
                        this.f273s.dispose();
                        this.actual.onSuccess(Boolean.valueOf(false));
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f273s.dispose();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onSuccess(Boolean.valueOf(true));
            }
        }

        public void dispose() {
            this.f273s.dispose();
        }

        public boolean isDisposed() {
            return this.f273s.isDisposed();
        }
    }

    public ObservableAllSingle(ObservableSource<T> source2, Predicate<? super T> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> t) {
        this.source.subscribe(new AllObserver(t, this.predicate));
    }

    public Observable<Boolean> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableAll<T>(this.source, this.predicate));
    }
}
