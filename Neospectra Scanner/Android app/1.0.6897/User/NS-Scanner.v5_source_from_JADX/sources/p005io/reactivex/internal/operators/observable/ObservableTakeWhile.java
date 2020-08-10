package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeWhile */
public final class ObservableTakeWhile<T> extends AbstractObservableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeWhile$TakeWhileObserver */
    static final class TakeWhileObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f364s;

        TakeWhileObserver(Observer<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f364s, s)) {
                this.f364s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f364s.dispose();
        }

        public boolean isDisposed() {
            return this.f364s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (!this.predicate.test(t)) {
                        this.done = true;
                        this.f364s.dispose();
                        this.actual.onComplete();
                        return;
                    }
                    this.actual.onNext(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f364s.dispose();
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
                this.actual.onComplete();
            }
        }
    }

    public ObservableTakeWhile(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new TakeWhileObserver(t, this.predicate));
    }
}
