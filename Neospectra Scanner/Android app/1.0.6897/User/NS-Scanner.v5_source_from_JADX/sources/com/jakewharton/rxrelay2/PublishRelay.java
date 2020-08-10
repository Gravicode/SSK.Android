package com.jakewharton.rxrelay2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;

public final class PublishRelay<T> extends Relay<T> {
    private static final PublishDisposable[] EMPTY = new PublishDisposable[0];
    private final AtomicReference<PublishDisposable<T>[]> subscribers = new AtomicReference<>(EMPTY);

    static final class PublishDisposable<T> extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 3562861878281475070L;
        final Observer<? super T> actual;
        final PublishRelay<T> parent;

        PublishDisposable(Observer<? super T> actual2, PublishRelay<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        /* access modifiers changed from: 0000 */
        public void onNext(T t) {
            if (!get()) {
                this.actual.onNext(t);
            }
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }

        public boolean isDisposed() {
            return get();
        }
    }

    public static <T> PublishRelay<T> create() {
        return new PublishRelay<>();
    }

    private PublishRelay() {
    }

    public void subscribeActual(Observer<? super T> t) {
        PublishDisposable<T> ps = new PublishDisposable<>(t, this);
        t.onSubscribe(ps);
        add(ps);
        if (ps.isDisposed()) {
            remove(ps);
        }
    }

    private void add(PublishDisposable<T> ps) {
        PublishDisposable<T>[] a;
        PublishDisposable<T>[] b;
        do {
            a = (PublishDisposable[]) this.subscribers.get();
            int n = a.length;
            b = new PublishDisposable[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;
        } while (!this.subscribers.compareAndSet(a, b));
    }

    /* access modifiers changed from: 0000 */
    public void remove(PublishDisposable<T> ps) {
        PublishDisposable<T>[] a;
        PublishDisposable<T>[] b;
        do {
            a = (PublishDisposable[]) this.subscribers.get();
            if (a != EMPTY) {
                int n = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == ps) {
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
                        PublishDisposable<T>[] b2 = new PublishDisposable[(n - 1)];
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
        } while (!this.subscribers.compareAndSet(a, b));
    }

    public void accept(T value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        for (PublishDisposable<T> s : (PublishDisposable[]) this.subscribers.get()) {
            s.onNext(value);
        }
    }

    public boolean hasObservers() {
        return ((PublishDisposable[]) this.subscribers.get()).length != 0;
    }
}
