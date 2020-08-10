package p005io.reactivex.internal.operators.observable;

import java.util.Collection;
import java.util.concurrent.Callable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableToList */
public final class ObservableToList<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableToList$ToListObserver */
    static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final Observer<? super U> actual;
        U collection;

        /* renamed from: s */
        Disposable f367s;

        ToListObserver(Observer<? super U> actual2, U collection2) {
            this.actual = actual2;
            this.collection = collection2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.f367s, s)) {
                this.f367s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f367s.dispose();
        }

        public boolean isDisposed() {
            return this.f367s.isDisposed();
        }

        public void onNext(T t) {
            this.collection.add(t);
        }

        public void onError(Throwable t) {
            this.collection = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            U c = this.collection;
            this.collection = null;
            this.actual.onNext(c);
            this.actual.onComplete();
        }
    }

    public ObservableToList(ObservableSource<T> source, int defaultCapacityHint) {
        super(source);
        this.collectionSupplier = Functions.createArrayList(defaultCapacityHint);
    }

    public ObservableToList(ObservableSource<T> source, Callable<U> collectionSupplier2) {
        super(source);
        this.collectionSupplier = collectionSupplier2;
    }

    public void subscribeActual(Observer<? super U> t) {
        try {
            this.source.subscribe(new ToListObserver(t, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, t);
        }
    }
}
