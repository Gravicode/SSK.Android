package p005io.reactivex.processors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.processors.UnicastProcessor */
public final class UnicastProcessor<T> extends FlowableProcessor<T> {
    final AtomicReference<Subscriber<? super T>> actual;
    volatile boolean cancelled;
    final boolean delayError;
    volatile boolean done;
    boolean enableOperatorFusion;
    Throwable error;
    final AtomicReference<Runnable> onTerminate;
    final AtomicBoolean once;
    final SpscLinkedArrayQueue<T> queue;
    final AtomicLong requested;
    final BasicIntQueueSubscription<T> wip;

    /* renamed from: io.reactivex.processors.UnicastProcessor$UnicastQueueSubscription */
    final class UnicastQueueSubscription extends BasicIntQueueSubscription<T> {
        private static final long serialVersionUID = -4896760517184205454L;

        UnicastQueueSubscription() {
        }

        @Nullable
        public T poll() {
            return UnicastProcessor.this.queue.poll();
        }

        public boolean isEmpty() {
            return UnicastProcessor.this.queue.isEmpty();
        }

        public void clear() {
            UnicastProcessor.this.queue.clear();
        }

        public int requestFusion(int requestedMode) {
            if ((requestedMode & 2) == 0) {
                return 0;
            }
            UnicastProcessor.this.enableOperatorFusion = true;
            return 2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(UnicastProcessor.this.requested, n);
                UnicastProcessor.this.drain();
            }
        }

