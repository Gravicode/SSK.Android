package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray */
public final class CompletableMergeDelayErrorArray extends Completable {
    final CompletableSource[] sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray$MergeInnerCompletableObserver */
    static final class MergeInnerCompletableObserver implements CompletableObserver {
        final CompletableObserver actual;
        final AtomicThrowable error;
        final CompositeDisposable set;
        final AtomicInteger wip;

        MergeInnerCompletableObserver(CompletableObserver s, CompositeDisposable set2, AtomicThrowable error2, AtomicInteger wip2) {
            this.actual = s;
            this.set = set2;
            this.error = error2;
            this.wip = wip2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onError(Throwable e) {
            if (this.error.addThrowable(e)) {
                tryTerminate();
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            tryTerminate();
        }

        /* access modifiers changed from: 0000 */
        public void tryTerminate() {
            if (this.wip.decrementAndGet() == 0) {
                Throwable ex = this.error.terminate();
                if (ex == null) {
                    this.actual.onComplete();
                } else {
                    this.actual.onError(ex);
                }
            }
        }
    }

    public CompletableMergeDelayErrorArray(CompletableSource[] sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        CompositeDisposable set = new CompositeDisposable();
        AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
        AtomicThrowable error = new AtomicThrowable();
        s.onSubscribe(set);
        CompletableSource[] completableSourceArr = this.sources;
        int length = completableSourceArr.length;
        int i = 0;
        while (i < length) {
            CompletableSource c = completableSourceArr[i];
            if (!set.isDisposed()) {
                if (c == null) {
                    error.addThrowable(new NullPointerException("A completable source is null"));
                    wip.decrementAndGet();
                } else {
                    c.subscribe(new MergeInnerCompletableObserver(s, set, error, wip));
                }
                i++;
            } else {
                return;
            }
        }
        if (wip.decrementAndGet() == 0) {
            Throwable ex = error.terminate();
            if (ex == null) {
                s.onComplete();
            } else {
                s.onError(ex);
            }
        }
    }
}
