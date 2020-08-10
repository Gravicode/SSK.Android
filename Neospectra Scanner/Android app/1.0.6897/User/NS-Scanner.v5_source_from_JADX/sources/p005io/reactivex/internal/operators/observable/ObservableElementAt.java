package p005io.reactivex.internal.operators.observable;

import java.util.NoSuchElementException;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableElementAt */
public final class ObservableElementAt<T> extends AbstractObservableWithUpstream<T, T> {
    final T defaultValue;
    final boolean errorOnFewer;
    final long index;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableElementAt$ElementAtObserver */
    static final class ElementAtObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        long count;
        final T defaultValue;
        boolean done;
        final boolean errorOnFewer;
        final long index;

        /* renamed from: s */
        Disposable f305s;

        ElementAtObserver(Observer<? super T> actual2, long index2, T defaultValue2, boolean errorOnFewer2) {
            this.actual = actual2;
            this.index = index2;
            this.defaultValue = defaultValue2;
            this.errorOnFewer = errorOnFewer2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f305s, s)) {
                this.f305s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f305s.dispose();
        }

        public boolean isDisposed() {
            return this.f305s.isDisposed();
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.f305s.dispose();
                    this.actual.onNext(t);
                    this.actual.onComplete();
                    return;
                }
                this.count = 1 + c;
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
                T v = this.defaultValue;
                if (v != null || !this.errorOnFewer) {
                    if (v != null) {
                        this.actual.onNext(v);
                    }
                    this.actual.onComplete();
                    return;
                }
                this.actual.onError(new NoSuchElementException());
            }
        }
    }

    public ObservableElementAt(ObservableSource<T> source, long index2, T defaultValue2, boolean errorOnFewer2) {
        super(source);
        this.index = index2;
        this.defaultValue = defaultValue2;
        this.errorOnFewer = errorOnFewer2;
    }

    public void subscribeActual(Observer<? super T> t) {
        ObservableSource observableSource = this.source;
        ElementAtObserver elementAtObserver = new ElementAtObserver(t, this.index, this.defaultValue, this.errorOnFewer);
        observableSource.subscribe(elementAtObserver);
    }
}