        public void cancel() {
            if (!UnicastProcessor.this.cancelled) {
                UnicastProcessor.this.cancelled = true;
                UnicastProcessor.this.doTerminate();
                if (!UnicastProcessor.this.enableOperatorFusion && UnicastProcessor.this.wip.getAndIncrement() == 0) {
                    UnicastProcessor.this.queue.clear();
                    UnicastProcessor.this.actual.lazySet(null);
                }
            }
        }
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastProcessor<T> create() {
        return new UnicastProcessor<>(bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastProcessor<T> create(int capacityHint) {
        return new UnicastProcessor<>(capacityHint);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public static <T> UnicastProcessor<T> create(boolean delayError2) {
        return new UnicastProcessor<>(bufferSize(), null, delayError2);
    }

    @CheckReturnValue
    @NonNull
    public static <T> UnicastProcessor<T> create(int capacityHint, Runnable onCancelled) {
        ObjectHelper.requireNonNull(onCancelled, "onTerminate");
        return new UnicastProcessor<>(capacityHint, onCancelled);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public static <T> UnicastProcessor<T> create(int capacityHint, Runnable onCancelled, boolean delayError2) {
        ObjectHelper.requireNonNull(onCancelled, "onTerminate");
        return new UnicastProcessor<>(capacityHint, onCancelled, delayError2);
    }

    UnicastProcessor(int capacityHint) {
        this(capacityHint, null, true);
    }

    UnicastProcessor(int capacityHint, Runnable onTerminate2) {
        this(capacityHint, onTerminate2, true);
    }

    UnicastProcessor(int capacityHint, Runnable onTerminate2, boolean delayError2) {
        this.queue = new SpscLinkedArrayQueue<>(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
        this.onTerminate = new AtomicReference<>(onTerminate2);
        this.delayError = delayError2;
        this.actual = new AtomicReference<>();
        this.once = new AtomicBoolean();
        this.wip = new UnicastQueueSubscription();
        this.requested = new AtomicLong();
    }

    /* access modifiers changed from: 0000 */
    public void doTerminate() {
        Runnable r = (Runnable) this.onTerminate.getAndSet(null);
        if (r != null) {
            r.run();
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainRegular(Subscriber<? super T> a) {
        long e;
        long e2;
        SpscLinkedArrayQueue<T> q = this.queue;
        boolean failFast = !this.delayError;
        int missed = 1;
        while (true) {
            long r = this.requested.get();
            long e3 = 0;
            while (true) {
                e = e3;
                if (r == e) {
                    break;
                }
                boolean d = this.done;
                T t = q.poll();
                boolean empty = t == null;
                T t2 = t;
                boolean z = d;
                if (!checkTerminated(failFast, d, empty, a, q)) {
                    if (empty) {
                        break;
                    }
                    a.onNext(t2);
                    e3 = 1 + e;
                } else {
                    return;
                }
            }
            Subscriber<? super T> subscriber = a;
            if (r == e) {
                e2 = e;
                if (checkTerminated(failFast, this.done, q.isEmpty(), subscriber, q)) {
                    return;
                }
            } else {
                e2 = e;
            }
            if (!(e2 == 0 || r == Long.MAX_VALUE)) {
                this.requested.addAndGet(-e2);
            }
            missed = this.wip.addAndGet(-missed);
            if (missed == 0) {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drainFused(Subscriber<? super T> a) {
        int missed = 1;
        SpscLinkedArrayQueue<T> q = this.queue;
        boolean failFast = !this.delayError;
        while (!this.cancelled) {
            boolean d = this.done;
            if (!failFast || !d || this.error == null) {
                a.onNext(null);
                if (d) {
                    this.actual.lazySet(null);
                    Throwable ex = this.error;
                    if (ex != null) {
                        a.onError(ex);
                    } else {
                        a.onComplete();
                    }
                    return;
                }
                missed = this.wip.addAndGet(-missed);
                if (missed == 0) {
                    return;
                }
            } else {
                q.clear();
                this.actual.lazySet(null);
                a.onError(this.error);
                return;
            }
        }
        q.clear();
        this.actual.lazySet(null);
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            int missed = 1;
            Subscriber<? super T> a = (Subscriber) this.actual.get();
            while (a == null) {
                missed = this.wip.addAndGet(-missed);
                if (missed != 0) {
                    a = (Subscriber) this.actual.get();
                } else {
                    return;
                }
            }
            if (this.enableOperatorFusion) {
                drainFused(a);
            } else {
                drainRegular(a);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean checkTerminated(boolean failFast, boolean d, boolean empty, Subscriber<? super T> a, SpscLinkedArrayQueue<T> q) {
        if (this.cancelled) {
            q.clear();
            this.actual.lazySet(null);
            return true;
        }
        if (d) {
            if (failFast && this.error != null) {
                q.clear();
                this.actual.lazySet(null);
                a.onError(this.error);
                return true;
            } else if (empty) {
                Throwable e = this.error;
                this.actual.lazySet(null);
                if (e != null) {
                    a.onError(e);
                } else {
                    a.onComplete();
                }
                return true;
            }
        }
        return false;
    }

    public void onSubscribe(Subscription s) {
        if (this.done || this.cancelled) {
            s.cancel();
        } else {
            s.request(Long.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.done && !this.cancelled) {
            this.queue.offer(t);
            drain();
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.done || this.cancelled) {
            RxJavaPlugins.onError(t);
            return;
        }
        this.error = t;
        this.done = true;
        doTerminate();
        drain();
    }

    public void onComplete() {
        if (!this.done && !this.cancelled) {
            this.done = true;
            doTerminate();
            drain();
        }
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.once.get() || !this.once.compareAndSet(false, true)) {
            EmptySubscription.error(new IllegalStateException("This processor allows only a single Subscriber"), s);
            return;
        }
        s.onSubscribe(this.wip);
        this.actual.set(s);
        if (this.cancelled) {
            this.actual.lazySet(null);
        } else {
            drain();
        }
    }

    public boolean hasSubscribers() {
        return this.actual.get() != null;
    }

    @Nullable
    public Throwable getThrowable() {
        if (this.done) {
            return this.error;
        }
        return null;
    }

    public boolean hasComplete() {
        return this.done && this.error == null;
    }

    public boolean hasThrowable() {
        return this.done && this.error != null;
    }
}
