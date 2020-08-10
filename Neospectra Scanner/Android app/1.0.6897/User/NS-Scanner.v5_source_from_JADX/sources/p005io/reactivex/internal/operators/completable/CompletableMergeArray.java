package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMergeArray */
public final class CompletableMergeArray extends Completable {
    final CompletableSource[] sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableMergeArray$InnerCompletableObserver */
    static final class InnerCompletableObserver extends AtomicInteger implements CompletableObserver {
        private static final long serialVersionUID = -8360547806504310570L;
        final CompletableObserver actual;
        final AtomicBoolean once;
        final CompositeDisposable set;

        InnerCompletableObserver(CompletableObserver actual2, AtomicBoolean once2, CompositeDisposable set2, int n) {
            this.actual = actual2;
            this.once = once2;
            this.set = set2;
            lazySet(n);
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onError(Throwable e) {
            this.set.dispose();
            if (this.once.compareAndSet(false, true)) {
                this.actual.onError(e);
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            if (decrementAndGet() == 0 && this.once.compareAndSet(false, true)) {
                this.actual.onComplete();
            }
        }
    }

    public CompletableMergeArray(CompletableSource[] sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompositeDisposable set = new CompositeDisposable();
        InnerCompletableObserver shared = new InnerCompletableObserver(s, new AtomicBoolean(), set, this.sources.length + 1);
        s.onSubscribe(set);
        CompletableSource[] completableSourceArr = this.sources;
        int length = completableSourceArr.length;
        int i = 0;
        while (i < length) {
            CompletableSource c = completableSourceArr[i];
            if (!set.isDisposed()) {
                if (c == null) {
                    set.dispose();
                    shared.onError(new NullPointerException("A completable source is null"));
                    return;
                }
                c.subscribe(shared);
                i++;
            } else {
                return;
            }
        }
        shared.onComplete();
    }
}
