package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate */
public final class ObservableTakeUntilPredicate<T> extends AbstractObservableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate$TakeUntilPredicateObserver */
    static final class TakeUntilPredicateObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f363s;

        TakeUntilPredicateObserver(Observer<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f363s, s)) {
                this.f363s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f363s.dispose();
        }

        public boolean isDisposed() {
            return this.f363s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                this.actual.onNext(t);
                try {
                    if (this.predicate.test(t)) {
                        this.done = true;
                        this.f363s.dispose();
                        this.actual.onComplete();
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f363s.dispose();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (!this.done) {
                this.done = true;
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
            }
        }
    }

    public ObservableTakeUntilPredicate(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new TakeUntilPredicateObserver(s, this.predicate));
    }
}
