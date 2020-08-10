package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableConcatArray */
public final class CompletableConcatArray extends Completable {
    final CompletableSource[] sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableConcatArray$ConcatInnerObserver */
    static final class ConcatInnerObserver extends AtomicInteger implements CompletableObserver {
        private static final long serialVersionUID = -7965400327305809232L;
        final CompletableObserver actual;
        int index;

        /* renamed from: sd */
        final SequentialDisposable f97sd = new SequentialDisposable();
        final CompletableSource[] sources;

        ConcatInnerObserver(CompletableObserver actual2, CompletableSource[] sources2) {
            this.actual = actual2;
            this.sources = sources2;
        }

        public void onSubscribe(Disposable d) {
            this.f97sd.replace(d);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            next();
        }

        /* access modifiers changed from: 0000 */
        public void next() {
            if (!this.f97sd.isDisposed() && getAndIncrement() == 0) {
                CompletableSource[] a = this.sources;
                while (!this.f97sd.isDisposed()) {
                    int idx = this.index;
                    this.index = idx + 1;
                    if (idx == a.length) {
                        this.actual.onComplete();
                        return;
                    }
                    a[idx].subscribe(this);
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }
    }

    public CompletableConcatArray(CompletableSource[] sources2) {
        this.sources = sources2;
    }

    public void subscribeActual(CompletableObserver s) {
        ConcatInnerObserver inner = new ConcatInnerObserver(s, this.sources);
        s.onSubscribe(inner.f97sd);
        inner.next();
    }
}
