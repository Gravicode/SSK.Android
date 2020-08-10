package p008rx.internal.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.CompositeException;
import p008rx.functions.FuncN;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.atomic.SpscLinkedArrayQueue;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.operators.OnSubscribeCombineLatest */
public final class OnSubscribeCombineLatest<T, R> implements OnSubscribe<R> {
    final int bufferSize;
    final FuncN<? extends R> combiner;
    final boolean delayError;
    final Observable<? extends T>[] sources;
    final Iterable<? extends Observable<? extends T>> sourcesIterable;

    /* renamed from: rx.internal.operators.OnSubscribeCombineLatest$CombinerSubscriber */
    static final class CombinerSubscriber<T, R> extends Subscriber<T> {
        boolean done;
        final int index;
        final LatestCoordinator<T, R> parent;

        public CombinerSubscriber(LatestCoordinator<T, R> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
            request((long) parent2.bufferSize);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.parent.combine(NotificationLite.next(t), this.index);
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaHooks.onError(t);
                return;
            }
            this.parent.onError(t);
            this.done = true;
            this.parent.combine(null, this.index);
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.parent.combine(null, this.index);
            }
        }

        public void requestMore(long n) {
            request(n);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeCombineLatest$LatestCoordinator */
    static final class LatestCoordinator<T, R> extends AtomicInteger implements Producer, Subscription {
        static final Object MISSING = new Object();
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final FuncN<? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Object[] latest;
        final SpscLinkedArrayQueue<Object> queue;
        final AtomicLong requested = new AtomicLong();
        final CombinerSubscriber<T, R>[] subscribers;

        public LatestCoordinator(Subscriber<? super R> actual2, FuncN<? extends R> combiner2, int count, int bufferSize2, boolean delayError2) {
            this.actual = actual2;
            this.combiner = combiner2;
            this.bufferSize = bufferSize2;
            this.delayError = delayError2;
            this.latest = new Object[count];
            Arrays.fill(this.latest, MISSING);
            this.subscribers = new CombinerSubscriber[count];
            this.queue = new SpscLinkedArrayQueue<>(bufferSize2);
        }

        public void subscribe(Observable<? extends T>[] sources) {
            Subscriber<T>[] as = this.subscribers;
            int len = as.length;
            for (int i = 0; i < len; i++) {
                as[i] = new CombinerSubscriber<>(this, i);
            }
            lazySet(0);
            this.actual.add(this);
            this.actual.setProducer(this);
            for (int i2 = 0; i2 < len && !this.cancelled; i2++) {
                sources[i2].subscribe(as[i2]);
            }
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                drain();
            }
        }

        public void unsubscribe() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    cancel(this.queue);
                }
            }
        }

        public boolean isUnsubscribed() {
            return this.cancelled;
        }

        /* access modifiers changed from: 0000 */
        public void cancel(Queue<?> q) {
            q.clear();
            for (CombinerSubscriber<T, R> s : this.subscribers) {
                s.unsubscribe();
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0039, code lost:
            if (r3 == MISSING) goto L_0x0041;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x006c, code lost:
            if (r7 != false) goto L_0x0076;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x006e, code lost:
            if (r12 == null) goto L_0x0076;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0070, code lost:
            r0.requestMore(1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0075, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x0076, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0079, code lost:
            return;
         */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0044  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0069 A[Catch:{ all -> 0x007a }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void combine(java.lang.Object r12, int r13) {
            /*
                r11 = this;
                rx.internal.operators.OnSubscribeCombineLatest$CombinerSubscriber<T, R>[] r0 = r11.subscribers
                r0 = r0[r13]
                monitor-enter(r11)
                r1 = 0
                java.lang.Object[] r2 = r11.latest     // Catch:{ all -> 0x0084 }
                int r2 = r2.length     // Catch:{ all -> 0x0084 }
                java.lang.Object[] r3 = r11.latest     // Catch:{ all -> 0x0082 }
                r3 = r3[r13]     // Catch:{ all -> 0x0082 }
                int r4 = r11.active     // Catch:{ all -> 0x0082 }
                java.lang.Object r5 = MISSING     // Catch:{ all -> 0x007f }
                if (r3 != r5) goto L_0x0017
                int r4 = r4 + 1
                r11.active = r4     // Catch:{ all -> 0x007f }
            L_0x0017:
                int r5 = r11.complete     // Catch:{ all -> 0x007f }
                if (r12 != 0) goto L_0x0025
                int r5 = r5 + 1
                r11.complete = r5     // Catch:{ all -> 0x0020 }
                goto L_0x002d
            L_0x0020:
                r3 = move-exception
                r1 = r4
                r4 = 0
                goto L_0x0088
            L_0x0025:
                java.lang.Object[] r6 = r11.latest     // Catch:{ all -> 0x0020 }
                java.lang.Object r7 = p008rx.internal.operators.NotificationLite.getValue(r12)     // Catch:{ all -> 0x0020 }
                r6[r13] = r7     // Catch:{ all -> 0x0020 }
            L_0x002d:
                r6 = 1
                if (r4 != r2) goto L_0x0032
                r7 = 1
                goto L_0x0033
            L_0x0032:
                r7 = 0
            L_0x0033:
                if (r5 == r2) goto L_0x0041
                if (r12 != 0) goto L_0x0040
                java.lang.Object r8 = MISSING     // Catch:{ all -> 0x003c }
                if (r3 != r8) goto L_0x0040
                goto L_0x0041
            L_0x003c:
                r3 = move-exception
                r1 = r4
                r4 = 0
                goto L_0x0089
            L_0x0040:
                goto L_0x0042
            L_0x0041:
                r1 = 1
            L_0x0042:
                if (r1 != 0) goto L_0x0069
                if (r12 == 0) goto L_0x0054
                if (r7 == 0) goto L_0x0054
                rx.internal.util.atomic.SpscLinkedArrayQueue<java.lang.Object> r6 = r11.queue     // Catch:{ all -> 0x007a }
                java.lang.Object[] r8 = r11.latest     // Catch:{ all -> 0x007a }
                java.lang.Object r8 = r8.clone()     // Catch:{ all -> 0x007a }
                r6.offer(r0, r8)     // Catch:{ all -> 0x007a }
                goto L_0x006b
            L_0x0054:
                if (r12 != 0) goto L_0x006b
                java.util.concurrent.atomic.AtomicReference<java.lang.Throwable> r8 = r11.error     // Catch:{ all -> 0x007a }
                java.lang.Object r8 = r8.get()     // Catch:{ all -> 0x007a }
                if (r8 == 0) goto L_0x006b
                java.lang.Object r8 = MISSING     // Catch:{ all -> 0x007a }
                if (r3 == r8) goto L_0x0066
                boolean r8 = r11.delayError     // Catch:{ all -> 0x007a }
                if (r8 != 0) goto L_0x006b
            L_0x0066:
                r11.done = r6     // Catch:{ all -> 0x007a }
                goto L_0x006b
            L_0x0069:
                r11.done = r6     // Catch:{ all -> 0x007a }
            L_0x006b:
                monitor-exit(r11)     // Catch:{ all -> 0x007a }
                if (r7 != 0) goto L_0x0076
                if (r12 == 0) goto L_0x0076
                r8 = 1
                r0.requestMore(r8)
                return
            L_0x0076:
                r11.drain()
                return
            L_0x007a:
                r3 = move-exception
                r10 = r4
                r4 = r1
                r1 = r10
                goto L_0x0089
            L_0x007f:
                r3 = move-exception
                r1 = r4
                goto L_0x0086
            L_0x0082:
                r3 = move-exception
                goto L_0x0086
            L_0x0084:
                r3 = move-exception
                r2 = 0
            L_0x0086:
                r4 = 0
                r5 = 0
            L_0x0088:
                r7 = 0
            L_0x0089:
                monitor-exit(r11)     // Catch:{ all -> 0x008b }
                throw r3
            L_0x008b:
                r3 = move-exception
                goto L_0x0089
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OnSubscribeCombineLatest.LatestCoordinator.combine(java.lang.Object, int):void");
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            long emitted;
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
                Subscriber<? super R> a = this.actual;
                boolean delayError2 = this.delayError;
                AtomicLong localRequested = this.requested;
                int missed = 1;
                do {
                    int missed2 = missed;
                    if (!checkTerminated(this.done, spscLinkedArrayQueue.isEmpty(), a, spscLinkedArrayQueue, delayError2)) {
                        long requestAmount = localRequested.get();
                        long emitted2 = 0;
                        while (true) {
                            emitted = emitted2;
                            if (emitted == requestAmount) {
                                break;
                            }
                            boolean d = this.done;
                            CombinerSubscriber<T, R> cs = (CombinerSubscriber) spscLinkedArrayQueue.peek();
                            boolean empty = cs == null;
                            CombinerSubscriber<T, R> cs2 = cs;
                            boolean z = d;
                            long emitted3 = emitted;
                            if (!checkTerminated(d, empty, a, spscLinkedArrayQueue, delayError2)) {
                                if (empty) {
                                    emitted = emitted3;
                                    break;
                                }
                                spscLinkedArrayQueue.poll();
                                Object[] array = (Object[]) spscLinkedArrayQueue.poll();
                                if (array == null) {
                                    this.cancelled = true;
                                    cancel(spscLinkedArrayQueue);
                                    a.onError(new IllegalStateException("Broken queue?! Sender received but not the array."));
                                    return;
                                }
                                try {
                                    a.onNext(this.combiner.call(array));
                                    cs2.requestMore(1);
                                    emitted2 = emitted3 + 1;
                                } catch (Throwable th) {
                                    CombinerSubscriber combinerSubscriber = cs2;
                                    long j = emitted3;
                                    Throwable ex = th;
                                    this.cancelled = true;
                                    cancel(spscLinkedArrayQueue);
                                    a.onError(ex);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(emitted == 0 || requestAmount == Long.MAX_VALUE)) {
                            BackpressureUtils.produced(localRequested, emitted);
                        }
                        missed = addAndGet(-missed2);
                    } else {
                        return;
                    }
                } while (missed != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean mainDone, boolean queueEmpty, Subscriber<?> childSubscriber, Queue<?> q, boolean delayError2) {
            if (this.cancelled) {
                cancel(q);
                return true;
            }
            if (mainDone) {
                if (!delayError2) {
                    Throwable e = (Throwable) this.error.get();
                    if (e != null) {
                        cancel(q);
                        childSubscriber.onError(e);
                        return true;
                    } else if (queueEmpty) {
                        childSubscriber.onCompleted();
                        return true;
                    }
                } else if (queueEmpty) {
                    Throwable e2 = (Throwable) this.error.get();
                    if (e2 != null) {
                        childSubscriber.onError(e2);
                    } else {
                        childSubscriber.onCompleted();
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        public void onError(Throwable e) {
            Throwable curr;
            Throwable next;
            AtomicReference<Throwable> localError = this.error;
            do {
                curr = (Throwable) localError.get();
                if (curr == null) {
                    next = e;
                } else if (curr instanceof CompositeException) {
                    List<Throwable> es = new ArrayList<>(((CompositeException) curr).getExceptions());
                    es.add(e);
                    next = new CompositeException((Collection<? extends Throwable>) es);
                } else {
                    next = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{curr, e}));
                }
            } while (!localError.compareAndSet(curr, next));
        }
    }

    public OnSubscribeCombineLatest(Iterable<? extends Observable<? extends T>> sourcesIterable2, FuncN<? extends R> combiner2) {
        this(null, sourcesIterable2, combiner2, RxRingBuffer.SIZE, false);
    }

    public OnSubscribeCombineLatest(Observable<? extends T>[] sources2, Iterable<? extends Observable<? extends T>> sourcesIterable2, FuncN<? extends R> combiner2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.combiner = combiner2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void call(Subscriber<? super R> s) {
        Observable<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 != null) {
            count = sources2.length;
        } else if (this.sourcesIterable instanceof List) {
            List list = (List) this.sourcesIterable;
            sources2 = (Observable[]) list.toArray(new Observable[list.size()]);
            count = sources2.length;
        } else {
            sources2 = new Observable[8];
            for (Observable<? extends T> p : this.sourcesIterable) {
                if (count == sources2.length) {
                    Observable<? extends T>[] b = new Observable[((count >> 2) + count)];
                    System.arraycopy(sources2, 0, b, 0, count);
                    sources2 = b;
                }
                int count2 = count + 1;
                sources2[count] = p;
                count = count2;
            }
        }
        if (count == 0) {
            s.onCompleted();
            return;
        }
        LatestCoordinator<T, R> lc = new LatestCoordinator<>(s, this.combiner, count, this.bufferSize, this.delayError);
        lc.subscribe(sources2);
    }
}
