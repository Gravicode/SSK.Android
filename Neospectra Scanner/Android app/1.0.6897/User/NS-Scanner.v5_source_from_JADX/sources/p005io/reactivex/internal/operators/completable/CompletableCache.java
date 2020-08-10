package p005io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;

@Experimental
/* renamed from: io.reactivex.internal.operators.completable.CompletableCache */
public final class CompletableCache extends Completable implements CompletableObserver {
    static final InnerCompletableCache[] EMPTY = new InnerCompletableCache[0];
    static final InnerCompletableCache[] TERMINATED = new InnerCompletableCache[0];
    Throwable error;
    final AtomicReference<InnerCompletableCache[]> observers = new AtomicReference<>(EMPTY);
    final AtomicBoolean once = new AtomicBoolean();
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableCache$InnerCompletableCache */
    final class InnerCompletableCache extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 8943152917179642732L;
        final CompletableObserver actual;

        InnerCompletableCache(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public boolean isDisposed() {
            return get();
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                CompletableCache.this.remove(this);
            }
        }
    }

    public CompletableCache(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        InnerCompletableCache inner = new InnerCompletableCache(s);
        s.onSubscribe(inner);
        if (add(inner)) {
            if (inner.isDisposed()) {
                remove(inner);
            }
            if (this.once.compareAndSet(false, true)) {
                this.source.subscribe(this);
                return;
            }
            return;
        }
        Throwable ex = this.error;
        if (ex != null) {
            s.onError(ex);
        } else {
            s.onComplete();
        }
    }

    public void onSubscribe(Disposable d) {
    }

    public void onError(Throwable e) {
        InnerCompletableCache[] innerCompletableCacheArr;
        this.error = e;
        for (InnerCompletableCache inner : (InnerCompletableCache[]) this.observers.getAndSet(TERMINATED)) {
            if (!inner.get()) {
                inner.actual.onError(e);
            }
        }
    }

    public void onComplete() {
        InnerCompletableCache[] innerCompletableCacheArr;
        for (InnerCompletableCache inner : (InnerCompletableCache[]) this.observers.getAndSet(TERMINATED)) {
            if (!inner.get()) {
                inner.actual.onComplete();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(InnerCompletableCache inner) {
        InnerCompletableCache[] a;
        InnerCompletableCache[] b;
        do {
            a = (InnerCompletableCache[]) this.observers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new InnerCompletableCache[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(InnerCompletableCache inner) {
        InnerCompletableCache[] a;
        InnerCompletableCache[] b;
        do {
            a = (InnerCompletableCache[]) this.observers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == inner) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        b = EMPTY;
                    } else {
                        InnerCompletableCache[] b2 = new InnerCompletableCache[(n - 1)];
                        System.arraycopy(a, 0, b2, 0, j);
                        System.arraycopy(a, j + 1, b2, j, (n - j) - 1);
                        b = b2;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } while (!this.observers.compareAndSet(a, b));
    }
}
