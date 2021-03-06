package p005io.reactivex.internal.operators.single;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.single.SingleCache */
public final class SingleCache<T> extends Single<T> implements SingleObserver<T> {
    static final CacheDisposable[] EMPTY = new CacheDisposable[0];
    static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
    Throwable error;
    final AtomicReference<CacheDisposable<T>[]> observers = new AtomicReference<>(EMPTY);
    final SingleSource<? extends T> source;
    T value;
    final AtomicInteger wip = new AtomicInteger();

    /* renamed from: io.reactivex.internal.operators.single.SingleCache$CacheDisposable */
    static final class CacheDisposable<T> extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 7514387411091976596L;
        final SingleObserver<? super T> actual;
        final SingleCache<T> parent;

        CacheDisposable(SingleObserver<? super T> actual2, SingleCache<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public boolean isDisposed() {
            return get();
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }
    }

    public SingleCache(SingleSource<? extends T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        CacheDisposable<T> d = new CacheDisposable<>(s, this);
        s.onSubscribe(d);
        if (add(d)) {
            if (d.isDisposed()) {
                remove(d);
            }
            if (this.wip.getAndIncrement() == 0) {
                this.source.subscribe(this);
            }
            return;
        }
        Throwable ex = this.error;
        if (ex != null) {
            s.onError(ex);
        } else {
            s.onSuccess(this.value);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(CacheDisposable<T> observer) {
        CacheDisposable<T>[] a;
        CacheDisposable<T>[] b;
        do {
            a = (CacheDisposable[]) this.observers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new CacheDisposable[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = observer;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(CacheDisposable<T> observer) {
        CacheDisposable<T>[] a;
        CacheDisposable<T>[] b;
        do {
            a = (CacheDisposable[]) this.observers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == observer) {
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
                        CacheDisposable<T>[] b2 = new CacheDisposable[(n - 1)];
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

    public void onSubscribe(Disposable d) {
    }

    public void onSuccess(T value2) {
        CacheDisposable<T>[] cacheDisposableArr;
        this.value = value2;
        for (CacheDisposable<T> d : (CacheDisposable[]) this.observers.getAndSet(TERMINATED)) {
            if (!d.isDisposed()) {
                d.actual.onSuccess(value2);
            }
        }
    }

    public void onError(Throwable e) {
        CacheDisposable<T>[] cacheDisposableArr;
        this.error = e;
        for (CacheDisposable<T> d : (CacheDisposable[]) this.observers.getAndSet(TERMINATED)) {
            if (!d.isDisposed()) {
                d.actual.onError(e);
            }
        }
    }
}
