package p005io.reactivex.internal.operators.completable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMergeIterable */
public final class CompletableMergeIterable extends Completable {
    final Iterable<? extends CompletableSource> sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableMergeIterable$MergeCompletableObserver */
    static final class MergeCompletableObserver extends AtomicBoolean implements CompletableObserver {
        private static final long serialVersionUID = -7730517613164279224L;
        final CompletableObserver actual;
        final CompositeDisposable set;
        final AtomicInteger wip;

        MergeCompletableObserver(CompletableObserver actual2, CompositeDisposable set2, AtomicInteger wip2) {
            this.actual = actual2;
            this.set = set2;
            this.wip = wip2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onError(Throwable e) {
            this.set.dispose();
            if (compareAndSet(false, true)) {
                this.actual.onError(e);
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            if (this.wip.decrementAndGet() == 0 && compareAndSet(false, true)) {
                this.actual.onComplete();
            }
        }
    }

    public CompletableMergeIterable(Iterable<? extends CompletableSource> sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompositeDisposable set = new CompositeDisposable();
        s.onSubscribe(set);
        try {
            Iterator<? extends CompletableSource> iterator = (Iterator) ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
            AtomicInteger wip = new AtomicInteger(1);
            MergeCompletableObserver shared = new MergeCompletableObserver(s, set, wip);
            while (!set.isDisposed()) {
                try {
                    if (!iterator.hasNext()) {
                        shared.onComplete();
                        return;
                    } else if (!set.isDisposed()) {
                        try {
                            CompletableSource c = (CompletableSource) ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null CompletableSource");
                            if (!set.isDisposed()) {
                                wip.getAndIncrement();
                                c.subscribe(shared);
                            } else {
                                return;
                            }
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            set.dispose();
                            shared.onError(e);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    set.dispose();
                    shared.onError(e2);
                    return;
                }
            }
        } catch (Throwable e3) {
            Exceptions.throwIfFatal(e3);
            s.onError(e3);
        }
    }
}
