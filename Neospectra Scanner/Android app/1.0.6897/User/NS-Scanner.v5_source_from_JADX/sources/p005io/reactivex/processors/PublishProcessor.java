package p005io.reactivex.processors;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.processors.PublishProcessor */
public final class PublishProcessor<T> extends FlowableProcessor<T> {
    static final PublishSubscription[] EMPTY = new PublishSubscription[0];
    static final PublishSubscription[] TERMINATED = new PublishSubscription[0];
    Throwable error;
    final AtomicReference<PublishSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

    /* renamed from: io.reactivex.processors.PublishProcessor$PublishSubscription */
    static final class PublishSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 3562861878281475070L;
        final Subscriber<? super T> actual;
        final PublishProcessor<T> parent;

        PublishSubscription(Subscriber<? super T> actual2, PublishProcessor<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void onNext(T t) {
            long r = get();
            if (r != Long.MIN_VALUE) {
                if (r != 0) {
                    this.actual.onNext(t);
                    BackpressureHelper.producedCancel(this, 1);
                } else {
                    cancel();
                    this.actual.onError(new MissingBackpressureException("Could not emit value due to lack of requests"));
                }
            }
        }

        public void onError(Throwable t) {
            if (get() != Long.MIN_VALUE) {
                this.actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (get() != Long.MIN_VALUE) {
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }

        /* access modifiers changed from: 0000 */
        public boolean isFull() {
            return get() == 0;
        }
    }

    @CheckReturnValue
    @NonNull
    public static <T> PublishProcessor<T> create() {
        return new PublishProcessor<>();
    }

    PublishProcessor() {
    }

    public void subscribeActual(Subscriber<? super T> t) {
        PublishSubscription<T> ps = new PublishSubscription<>(t, this);
        t.onSubscribe(ps);
        if (!add(ps)) {
            Throwable ex = this.error;
            if (ex != null) {
                t.onError(ex);
            } else {
                t.onComplete();
            }
        } else if (ps.isCancelled()) {
            remove(ps);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(PublishSubscription<T> ps) {
        PublishSubscription<T>[] a;
        PublishSubscription<T>[] b;
        do {
            a = (PublishSubscription[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new PublishSubscription[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = ps;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(PublishSubscription<T> ps) {
        PublishSubscription<T>[] a;
        PublishSubscription<T>[] b;
        do {
            a = (PublishSubscription[]) this.subscribers.get();
            if (a != TERMINATED && a != EMPTY) {
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
                        PublishSubscription<T>[] b2 = new PublishSubscription[(n - 1)];
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

    public void onSubscribe(Subscription s) {
        if (this.subscribers.get() == TERMINATED) {
            s.cancel();
        } else {
            s.request(Long.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        for (PublishSubscription<T> s : (PublishSubscription[]) this.subscribers.get()) {
            s.onNext(t);
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.subscribers.get() == TERMINATED) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.error = t;
        for (PublishSubscription<T> s : (PublishSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            s.onError(t);
        }
    }

    public void onComplete() {
        if (this.subscribers.get() != TERMINATED) {
            for (PublishSubscription<T> s : (PublishSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                s.onComplete();
            }
        }
    }

    @Experimental
    public boolean offer(T t) {
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return true;
        }
        PublishSubscription<T>[] array = (PublishSubscription[]) this.subscribers.get();
        for (PublishSubscription<T> s : array) {
            if (s.isFull()) {
                return false;
            }
        }
        for (PublishSubscription<T> s2 : array) {
            s2.onNext(t);
        }
        return true;
    }

    public boolean hasSubscribers() {
        return ((PublishSubscription[]) this.subscribers.get()).length != 0;
    }

    @Nullable
    public Throwable getThrowable() {
        if (this.subscribers.get() == TERMINATED) {
            return this.error;
        }
        return null;
    }

    public boolean hasThrowable() {
        return this.subscribers.get() == TERMINATED && this.error != null;
    }

    public boolean hasComplete() {
        return this.subscribers.get() == TERMINATED && this.error == null;
    }
}
