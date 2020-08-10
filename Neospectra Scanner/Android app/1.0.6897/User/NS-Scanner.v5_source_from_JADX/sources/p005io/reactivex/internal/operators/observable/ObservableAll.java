package p005io.reactivex.internal.operators.observable;

import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableAll */
public final class ObservableAll<T> extends AbstractObservableWithUpstream<T, Boolean> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableAll$AllObserver */
    static final class AllObserver<T> implements Observer<T>, Disposable {
        final Observer<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Disposable f272s;

        AllObserver(Observer<? super Boolean> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f272s, s)) {
                this.f272s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (!this.predicate.test(t)) {
                        this.done = true;
                        this.f272s.dispose();
                        this.actual.onNext(Boolean.valueOf(false));
                        this.actual.onComplete();
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f272s.dispose();
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
                this.actual.onNext(Boolean.valueOf(true));
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.f272s.dispose();
        }

        public boolean isDisposed() {
            return this.f272s.isDisposed();
        }
    }

    public ObservableAll(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Boolean> t) {
        this.source.subscribe(new AllObserver(t, this.predicate));
    }
}
