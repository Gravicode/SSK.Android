package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableAny */
public final class ObservableAny<T> extends AbstractObservableWithUpstream<T, Boolean> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableAny$AnyObserver */
    static final class AnyObserver<T> implements Observer<T>, Disposable {
        final Observer<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f274s;

        AnyObserver(Observer<? super Boolean> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f274s, s)) {
                this.f274s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (this.predicate.test(t)) {
                        this.done = true;
                        this.f274s.dispose();
                        this.actual.onNext(Boolean.valueOf(true));
                        this.actual.onComplete();
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f274s.dispose();
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
                this.actual.onNext(Boolean.valueOf(false));
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.f274s.dispose();
        }

        public boolean isDisposed() {
            return this.f274s.isDisposed();
        }
    }

    public ObservableAny(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Boolean> t) {
        this.source.subscribe(new AnyObserver(t, this.predicate));
    }
}
