package p005io.reactivex.internal.operators.completable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableConcatIterable */
public final class CompletableConcatIterable extends Completable {
    final Iterable<? extends CompletableSource> sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableConcatIterable$ConcatInnerObserver */
    static final class ConcatInnerObserver extends AtomicInteger implements CompletableObserver {
        private static final long serialVersionUID = -7965400327305809232L;
        final CompletableObserver actual;

        /* renamed from: sd */
        final SequentialDisposable f98sd = new SequentialDisposable();
        final Iterator<? extends CompletableSource> sources;

        ConcatInnerObserver(CompletableObserver actual2, Iterator<? extends CompletableSource> sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void onSubscribe(Disposable d) {
            this.f98sd.replace(d);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            next();
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            if (!this.f98sd.isDisposed() && getAndIncrement() == 0) {
                Iterator<? extends CompletableSource> a = this.sources;
                while (!this.f98sd.isDisposed()) {
                    try {
                        if (!a.hasNext()) {
                            this.actual.onComplete();
                            return;
                        }
                        try {
                            ((CompletableSource) ObjectHelper.requireNonNull(a.next(), "The CompletableSource returned is null")).subscribe(this);
                            if (decrementAndGet() == 0) {
                                return;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            this.actual.onError(ex);
                            return;
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(ex2);
                        this.actual.onError(ex2);
                        return;
                    }
                }
            }
        }
    }

    public CompletableConcatIterable(Iterable<? extends CompletableSource> sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        try {
            ConcatInnerObserver inner = new ConcatInnerObserver(s, (Iterator) ObjectHelper.requireNonNull(this.sources.iterator(), "The iterator returned is null"));
            s.onSubscribe(inner.f98sd);
            inner.next();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, s);
        }
    }
}
