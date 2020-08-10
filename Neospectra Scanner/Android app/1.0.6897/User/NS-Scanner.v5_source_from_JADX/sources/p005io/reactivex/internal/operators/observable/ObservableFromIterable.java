package p005io.reactivex.internal.operators.observable;

import java.util.Iterator;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.observers.BasicQueueDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromIterable */
public final class ObservableFromIterable<T> extends Observable<T> {
    final Iterable<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFromIterable$FromIterableDisposable */
    static final class FromIterableDisposable<T> extends BasicQueueDisposable<T> {
        final Observer<? super T> actual;
        boolean checkNext;
        volatile boolean disposed;
        boolean done;
        boolean fusionMode;

        /* renamed from: it */
        final Iterator<? extends T> f315it;

        FromIterableDisposable(Observer<? super T> actual2, Iterator<? extends T> it) {
            this.actual = actual2;
            this.f315it = it;
        }

        /* access modifiers changed from: 0000 */
        public void run() {
            while (!isDisposed()) {
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.f315it.next(), "The iterator returned a null value"));
                    if (!isDisposed()) {
                        try {
                            if (!this.f315it.hasNext()) {
                                if (!isDisposed()) {
                                    this.actual.onComplete();
                                }
                                return;
                            }
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            this.actual.onError(e);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    this.actual.onError(e2);
                    return;
                }
            }
        }

        public int requestFusion(int mode) {
            if ((mode & 1) == 0) {
                return 0;
            }
            this.fusionMode = true;
            return 1;
        }

        @Nullable
        public T poll() {
            if (this.done) {
                return null;
            }
            if (!this.checkNext) {
                this.checkNext = true;
            } else if (!this.f315it.hasNext()) {
                this.done = true;
                return null;
            }
            return ObjectHelper.requireNonNull(this.f315it.next(), "The iterator returned a null value");
        }

        public boolean isEmpty() {
            return this.done;
        }

        public void clear() {
            this.done = true;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    public ObservableFromIterable(Iterable<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            Iterator<? extends T> it = this.source.iterator();
            try {
                if (!it.hasNext()) {
                    EmptyDisposable.complete(s);
                    return;
                }
                FromIterableDisposable<T> d = new FromIterableDisposable<>(s, it);
                s.onSubscribe(d);
                if (!d.fusionMode) {
                    d.run();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, s);
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            EmptyDisposable.error(e2, s);
        }
    }
}
