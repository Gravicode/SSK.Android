package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.exceptions.OnErrorThrowable;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.ScalarSynchronousObservable;
import p008rx.internal.util.atomic.SpscAtomicArrayQueue;
import p008rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import p008rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import p008rx.internal.util.unsafe.Pow2;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.subscriptions.CompositeSubscription;

/* renamed from: rx.internal.operators.OperatorMerge */
public final class OperatorMerge<T> implements Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    /* renamed from: rx.internal.operators.OperatorMerge$HolderDelayErrors */
    static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(true, Integer.MAX_VALUE);

        HolderDelayErrors() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$HolderNoDelay */
    static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(false, Integer.MAX_VALUE);

        HolderNoDelay() {
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$InnerSubscriber */
    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int LIMIT = (RxRingBuffer.SIZE / 4);
        volatile boolean done;

        /* renamed from: id */
        final long f908id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        public InnerSubscriber(MergeSubscriber<T> parent2, long id) {
            this.parent = parent2;
            this.f908id = id;
        }

        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request((long) RxRingBuffer.SIZE);
        }

        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        public void onError(Throwable e) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(e);
            this.parent.emit();
        }

        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long n) {
            int r = this.outstanding - ((int) n);
            if (r > LIMIT) {
                this.outstanding = r;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int k = RxRingBuffer.SIZE - r;
            if (k > 0) {
                request((long) k);
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$MergeProducer */
    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void request(long n) {
            if (n > 0) {
                if (get() != Long.MAX_VALUE) {
                    BackpressureUtils.getAndAddRequest(this, n);
                    this.subscriber.emit();
                }
            } else if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
        }

        public long produced(int n) {
            return addAndGet((long) (-n));
        }
    }

    /* renamed from: rx.internal.operators.OperatorMerge$MergeSubscriber */
    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY = new InnerSubscriber[0];
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        final Object innerGuard = new Object();
        volatile InnerSubscriber<?>[] innerSubscribers = EMPTY;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        int scalarEmissionCount;
        final int scalarEmissionLimit;
        volatile CompositeSubscription subscriptions;
        long uniqueId;

        public MergeSubscriber(Subscriber<? super T> child2, boolean delayErrors2, int maxConcurrent2) {
            this.child = child2;
            this.delayErrors = delayErrors2;
            this.maxConcurrent = maxConcurrent2;
            if (maxConcurrent2 == Integer.MAX_VALUE) {
                this.scalarEmissionLimit = Integer.MAX_VALUE;
                request(Long.MAX_VALUE);
                return;
            }
            this.scalarEmissionLimit = Math.max(1, maxConcurrent2 >> 1);
            request((long) maxConcurrent2);
        }

        /* access modifiers changed from: 0000 */
        public Queue<Throwable> getOrCreateErrorQueue() {
            ConcurrentLinkedQueue<Throwable> q = this.errors;
            if (q == null) {
                synchronized (this) {
                    q = this.errors;
                    if (q == null) {
                        q = new ConcurrentLinkedQueue<>();
                        this.errors = q;
                    }
                }
            }
            return q;
        }

        /* access modifiers changed from: 0000 */
        public CompositeSubscription getOrCreateComposite() {
            CompositeSubscription c = this.subscriptions;
            if (c == null) {
                boolean shouldAdd = false;
                synchronized (this) {
                    c = this.subscriptions;
                    if (c == null) {
                        c = new CompositeSubscription();
                        this.subscriptions = c;
                        shouldAdd = true;
                    }
                }
                if (shouldAdd) {
                    add(c);
                }
            }
            return c;
        }

        public void onNext(Observable<? extends T> t) {
            if (t != null) {
                if (t == Observable.empty()) {
                    emitEmpty();
                } else if (t instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) t).get());
                } else {
                    long j = this.uniqueId;
                    this.uniqueId = 1 + j;
                    InnerSubscriber<T> inner = new InnerSubscriber<>(this, j);
                    addInner(inner);
                    t.unsafeSubscribe(inner);
                    emit();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void emitEmpty() {
            int produced = this.scalarEmissionCount + 1;
            if (produced == this.scalarEmissionLimit) {
                this.scalarEmissionCount = 0;
                requestMore((long) produced);
                return;
            }
            this.scalarEmissionCount = produced;
        }

        private void reportError() {
            List<Throwable> list = new ArrayList<>(this.errors);
            if (list.size() == 1) {
                this.child.onError((Throwable) list.get(0));
            } else {
                this.child.onError(new CompositeException((Collection<? extends Throwable>) list));
            }
        }

        public void onError(Throwable e) {
            getOrCreateErrorQueue().offer(e);
            this.done = true;
            emit();
        }

        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* access modifiers changed from: 0000 */
        public void addInner(InnerSubscriber<T> inner) {
            getOrCreateComposite().add(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                InnerSubscriber<?>[] b = new InnerSubscriber[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = inner;
                this.innerSubscribers = b;
            }
        }

        /* access modifiers changed from: 0000 */
        public void removeInner(InnerSubscriber<T> inner) {
            RxRingBuffer q = inner.queue;
            if (q != null) {
                q.release();
            }
            this.subscriptions.remove(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a = this.innerSubscribers;
                int n = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (inner.equals(a[i])) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.innerSubscribers = EMPTY;
                        return;
                    }
                    InnerSubscriber<?>[] b = new InnerSubscriber[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    this.innerSubscribers = b;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(InnerSubscriber<T> subscriber, T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                RxRingBuffer subscriberQueue = subscriber.queue;
                if (subscriberQueue == null || subscriberQueue.isEmpty()) {
                    emitScalar(subscriber, value, r);
                    return;
                }
                queueScalar(subscriber, value);
                emitLoop();
                return;
            }
            queueScalar(subscriber, value);
            emit();
        }

        /* access modifiers changed from: protected */
        public void queueScalar(InnerSubscriber<T> subscriber, T value) {
            RxRingBuffer q = subscriber.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                subscriber.add(q);
                subscriber.queue = q;
            }
            try {
                q.onNext(NotificationLite.next(value));
            } catch (MissingBackpressureException ex) {
                subscriber.unsubscribe();
                subscriber.onError(ex);
            } catch (IllegalStateException ex2) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                    subscriber.onError(ex2);
                }
            }
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0048, code lost:
            if (1 != 0) goto L_0x0052;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x004a, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x004d, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0052, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0056, code lost:
            if (1 != 0) goto L_0x0060;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0058, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x005b, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0060, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0063, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x0067, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x0068, code lost:
            r2 = r1;
            r1 = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitScalar(p008rx.internal.operators.OperatorMerge.InnerSubscriber<T> r6, T r7, long r8) {
            /*
                r5 = this;
                r0 = 0
                r1 = r0
                rx.Subscriber<? super T> r2 = r5.child     // Catch:{ Throwable -> 0x000a }
                r2.onNext(r7)     // Catch:{ Throwable -> 0x000a }
                goto L_0x002b
            L_0x0008:
                r2 = move-exception
                goto L_0x006b
            L_0x000a:
                r2 = move-exception
                boolean r3 = r5.delayErrors     // Catch:{ all -> 0x0008 }
                if (r3 != 0) goto L_0x0024
                p008rx.exceptions.Exceptions.throwIfFatal(r2)     // Catch:{ all -> 0x0008 }
                r1 = 1
                r6.unsubscribe()     // Catch:{ all -> 0x0008 }
                r6.onError(r2)     // Catch:{ all -> 0x0008 }
                if (r1 != 0) goto L_0x0023
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x0020 }
                monitor-exit(r5)     // Catch:{ all -> 0x0020 }
                goto L_0x0023
            L_0x0020:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0020 }
                throw r0
            L_0x0023:
                return
            L_0x0024:
                java.util.Queue r3 = r5.getOrCreateErrorQueue()     // Catch:{ all -> 0x0008 }
                r3.offer(r2)     // Catch:{ all -> 0x0008 }
            L_0x002b:
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
                if (r2 == 0) goto L_0x003a
                rx.internal.operators.OperatorMerge$MergeProducer<T> r2 = r5.producer     // Catch:{ all -> 0x0008 }
                r3 = 1
                r2.produced(r3)     // Catch:{ all -> 0x0008 }
            L_0x003a:
                r2 = 1
                r6.requestMore(r2)     // Catch:{ all -> 0x0008 }
                monitor-enter(r5)     // Catch:{ all -> 0x0008 }
                r2 = 1
                boolean r1 = r5.missed     // Catch:{ all -> 0x0064 }
                if (r1 != 0) goto L_0x0053
                r5.emitting = r0     // Catch:{ all -> 0x0064 }
                monitor-exit(r5)     // Catch:{ all -> 0x0064 }
                if (r2 != 0) goto L_0x0052
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x004f }
                monitor-exit(r5)     // Catch:{ all -> 0x004f }
                goto L_0x0052
            L_0x004f:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x004f }
                throw r0
            L_0x0052:
                return
            L_0x0053:
                r5.missed = r0     // Catch:{ all -> 0x0064 }
                monitor-exit(r5)     // Catch:{ all -> 0x0064 }
                if (r2 != 0) goto L_0x0060
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x005d }
                monitor-exit(r5)     // Catch:{ all -> 0x005d }
                goto L_0x0060
            L_0x005d:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x005d }
                throw r0
            L_0x0060:
                r5.emitLoop()
                return
            L_0x0064:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0064 }
                throw r1     // Catch:{ all -> 0x0067 }
            L_0x0067:
                r1 = move-exception
                r4 = r2
                r2 = r1
                r1 = r4
            L_0x006b:
                if (r1 != 0) goto L_0x0075
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x0072 }
                monitor-exit(r5)     // Catch:{ all -> 0x0072 }
                goto L_0x0075
            L_0x0072:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0072 }
                throw r0
            L_0x0075:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(rx.internal.operators.OperatorMerge$InnerSubscriber, java.lang.Object, long):void");
        }

        public void requestMore(long n) {
            request(n);
        }

        /* access modifiers changed from: 0000 */
        public void tryEmit(T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                Queue<Object> mainQueue = this.queue;
                if (mainQueue == null || mainQueue.isEmpty()) {
                    emitScalar(value, r);
                    return;
                }
                queueScalar(value);
                emitLoop();
                return;
            }
            queueScalar(value);
            emit();
        }

        /* access modifiers changed from: protected */
        public void queueScalar(T value) {
            Queue<Object> q = this.queue;
            if (q == null) {
                int mc = this.maxConcurrent;
                if (mc == Integer.MAX_VALUE) {
                    q = new SpscUnboundedAtomicArrayQueue<>(RxRingBuffer.SIZE);
                } else if (!Pow2.isPowerOfTwo(mc)) {
                    q = new SpscExactAtomicArrayQueue<>(mc);
                } else if (UnsafeAccess.isUnsafeAvailable()) {
                    q = new SpscArrayQueue<>(mc);
                } else {
                    q = new SpscAtomicArrayQueue<>(mc);
                }
                this.queue = q;
            }
            if (!q.offer(NotificationLite.next(value))) {
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), value));
            }
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0053, code lost:
            if (1 != 0) goto L_0x005d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0055, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0058, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x005d, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0061, code lost:
            if (1 != 0) goto L_0x006b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0063, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0066, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x006b, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x006e, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:0x0072, code lost:
            r2 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x0073, code lost:
            r1 = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitScalar(T r6, long r7) {
            /*
                r5 = this;
                r0 = 0
                r1 = r0
                rx.Subscriber<? super T> r2 = r5.child     // Catch:{ Throwable -> 0x000a }
                r2.onNext(r6)     // Catch:{ Throwable -> 0x000a }
                goto L_0x002b
            L_0x0008:
                r2 = move-exception
                goto L_0x0074
            L_0x000a:
                r2 = move-exception
                boolean r3 = r5.delayErrors     // Catch:{ all -> 0x0008 }
                if (r3 != 0) goto L_0x0024
                p008rx.exceptions.Exceptions.throwIfFatal(r2)     // Catch:{ all -> 0x0008 }
                r1 = 1
                r5.unsubscribe()     // Catch:{ all -> 0x0008 }
                r5.onError(r2)     // Catch:{ all -> 0x0008 }
                if (r1 != 0) goto L_0x0023
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x0020 }
                monitor-exit(r5)     // Catch:{ all -> 0x0020 }
                goto L_0x0023
            L_0x0020:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0020 }
                throw r0
            L_0x0023:
                return
            L_0x0024:
                java.util.Queue r3 = r5.getOrCreateErrorQueue()     // Catch:{ all -> 0x0008 }
                r3.offer(r2)     // Catch:{ all -> 0x0008 }
            L_0x002b:
                r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r2 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
                r3 = 1
                if (r2 == 0) goto L_0x003a
                rx.internal.operators.OperatorMerge$MergeProducer<T> r2 = r5.producer     // Catch:{ all -> 0x0008 }
                r2.produced(r3)     // Catch:{ all -> 0x0008 }
            L_0x003a:
                int r2 = r5.scalarEmissionCount     // Catch:{ all -> 0x0008 }
                int r2 = r2 + r3
                int r3 = r5.scalarEmissionLimit     // Catch:{ all -> 0x0008 }
                if (r2 != r3) goto L_0x0048
                r5.scalarEmissionCount = r0     // Catch:{ all -> 0x0008 }
                long r3 = (long) r2     // Catch:{ all -> 0x0008 }
                r5.requestMore(r3)     // Catch:{ all -> 0x0008 }
                goto L_0x004a
            L_0x0048:
                r5.scalarEmissionCount = r2     // Catch:{ all -> 0x0008 }
            L_0x004a:
                monitor-enter(r5)     // Catch:{ all -> 0x0008 }
                r3 = 1
                boolean r1 = r5.missed     // Catch:{ all -> 0x006f }
                if (r1 != 0) goto L_0x005e
                r5.emitting = r0     // Catch:{ all -> 0x006f }
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                if (r3 != 0) goto L_0x005d
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x005a }
                monitor-exit(r5)     // Catch:{ all -> 0x005a }
                goto L_0x005d
            L_0x005a:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x005a }
                throw r0
            L_0x005d:
                return
            L_0x005e:
                r5.missed = r0     // Catch:{ all -> 0x006f }
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                if (r3 != 0) goto L_0x006b
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x0068 }
                monitor-exit(r5)     // Catch:{ all -> 0x0068 }
                goto L_0x006b
            L_0x0068:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0068 }
                throw r0
            L_0x006b:
                r5.emitLoop()
                return
            L_0x006f:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x006f }
                throw r1     // Catch:{ all -> 0x0072 }
            L_0x0072:
                r2 = move-exception
                r1 = r3
            L_0x0074:
                if (r1 != 0) goto L_0x007e
                monitor-enter(r5)
                r5.emitting = r0     // Catch:{ all -> 0x007b }
                monitor-exit(r5)     // Catch:{ all -> 0x007b }
                goto L_0x007e
            L_0x007b:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x007b }
                throw r0
            L_0x007e:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorMerge.MergeSubscriber.emitScalar(java.lang.Object, long):void");
        }

        /* access modifiers changed from: 0000 */
        public void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                    return;
                }
                this.emitting = true;
                emitLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:206:0x01ca, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:209:?, code lost:
            unsubscribe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:210:0x01d0, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:272:0x0275, code lost:
            if (1 != 0) goto L_0x0281;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:273:0x0277, code lost:
            monitor-enter(r38);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:276:?, code lost:
            r1.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:277:0x027b, code lost:
            monitor-exit(r38);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:279:0x027d, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:282:0x0280, code lost:
            throw r0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:283:0x0281, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:89:0x00d0, code lost:
            if (r5.isEmpty() != false) goto L_0x00d2;
         */
        /* JADX WARNING: Removed duplicated region for block: B:100:0x00e9  */
        /* JADX WARNING: Removed duplicated region for block: B:135:0x0138 A[Catch:{ all -> 0x0265 }] */
        /* JADX WARNING: Removed duplicated region for block: B:302:0x029e  */
        /* JADX WARNING: Removed duplicated region for block: B:334:0x0249 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r38 = this;
                r1 = r38
                r2 = 0
                r3 = r2
                rx.Subscriber<? super T> r4 = r1.child     // Catch:{ all -> 0x0298 }
            L_0x0006:
                boolean r5 = r38.checkTerminate()     // Catch:{ all -> 0x0298 }
                if (r5 == 0) goto L_0x0019
                r3 = 1
                if (r3 != 0) goto L_0x0018
                monitor-enter(r38)
                r1.emitting = r2     // Catch:{ all -> 0x0014 }
                monitor-exit(r38)     // Catch:{ all -> 0x0014 }
                goto L_0x0018
            L_0x0014:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x0014 }
                throw r2
            L_0x0018:
                return
            L_0x0019:
                java.util.Queue<java.lang.Object> r5 = r1.queue     // Catch:{ all -> 0x0298 }
                rx.internal.operators.OperatorMerge$MergeProducer<T> r6 = r1.producer     // Catch:{ all -> 0x0298 }
                long r6 = r6.get()     // Catch:{ all -> 0x0298 }
                r8 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r8 != 0) goto L_0x002c
                r8 = 1
                goto L_0x002d
            L_0x002c:
                r8 = 0
            L_0x002d:
                r9 = 0
                r10 = 1
                r13 = 0
                if (r5 == 0) goto L_0x00c0
            L_0x0034:
                r15 = 0
                r36 = r6
                r7 = r15
                r15 = r36
                r6 = 0
            L_0x003b:
                int r17 = (r15 > r13 ? 1 : (r15 == r13 ? 0 : -1))
                if (r17 <= 0) goto L_0x009e
                java.lang.Object r17 = r5.poll()     // Catch:{ all -> 0x0098 }
                r6 = r17
                boolean r17 = r38.checkTerminate()     // Catch:{ all -> 0x0098 }
                if (r17 == 0) goto L_0x0058
                r3 = 1
                if (r3 != 0) goto L_0x0057
                monitor-enter(r38)
                r1.emitting = r2     // Catch:{ all -> 0x0053 }
                monitor-exit(r38)     // Catch:{ all -> 0x0053 }
                goto L_0x0057
            L_0x0053:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x0053 }
                throw r2
            L_0x0057:
                return
            L_0x0058:
                if (r6 != 0) goto L_0x005b
                goto L_0x009e
            L_0x005b:
                java.lang.Object r17 = p008rx.internal.operators.NotificationLite.getValue(r6)     // Catch:{ all -> 0x0098 }
                r18 = r17
                r12 = r18
                r4.onNext(r12)     // Catch:{ Throwable -> 0x0067 }
                goto L_0x008f
            L_0x0067:
                r0 = move-exception
                r19 = r0
                boolean r13 = r1.delayErrors     // Catch:{ all -> 0x0098 }
                if (r13 != 0) goto L_0x0086
                r13 = r19
                p008rx.exceptions.Exceptions.throwIfFatal(r13)     // Catch:{ all -> 0x0098 }
                r3 = 1
                r38.unsubscribe()     // Catch:{ all -> 0x0098 }
                r4.onError(r13)     // Catch:{ all -> 0x0098 }
                if (r3 != 0) goto L_0x0085
                monitor-enter(r38)
                r1.emitting = r2     // Catch:{ all -> 0x0081 }
                monitor-exit(r38)     // Catch:{ all -> 0x0081 }
                goto L_0x0085
            L_0x0081:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x0081 }
                throw r2
            L_0x0085:
                return
            L_0x0086:
                r13 = r19
                java.util.Queue r14 = r38.getOrCreateErrorQueue()     // Catch:{ all -> 0x0098 }
                r14.offer(r13)     // Catch:{ all -> 0x0098 }
            L_0x008f:
                int r9 = r9 + 1
                int r7 = r7 + 1
                long r15 = r15 - r10
                r13 = 0
                goto L_0x003b
            L_0x0098:
                r0 = move-exception
                r2 = r0
                r22 = r3
                goto L_0x029c
            L_0x009e:
                if (r7 <= 0) goto L_0x00af
                if (r8 == 0) goto L_0x00a8
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            L_0x00a7:
                goto L_0x00b0
            L_0x00a8:
                rx.internal.operators.OperatorMerge$MergeProducer<T> r12 = r1.producer     // Catch:{ all -> 0x0098 }
                long r12 = r12.produced(r7)     // Catch:{ all -> 0x0098 }
                goto L_0x00a7
            L_0x00af:
                r12 = r15
            L_0x00b0:
                r14 = 0
                int r16 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r16 == 0) goto L_0x00bf
                if (r6 != 0) goto L_0x00b9
                goto L_0x00bf
            L_0x00b9:
                r6 = r12
                r13 = 0
                goto L_0x0034
            L_0x00bf:
                r6 = r12
            L_0x00c0:
                boolean r12 = r1.done     // Catch:{ all -> 0x0298 }
                java.util.Queue<java.lang.Object> r13 = r1.queue     // Catch:{ all -> 0x0298 }
                r5 = r13
                rx.internal.operators.OperatorMerge$InnerSubscriber<?>[] r13 = r1.innerSubscribers     // Catch:{ all -> 0x0298 }
                int r14 = r13.length     // Catch:{ all -> 0x0298 }
                if (r12 == 0) goto L_0x00f3
                if (r5 == 0) goto L_0x00d2
                boolean r15 = r5.isEmpty()     // Catch:{ all -> 0x0098 }
                if (r15 == 0) goto L_0x00f3
            L_0x00d2:
                if (r14 != 0) goto L_0x00f3
                java.util.concurrent.ConcurrentLinkedQueue<java.lang.Throwable> r10 = r1.errors     // Catch:{ all -> 0x0098 }
                if (r10 == 0) goto L_0x00e3
                boolean r11 = r10.isEmpty()     // Catch:{ all -> 0x0098 }
                if (r11 == 0) goto L_0x00df
                goto L_0x00e3
            L_0x00df:
                r38.reportError()     // Catch:{ all -> 0x0098 }
                goto L_0x00e6
            L_0x00e3:
                r4.onCompleted()     // Catch:{ all -> 0x0098 }
            L_0x00e6:
                r3 = 1
                if (r3 != 0) goto L_0x00f2
                monitor-enter(r38)
                r1.emitting = r2     // Catch:{ all -> 0x00ee }
                monitor-exit(r38)     // Catch:{ all -> 0x00ee }
                goto L_0x00f2
            L_0x00ee:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x00ee }
                throw r2
            L_0x00f2:
                return
            L_0x00f3:
                r15 = 0
                if (r14 <= 0) goto L_0x0258
                long r10 = r1.lastId     // Catch:{ all -> 0x0298 }
                int r2 = r1.lastIndex     // Catch:{ all -> 0x0298 }
                if (r14 <= r2) goto L_0x010b
                r22 = r3
                r3 = r13[r2]     // Catch:{ all -> 0x0265 }
                r25 = r5
                r23 = r6
                long r5 = r3.f908id     // Catch:{ all -> 0x0265 }
                int r3 = (r5 > r10 ? 1 : (r5 == r10 ? 0 : -1))
                if (r3 == 0) goto L_0x0133
                goto L_0x0111
            L_0x010b:
                r22 = r3
                r25 = r5
                r23 = r6
            L_0x0111:
                if (r14 > r2) goto L_0x0114
                r2 = 0
            L_0x0114:
                r3 = r2
                r5 = r3
                r3 = 0
            L_0x0117:
                if (r3 >= r14) goto L_0x012a
                r6 = r13[r5]     // Catch:{ all -> 0x0265 }
                long r6 = r6.f908id     // Catch:{ all -> 0x0265 }
                int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r6 != 0) goto L_0x0122
                goto L_0x012a
            L_0x0122:
                int r5 = r5 + 1
                if (r5 != r14) goto L_0x0127
                r5 = 0
            L_0x0127:
                int r3 = r3 + 1
                goto L_0x0117
            L_0x012a:
                r2 = r5
                r1.lastIndex = r5     // Catch:{ all -> 0x0265 }
                r3 = r13[r5]     // Catch:{ all -> 0x0265 }
                long r6 = r3.f908id     // Catch:{ all -> 0x0265 }
                r1.lastId = r6     // Catch:{ all -> 0x0265 }
            L_0x0133:
                r3 = r2
                r5 = r3
                r3 = 0
            L_0x0136:
                if (r3 >= r14) goto L_0x0249
                boolean r6 = r38.checkTerminate()     // Catch:{ all -> 0x0265 }
                if (r6 == 0) goto L_0x014c
                r6 = 1
                if (r6 != 0) goto L_0x014b
                monitor-enter(r38)
                r7 = 0
                r1.emitting = r7     // Catch:{ all -> 0x0147 }
                monitor-exit(r38)     // Catch:{ all -> 0x0147 }
                goto L_0x014b
            L_0x0147:
                r0 = move-exception
                r7 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x0147 }
                throw r7
            L_0x014b:
                return
            L_0x014c:
                r6 = r13[r5]     // Catch:{ all -> 0x0265 }
                r7 = 0
            L_0x014f:
                r16 = r7
                r7 = 0
            L_0x0152:
                r17 = 0
                int r19 = (r23 > r17 ? 1 : (r23 == r17 ? 0 : -1))
                if (r19 <= 0) goto L_0x01d7
                boolean r17 = r38.checkTerminate()     // Catch:{ all -> 0x0265 }
                if (r17 == 0) goto L_0x0171
                r17 = 1
                if (r17 != 0) goto L_0x016e
                monitor-enter(r38)
                r26 = r2
                r2 = 0
                r1.emitting = r2     // Catch:{ all -> 0x016a }
                monitor-exit(r38)     // Catch:{ all -> 0x016a }
                goto L_0x0170
            L_0x016a:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x016a }
                throw r2
            L_0x016e:
                r26 = r2
            L_0x0170:
                return
            L_0x0171:
                r26 = r2
                rx.internal.util.RxRingBuffer r2 = r6.queue     // Catch:{ all -> 0x0265 }
                if (r2 != 0) goto L_0x0178
                goto L_0x0187
            L_0x0178:
                java.lang.Object r17 = r2.poll()     // Catch:{ all -> 0x0265 }
                r27 = r17
                r28 = r2
                r2 = r27
                if (r2 != 0) goto L_0x018a
                r16 = r2
            L_0x0187:
                r17 = 1
                goto L_0x01db
            L_0x018a:
                java.lang.Object r16 = p008rx.internal.operators.NotificationLite.getValue(r2)     // Catch:{ all -> 0x0265 }
                r29 = r16
                r30 = r2
                r2 = r29
                r4.onNext(r2)     // Catch:{ Throwable -> 0x01a6 }
                r16 = 0
                r17 = 1
                long r23 = r23 - r17
                int r7 = r7 + 1
                r2 = r26
                r16 = r30
                goto L_0x0152
            L_0x01a6:
                r0 = move-exception
                r31 = r0
                r16 = 1
                r32 = r2
                r2 = r31
                p008rx.exceptions.Exceptions.throwIfFatal(r2)     // Catch:{ all -> 0x01d1 }
                r4.onError(r2)     // Catch:{ all -> 0x01ca }
                r38.unsubscribe()     // Catch:{ all -> 0x01d1 }
                if (r16 != 0) goto L_0x01c7
                monitor-enter(r38)
                r33 = r2
                r2 = 0
                r1.emitting = r2     // Catch:{ all -> 0x01c3 }
                monitor-exit(r38)     // Catch:{ all -> 0x01c3 }
                goto L_0x01c9
            L_0x01c3:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x01c3 }
                throw r2
            L_0x01c7:
                r33 = r2
            L_0x01c9:
                return
            L_0x01ca:
                r0 = move-exception
                r33 = r2
                r38.unsubscribe()     // Catch:{ all -> 0x01d1 }
                throw r0     // Catch:{ all -> 0x01d1 }
            L_0x01d1:
                r0 = move-exception
                r2 = r0
                r22 = r16
                goto L_0x029c
            L_0x01d7:
                r26 = r2
                r17 = 1
            L_0x01db:
                if (r7 <= 0) goto L_0x01f4
                if (r8 != 0) goto L_0x01e6
                rx.internal.operators.OperatorMerge$MergeProducer<T> r2 = r1.producer     // Catch:{ all -> 0x0265 }
                long r20 = r2.produced(r7)     // Catch:{ all -> 0x0265 }
                goto L_0x01eb
            L_0x01e6:
                r20 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            L_0x01eb:
                r34 = r10
                long r10 = (long) r7     // Catch:{ all -> 0x0265 }
                r6.requestMore(r10)     // Catch:{ all -> 0x0265 }
                r23 = r20
                goto L_0x01f6
            L_0x01f4:
                r34 = r10
            L_0x01f6:
                r10 = 0
                int r2 = (r23 > r10 ? 1 : (r23 == r10 ? 0 : -1))
                if (r2 == 0) goto L_0x0208
                if (r16 != 0) goto L_0x01ff
                goto L_0x0208
            L_0x01ff:
                r7 = r16
                r2 = r26
                r10 = r34
                goto L_0x014f
            L_0x0208:
                boolean r2 = r6.done     // Catch:{ all -> 0x0265 }
                rx.internal.util.RxRingBuffer r7 = r6.queue     // Catch:{ all -> 0x0265 }
                if (r2 == 0) goto L_0x0231
                if (r7 == 0) goto L_0x0216
                boolean r10 = r7.isEmpty()     // Catch:{ all -> 0x0265 }
                if (r10 == 0) goto L_0x0231
            L_0x0216:
                r1.removeInner(r6)     // Catch:{ all -> 0x0265 }
                boolean r10 = r38.checkTerminate()     // Catch:{ all -> 0x0265 }
                if (r10 == 0) goto L_0x022d
                r10 = 1
                if (r10 != 0) goto L_0x022c
                monitor-enter(r38)
                r11 = 0
                r1.emitting = r11     // Catch:{ all -> 0x0228 }
                monitor-exit(r38)     // Catch:{ all -> 0x0228 }
                goto L_0x022c
            L_0x0228:
                r0 = move-exception
                r11 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x0228 }
                throw r11
            L_0x022c:
                return
            L_0x022d:
                int r9 = r9 + 1
                r10 = 1
                r15 = r10
            L_0x0231:
                r10 = 0
                int r19 = (r23 > r10 ? 1 : (r23 == r10 ? 0 : -1))
                if (r19 != 0) goto L_0x023b
                r6 = r23
                goto L_0x024f
            L_0x023b:
                int r5 = r5 + 1
                if (r5 != r14) goto L_0x0241
                r2 = 0
                r5 = r2
            L_0x0241:
                int r3 = r3 + 1
                r2 = r26
                r10 = r34
                goto L_0x0136
            L_0x0249:
                r26 = r2
                r34 = r10
                r6 = r23
            L_0x024f:
                r1.lastIndex = r5     // Catch:{ all -> 0x0265 }
                r2 = r13[r5]     // Catch:{ all -> 0x0265 }
                long r2 = r2.f908id     // Catch:{ all -> 0x0265 }
                r1.lastId = r2     // Catch:{ all -> 0x0265 }
                goto L_0x025e
            L_0x0258:
                r22 = r3
                r25 = r5
                r23 = r6
            L_0x025e:
                if (r9 <= 0) goto L_0x0268
                long r2 = (long) r9     // Catch:{ all -> 0x0265 }
                r1.request(r2)     // Catch:{ all -> 0x0265 }
                goto L_0x0268
            L_0x0265:
                r0 = move-exception
                r2 = r0
                goto L_0x029c
            L_0x0268:
                if (r15 == 0) goto L_0x026b
                goto L_0x028a
            L_0x026b:
                monitor-enter(r38)     // Catch:{ all -> 0x0265 }
                boolean r2 = r1.missed     // Catch:{ all -> 0x028f }
                if (r2 != 0) goto L_0x0285
                r2 = 1
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x0282 }
                monitor-exit(r38)     // Catch:{ all -> 0x0282 }
                if (r2 != 0) goto L_0x0281
                monitor-enter(r38)
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x027d }
                monitor-exit(r38)     // Catch:{ all -> 0x027d }
                goto L_0x0281
            L_0x027d:
                r0 = move-exception
                r3 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x027d }
                throw r3
            L_0x0281:
                return
            L_0x0282:
                r0 = move-exception
                r3 = r2
                goto L_0x0296
            L_0x0285:
                r2 = 0
                r1.missed = r2     // Catch:{ all -> 0x028f }
                monitor-exit(r38)     // Catch:{ all -> 0x028f }
            L_0x028a:
                r3 = r22
                r2 = 0
                goto L_0x0006
            L_0x028f:
                r0 = move-exception
                r2 = r0
                r3 = r22
            L_0x0293:
                monitor-exit(r38)     // Catch:{ all -> 0x0295 }
                throw r2     // Catch:{ all -> 0x0098 }
            L_0x0295:
                r0 = move-exception
            L_0x0296:
                r2 = r0
                goto L_0x0293
            L_0x0298:
                r0 = move-exception
                r22 = r3
                r2 = r0
            L_0x029c:
                if (r22 != 0) goto L_0x02a8
                monitor-enter(r38)
                r3 = 0
                r1.emitting = r3     // Catch:{ all -> 0x02a4 }
                monitor-exit(r38)     // Catch:{ all -> 0x02a4 }
                goto L_0x02a8
            L_0x02a4:
                r0 = move-exception
                r2 = r0
                monitor-exit(r38)     // Catch:{ all -> 0x02a4 }
                throw r2
            L_0x02a8:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OperatorMerge.MergeSubscriber.emitLoop():void");
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue<Throwable> e = this.errors;
            if (this.delayErrors || e == null || e.isEmpty()) {
                return false;
            }
            try {
                reportError();
                return true;
            } finally {
                unsubscribe();
            }
        }
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors2) {
        if (delayErrors2) {
            return HolderDelayErrors.INSTANCE;
        }
        return HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors2, int maxConcurrent2) {
        if (maxConcurrent2 <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("maxConcurrent > 0 required but it was ");
            sb.append(maxConcurrent2);
            throw new IllegalArgumentException(sb.toString());
        } else if (maxConcurrent2 == Integer.MAX_VALUE) {
            return instance(delayErrors2);
        } else {
            return new OperatorMerge<>(delayErrors2, maxConcurrent2);
        }
    }

    OperatorMerge(boolean delayErrors2, int maxConcurrent2) {
        this.delayErrors = delayErrors2;
        this.maxConcurrent = maxConcurrent2;
    }

    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> child) {
        MergeSubscriber<T> subscriber = new MergeSubscriber<>(child, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> producer = new MergeProducer<>(subscriber);
        subscriber.producer = producer;
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }
}
