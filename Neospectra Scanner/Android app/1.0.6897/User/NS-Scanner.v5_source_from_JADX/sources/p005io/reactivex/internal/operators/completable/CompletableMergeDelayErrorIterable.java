package p005io.reactivex.internal.operators.completable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.util.AtomicThrowable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMergeDelayErrorIterable */
public final class CompletableMergeDelayErrorIterable extends Completable {
    final Iterable<? extends CompletableSource> sources;

    public CompletableMergeDelayErrorIterable(Iterable<? extends CompletableSource> sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        try {
            Iterator<? extends CompletableSource> iterator = (Iterator) ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
            AtomicInteger wip = new AtomicInteger(1);
            AtomicThrowable error = new AtomicThrowable();
            while (!set.isDisposed()) {
                try {
                    if (!iterator.hasNext()) {
                        if (wip.decrementAndGet() == 0) {
                            Throwable ex = error.terminate();
                            if (ex == null) {
                                s.onComplete();
                            } else {
                                s.onError(ex);
                            }
                        }
                        return;
                    } else if (!set.isDisposed()) {
                        try {
                            CompletableSource c = (CompletableSource) ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null CompletableSource");
                            if (!set.isDisposed()) {
                                wip.getAndIncrement();
                                c.subscribe(new MergeInnerCompletableObserver(s, set, error, wip));
                            } else {
                                return;
                            }
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            error.addThrowable(e);
                        }
                    } else {
                        return;
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    error.addThrowable(e2);
                }
            }
        } catch (Throwable e3) {
            Exceptions.throwIfFatal(e3);
            s.onError(e3);
        }
    }
}
