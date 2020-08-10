package p005io.reactivex.subjects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableObserver;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.subjects.CompletableSubject */
public final class CompletableSubject extends Completable implements CompletableObserver {
    static final CompletableDisposable[] EMPTY = new CompletableDisposable[0];
    static final CompletableDisposable[] TERMINATED = new CompletableDisposable[0];
    Throwable error;
    final AtomicReference<CompletableDisposable[]> observers = new AtomicReference<>(EMPTY);
    final AtomicBoolean once = new AtomicBoolean();

    /* renamed from: io.reactivex.subjects.CompletableSubject$CompletableDisposable */
    static final class CompletableDisposable extends AtomicReference<CompletableSubject> implements Disposable {
        private static final long serialVersionUID = -7650903191002190468L;
        final CompletableObserver actual;

        CompletableDisposable(CompletableObserver actual2, CompletableSubject parent) {
            this.actual = actual2;
            lazySet(parent);
        }

        public void dispose() {
            CompletableSubject parent = (CompletableSubject) getAndSet(null);
            if (parent != null) {
                parent.remove(this);
            }
        }

        public boolean isDisposed() {
            return get() == null;
        }
    }

    @CheckReturnValue
    @NonNull
    public static CompletableSubject create() {
        return new CompletableSubject();
    }

    CompletableSubject() {
    }

    public void onSubscribe(Disposable d) {
        if (this.observers.get() == TERMINATED) {
            d.dispose();
        }
    }

    public void onError(Throwable e) {
        ObjectHelper.requireNonNull(e, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.once.compareAndSet(false, true)) {
            this.error = e;
            for (CompletableDisposable md : (CompletableDisposable[]) this.observers.getAndSet(TERMINATED)) {
                md.actual.onError(e);
            }
            return;
        }
        RxJavaPlugins.onError(e);
    }

    public void onComplete() {
        if (this.once.compareAndSet(false, true)) {
            for (CompletableDisposable md : (CompletableDisposable[]) this.observers.getAndSet(TERMINATED)) {
                md.actual.onComplete();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        CompletableDisposable md = new CompletableDisposable(observer, this);
        observer.onSubscribe(md);
        if (!add(md)) {
            Throwable ex = this.error;
            if (ex != null) {
                observer.onError(ex);
            } else {
                observer.onComplete();
            }
        } else if (md.isDisposed()) {
            remove(md);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(CompletableDisposable inner) {
        CompletableDisposable[] a;
        CompletableDisposable[] b;
        do {
            a = (CompletableDisposable[]) this.observers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new CompletableDisposable[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(CompletableDisposable inner) {
        CompletableDisposable[] a;
        CompletableDisposable[] b;
        do {
            a = (CompletableDisposable[]) this.observers.get();
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
                        CompletableDisposable[] b2 = new CompletableDisposable[(n - 1)];
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

    @Nullable
    public Throwable getThrowable() {
        if (this.observers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    public boolean hasThrowable() {
        return this.observers.get() == TERMINATED && this.error != null;
    }

    public boolean hasComplete() {
        return this.observers.get() == TERMINATED && this.error == null;
    }

    public boolean hasObservers() {
        return ((CompletableDisposable[]) this.observers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public int observerCount() {
        return ((CompletableDisposable[]) this.observers.get()).length;
    }
}
