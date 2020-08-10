package p005io.reactivex.processors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.annotations.BackpressureKind;
import p005io.reactivex.annotations.BackpressureSupport;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.SchedulerSupport;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
@BackpressureSupport(BackpressureKind.FULL)
@SchedulerSupport("none")
/* renamed from: io.reactivex.processors.MulticastProcessor */
public final class MulticastProcessor<T> extends FlowableProcessor<T> {
    static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
    static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
    final int bufferSize;
    int consumed;
    volatile boolean done;
    volatile Throwable error;
    int fusionMode;
    final int limit;
    final AtomicBoolean once;
    volatile SimpleQueue<T> queue;
    final boolean refcount;
    final AtomicReference<MulticastSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
    final AtomicReference<Subscription> upstream = new AtomicReference<>();
    final AtomicInteger wip = new AtomicInteger();

    /* renamed from: io.reactivex.processors.MulticastProcessor$MulticastSubscription */
    static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -363282618957264509L;
        final Subscriber<? super T> actual;
        long emitted;
        final MulticastProcessor<T> parent;

        MulticastSubscription(Subscriber<? super T> actual2, MulticastProcessor<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void request(long n) {
            long r;
            long u;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE && r != Long.MAX_VALUE) {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                this.parent.drain();
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onNext(T t) {
            if (get() != Long.MIN_VALUE) {
                this.emitted++;
                this.actual.onNext(t);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable t) {
            if (get() != Long.MIN_VALUE) {
                this.actual.onError(t);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onComplete() {
            if (get() != Long.MIN_VALUE) {
                this.actual.onComplete();
            }
        }
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create() {
        return new MulticastProcessor<>(bufferSize(), false);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(boolean refCount) {
        return new MulticastProcessor<>(bufferSize(), refCount);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(int bufferSize2) {
        return new MulticastProcessor<>(bufferSize2, false);
    }

    @CheckReturnValue
    @NonNull
    public static <T> MulticastProcessor<T> create(int bufferSize2, boolean refCount) {
        return new MulticastProcessor<>(bufferSize2, refCount);
    }

    MulticastProcessor(int bufferSize2, boolean refCount) {
        ObjectHelper.verifyPositive(bufferSize2, "bufferSize");
        this.bufferSize = bufferSize2;
        this.limit = bufferSize2 - (bufferSize2 >> 2);
        this.refcount = refCount;
        this.once = new AtomicBoolean();
    }

    public void start() {
        if (SubscriptionHelper.setOnce(this.upstream, EmptySubscription.INSTANCE)) {
            this.queue = new SpscArrayQueue(this.bufferSize);
        }
    }

    public void startUnbounded() {
        if (SubscriptionHelper.setOnce(this.upstream, EmptySubscription.INSTANCE)) {
            this.queue = new SpscLinkedArrayQueue(this.bufferSize);
        }
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this.upstream, s)) {
            if (s instanceof QueueSubscription) {
                QueueSubscription<T> qs = (QueueSubscription) s;
                int m = qs.requestFusion(3);
                if (m == 1) {
                    this.fusionMode = m;
                    this.queue = qs;
                    this.done = true;
                    drain();
                    return;
                } else if (m == 2) {
                    this.fusionMode = m;
                    this.queue = qs;
                    s.request((long) this.bufferSize);
                    return;
                }
            }
            this.queue = new SpscArrayQueue(this.bufferSize);
            s.request((long) this.bufferSize);
        }
    }

    public void onNext(T t) {
        if (!this.once.get()) {
            if (this.fusionMode == 0) {
                ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
                if (!this.queue.offer(t)) {
                    SubscriptionHelper.cancel(this.upstream);
                    onError(new MissingBackpressureException());
                    return;
                }
            }
            drain();
        }
    }

    public boolean offer(T t) {
        if (this.once.get()) {
            return false;
        }
        ObjectHelper.requireNonNull(t, "offer called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.fusionMode != 0 || !this.queue.offer(t)) {
            return false;
        }
        drain();
        return true;
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.once.compareAndSet(false, true)) {
            this.error = t;
            this.done = true;
            drain();
            return;
        }
        RxJavaPlugins.onError(t);
    }

    public void onComplete() {
        if (this.once.compareAndSet(false, true)) {
            this.done = true;
            drain();
        }
    }

    public boolean hasSubscribers() {
        return ((MulticastSubscription[]) this.subscribers.get()).length != 0;
    }

    public boolean hasThrowable() {
        return this.once.get() && this.error != null;
    }

    public boolean hasComplete() {
        return this.once.get() && this.error == null;
    }

    public Throwable getThrowable() {
        if (this.once.get()) {
            return this.error;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        MulticastSubscription<T> ms = new MulticastSubscription<>(s, this);
        s.onSubscribe(ms);
        if (!add(ms)) {
            if (this.once.get() || !this.refcount) {
                Throwable ex = this.error;
                if (ex != null) {
                    s.onError(ex);
                    return;
                }
            }
            s.onComplete();
        } else if (ms.get() == Long.MIN_VALUE) {
            remove(ms);
        } else {
            drain();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean add(MulticastSubscription<T> inner) {
        MulticastSubscription<T>[] a;
        MulticastSubscription<T>[] b;
        do {
            a = (MulticastSubscription[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new MulticastSubscription[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(MulticastSubscription<T> inner) {
        while (true) {
            MulticastSubscription<T>[] a = (MulticastSubscription[]) this.subscribers.get();
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
                if (j < 0) {
                    break;
                } else if (n != 1) {
                    MulticastSubscription<T>[] b = new MulticastSubscription[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    if (this.subscribers.compareAndSet(a, b)) {
                        break;
                    }
                } else if (this.refcount) {
                    if (this.subscribers.compareAndSet(a, TERMINATED)) {
                        SubscriptionHelper.cancel(this.upstream);
                        this.once.set(true);
                        break;
                    }
                } else if (this.subscribers.compareAndSet(a, EMPTY)) {
                    break;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x011c, code lost:
        if (r11 != 0) goto L_0x016c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x011e, code lost:
        r2 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r3.get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0126, code lost:
        if (r2 != TERMINATED) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0128, code lost:
        r7.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x012b, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x012c, code lost:
        if (r8 == r2) goto L_0x0133;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0135, code lost:
        if (r1.done == false) goto L_0x016c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x013b, code lost:
        if (r7.isEmpty() == false) goto L_0x016c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x013d, code lost:
        r9 = r1.error;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x013f, code lost:
        if (r9 == null) goto L_0x0155;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0141, code lost:
        r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r3.getAndSet(TERMINATED);
        r13 = r10.length;
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x014b, code lost:
        if (r14 >= r13) goto L_0x0169;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x014d, code lost:
        r10[r14].onError(r9);
        r14 = r14 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0155, code lost:
        r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r3.getAndSet(TERMINATED);
        r13 = r10.length;
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x015f, code lost:
        if (r14 >= r13) goto L_0x0169;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0161, code lost:
        r10[r14].onComplete();
        r14 = r14 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0169, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drain() {
        /*
            r31 = this;
            r1 = r31
            java.util.concurrent.atomic.AtomicInteger r2 = r1.wip
            int r2 = r2.getAndIncrement()
            if (r2 == 0) goto L_0x000b
            return
        L_0x000b:
            r2 = 1
            java.util.concurrent.atomic.AtomicReference<io.reactivex.processors.MulticastProcessor$MulticastSubscription<T>[]> r3 = r1.subscribers
            int r4 = r1.consumed
            int r5 = r1.limit
            int r6 = r1.fusionMode
        L_0x0014:
            io.reactivex.internal.fuseable.SimpleQueue<T> r7 = r1.queue
            if (r7 == 0) goto L_0x016a
            java.lang.Object r8 = r3.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r8 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r8
            int r9 = r8.length
            if (r9 == 0) goto L_0x016a
            r10 = -1
            int r12 = r8.length
            r14 = r10
            r10 = 0
        L_0x0026:
            r16 = 0
            if (r10 >= r12) goto L_0x005e
            r11 = r8[r10]
            long r18 = r11.get()
            int r16 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
            if (r16 < 0) goto L_0x0056
            r16 = -1
            int r16 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r16 != 0) goto L_0x0044
            r20 = r14
            long r13 = r11.emitted
            long r13 = r18 - r13
            r22 = r12
            r14 = r13
            goto L_0x0059
        L_0x0044:
            r20 = r14
            long r13 = r11.emitted
            long r13 = r18 - r13
            r23 = r11
            r22 = r12
            r11 = r20
            long r11 = java.lang.Math.min(r11, r13)
            r14 = r11
            goto L_0x0059
        L_0x0056:
            r22 = r12
            r11 = r14
        L_0x0059:
            int r10 = r10 + 1
            r12 = r22
            goto L_0x0026
        L_0x005e:
            r11 = r14
        L_0x005f:
            int r10 = (r11 > r16 ? 1 : (r11 == r16 ? 0 : -1))
            if (r10 <= 0) goto L_0x0116
            java.lang.Object r10 = r3.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r10
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r13 = TERMINATED
            if (r10 != r13) goto L_0x0071
            r7.clear()
            return
        L_0x0071:
            if (r8 == r10) goto L_0x0078
            r27 = r2
            goto L_0x012f
        L_0x0078:
            boolean r13 = r1.done
            java.lang.Object r15 = r7.poll()     // Catch:{ Throwable -> 0x0083 }
            r24 = r9
            r14 = r15
            goto L_0x0096
        L_0x0083:
            r0 = move-exception
            r15 = r0
            p005io.reactivex.exceptions.Exceptions.throwIfFatal(r15)
            java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r14 = r1.upstream
            p005io.reactivex.internal.subscriptions.SubscriptionHelper.cancel(r14)
            r13 = 1
            r14 = 0
            r1.error = r15
            r24 = r9
            r9 = 1
            r1.done = r9
        L_0x0096:
            if (r14 != 0) goto L_0x009a
            r9 = 1
            goto L_0x009b
        L_0x009a:
            r9 = 0
        L_0x009b:
            if (r13 == 0) goto L_0x00e0
            if (r9 == 0) goto L_0x00e0
            java.lang.Throwable r15 = r1.error
            if (r15 == 0) goto L_0x00c1
            r25 = r10
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = TERMINATED
            java.lang.Object r10 = r3.getAndSet(r10)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r10
            r26 = r13
            int r13 = r10.length
            r27 = r2
            r2 = 0
        L_0x00b3:
            if (r2 >= r13) goto L_0x00df
            r28 = r13
            r13 = r10[r2]
            r13.onError(r15)
            int r2 = r2 + 1
            r13 = r28
            goto L_0x00b3
        L_0x00c1:
            r27 = r2
            r25 = r10
            r26 = r13
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r2 = TERMINATED
            java.lang.Object r2 = r3.getAndSet(r2)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r2 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r2
            int r10 = r2.length
            r13 = 0
        L_0x00d1:
            if (r13 >= r10) goto L_0x00df
            r29 = r10
            r10 = r2[r13]
            r10.onComplete()
            int r13 = r13 + 1
            r10 = r29
            goto L_0x00d1
        L_0x00df:
            return
        L_0x00e0:
            r27 = r2
            r25 = r10
            r26 = r13
            if (r9 == 0) goto L_0x00e9
            goto L_0x011a
        L_0x00e9:
            int r2 = r8.length
            r10 = 0
        L_0x00eb:
            if (r10 >= r2) goto L_0x00f5
            r13 = r8[r10]
            r13.onNext(r14)
            int r10 = r10 + 1
            goto L_0x00eb
        L_0x00f5:
            r18 = 1
            long r11 = r11 - r18
            r2 = 1
            if (r6 == r2) goto L_0x010f
            int r4 = r4 + 1
            if (r4 != r5) goto L_0x010f
            r4 = 0
            java.util.concurrent.atomic.AtomicReference<org.reactivestreams.Subscription> r2 = r1.upstream
            java.lang.Object r2 = r2.get()
            org.reactivestreams.Subscription r2 = (org.reactivestreams.Subscription) r2
            r30 = r9
            long r9 = (long) r5
            r2.request(r9)
        L_0x010f:
            r9 = r24
            r2 = r27
            goto L_0x005f
        L_0x0116:
            r27 = r2
            r24 = r9
        L_0x011a:
            int r2 = (r11 > r16 ? 1 : (r11 == r16 ? 0 : -1))
            if (r2 != 0) goto L_0x016c
            java.lang.Object r2 = r3.get()
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r2 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r2
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r9 = TERMINATED
            if (r2 != r9) goto L_0x012c
            r7.clear()
            return
        L_0x012c:
            if (r8 == r2) goto L_0x0133
        L_0x012f:
            r2 = r27
            goto L_0x0014
        L_0x0133:
            boolean r9 = r1.done
            if (r9 == 0) goto L_0x016c
            boolean r9 = r7.isEmpty()
            if (r9 == 0) goto L_0x016c
            java.lang.Throwable r9 = r1.error
            if (r9 == 0) goto L_0x0155
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = TERMINATED
            java.lang.Object r10 = r3.getAndSet(r10)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r10
            int r13 = r10.length
            r14 = 0
        L_0x014b:
            if (r14 >= r13) goto L_0x0169
            r15 = r10[r14]
            r15.onError(r9)
            int r14 = r14 + 1
            goto L_0x014b
        L_0x0155:
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = TERMINATED
            java.lang.Object r10 = r3.getAndSet(r10)
            io.reactivex.processors.MulticastProcessor$MulticastSubscription[] r10 = (p005io.reactivex.processors.MulticastProcessor.MulticastSubscription[]) r10
            int r13 = r10.length
            r14 = 0
        L_0x015f:
            if (r14 >= r13) goto L_0x0169
            r15 = r10[r14]
            r15.onComplete()
            int r14 = r14 + 1
            goto L_0x015f
        L_0x0169:
            return
        L_0x016a:
            r27 = r2
        L_0x016c:
            java.util.concurrent.atomic.AtomicInteger r2 = r1.wip
            r8 = r27
            int r9 = -r8
            int r2 = r2.addAndGet(r9)
            if (r2 != 0) goto L_0x0179
            return
        L_0x0179:
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.reactivex.processors.MulticastProcessor.drain():void");
    }
}
