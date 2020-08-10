package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipWhile */
public final class ObservableSkipWhile<T> extends AbstractObservableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipWhile$SkipWhileObserver */
    static final class SkipWhileObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean notSkipping;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f356s;

        SkipWhileObserver(Observer<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f356s, s)) {
                this.f356s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f356s.dispose();
        }

        public boolean isDisposed() {
            return this.f356s.isDisposed();
        }

        public void onNext(T t) {
            if (this.notSkipping) {
                this.actual.onNext(t);
            } else {
                try {
                    if (!this.predicate.test(t)) {
                        this.notSkipping = true;
                        this.actual.onNext(t);
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f356s.dispose();
                    this.actual.onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableSkipWhile(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new SkipWhileObserver(s, this.predicate));
    }
}
