package p005io.reactivex.subjects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observer;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.internal.disposables.EmptyDisposable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.observers.BasicIntQueueDisposable;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.subjects.UnicastSubject */
public final class UnicastSubject<T> extends Subject<T> {
    final AtomicReference<Observer<? super T>> actual;
    final boolean delayError;
    volatile boolean disposed;
    volatile boolean done;
    boolean enableOperatorFusion;
    Throwable error;
    final AtomicReference<Runnable> onTerminate;
    final AtomicBoolean once;
    final SpscLinkedArrayQueue<T> queue;
    final BasicIntQueueDisposable<T> wip;

    /* renamed from: io.reactivex.subjects.UnicastSubject$UnicastQueueDisposable */
    final class UnicastQueueDisposable extends BasicIntQueueDisposable<T> {
        private static final long serialVersionUID = 7926949470189395511L;

        UnicastQueueDisposable() {
        }

        public int requestFusion(int mode) {
            if ((mode & 2) == 0) {
                return 0;
            }
            UnicastSubject.this.enableOperatorFusion = true;
            return 2;
        }

        @Nullable
        public T poll() throws Exception {
            return UnicastSubject.this.queue.poll();
        }

        public boolean isEmpty() {
            return UnicastSubject.this.queue.isEmpty();
        }

        public void clear() {
            UnicastSubject.this.queue.clear();
        }

        public void dispose() {
            if (!UnicastSubject.this.disposed) {
                UnicastSubject.this.disposed = true;
                UnicastSubject.this.doTerminate();
                UnicastSubject.this.actual.lazySet(null);
                if (UnicastSubject.this.wip.getAndIncrement() == 0) {
                    UnicastSubject.this.actual.lazySet(null);
                    UnicastSubject.this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return UnicastSubject.this.disposed;
        }
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastSubject<T> create() {
        return new UnicastSubject<>(bufferSize(), true);
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastSubject<T> create(int capacityHint) {
        return new UnicastSubject<>(capacityHint, true);
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastSubject<T> create(int capacityHint, Runnable onTerminate2) {
        return new UnicastSubject<>(capacityHint, onTerminate2, true);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public static <T> UnicastSubject<T> create(int capacityHint, Runnable onTerminate2, boolean delayError2) {
        return new UnicastSubject<>(capacityHint, onTerminate2, delayError2);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public static <T> UnicastSubject<T> create(boolean delayError2) {
        return new UnicastSubject<>(bufferSize(), delayError2);
    }

    UnicastSubject(int capacityHint, boolean delayError2) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        this.onTerminate = new AtomicReference<>();
        this.delayError = delayError2;
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    UnicastSubject(int capacityHint, Runnable onTerminate2) {
        this(capacityHint, onTerminate2, true);
    }

    UnicastSubject(int capacityHint, Runnable onTerminate2, boolean delayError2) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        this.onTerminate = new AtomicReference<>(ObjectHelper.requireNonNull(onTerminate2, "onTerminate"));
        this.delayError = delayError2;
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueDisposable();
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            EmptyDisposable.error((Throwable) new IllegalStateException("Only a single observer allowed."), observer);
        } else {
            observer.onSubscribe(this.wip);
            this.actual.lazySet(observer);
            if (this.disposed) {
                this.actual.lazySet(null);
                return;
            }
            drain();
        }
    }

    /* access modifiers changed from: 0000 */
    public void doTerminate() {
        Runnable r = (Runnable) this.onTerminate.get();
        if (r != null && this.onTerminate.compareAndSet(r, null)) {
            r.run();
        }
    }

    public void onSubscribe(Disposable s) {
        if (this.done || this.disposed) {
            s.dispose();
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done && !this.disposed) {
            this.queue.offer(t);
            drain();
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.done || this.disposed) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.error = t;
        this.done = true;
        doTerminate();
        drain();
    }

    public void onComplete() {
        if (!this.done && !this.disposed) {
            this.done = true;
            doTerminate();
            drain();
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainNormal(Observer<? super T> a) {
        SimpleQueue<T> q = this.queue;
        boolean failFast = !this.delayError;
        int missed = 1;
        boolean canBeError = true;
        while (!this.disposed) {
            boolean d = this.done;
            T v = this.queue.poll();
            boolean empty = v == null;
            if (d) {
                if (failFast && canBeError) {
                    if (!failedFast(q, a)) {
                        canBeError = false;
                    } else {
                        return;
                    }
                }
                if (empty) {
                    errorOrComplete(a);
                    return;
                }
            }
            if (empty) {
                missed = this.wip.addAndGet(-missed);
                if (missed == 0) {
                    return;
                }
            } else {
                a.onNext(v);
            }
        }
        this.actual.lazySet(null);
        q.clear();
    }

    /* access modifiers changed from: 0000 */
    public void drainFused(Observer<? super T> a) {
        int missed = 1;
        SpscLinkedArrayQueue<T> q = this.queue;
        boolean failFast = !this.delayError;
        while (!this.disposed) {
            boolean d = this.done;
            if (!failFast || !d || !failedFast(q, a)) {
                a.onNext(null);
                if (d) {
                    errorOrComplete(a);
                    return;
                }
                missed = this.wip.addAndGet(-missed);
                if (missed == 0) {
                    return;
                }
            } else {
                return;
            }
        }
        this.actual.lazySet(null);
        q.clear();
    }

    /* access modifiers changed from: 0000 */
    public void errorOrComplete(Observer<? super T> a) {
        this.actual.lazySet(null);
        Throwable ex = this.error;
        if (ex != null) {
            a.onError(ex);
        } else {
            a.onComplete();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean failedFast(SimpleQueue<T> q, Observer<? super T> a) {
        Throwable ex = this.error;
        if (ex == null) {
            return false;
        }
        this.actual.lazySet(null);
        q.clear();
        a.onError(ex);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            Observer<? super T> a = (Observer) this.actual.get();
            int missed = 1;
            while (a == null) {
                missed = this.wip.addAndGet(-missed);
                if (missed != 0) {
                    a = (Observer) this.actual.get();
                } else {
                    return;
                }
            }
            if (this.enableOperatorFusion) {
                drainFused(a);
            } else {
                drainNormal(a);
            }
        }
    }

    public boolean hasObservers() {
        return this.actual.get() != null;
    }

    @Nullable
    public Throwable getThrowable() {
        if (this.done) {
            return this.error;
        }
        return null;
    }

    public boolean hasThrowable() {
        return this.done && this.error != null;
    }

    public boolean hasComplete() {
        return this.done && this.error == null;
    }
}
