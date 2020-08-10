package p008rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.internal.util.atomic.SpscAtomicArrayQueue;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;

/* renamed from: rx.internal.operators.OnSubscribePublishMulticast */
public final class OnSubscribePublishMulticast<T> extends AtomicInteger implements OnSubscribe<T>, Observer<T>, Subscription {
    static final PublishProducer<?>[] EMPTY = new PublishProducer[0];
    static final PublishProducer<?>[] TERMINATED = new PublishProducer[0];
    private static final long serialVersionUID = -3741892510772238743L;
    final boolean delayError;
    volatile boolean done;
    Throwable error;
    final ParentSubscriber<T> parent;
    final int prefetch;
    volatile Producer producer;
    final Queue<T> queue;
    volatile PublishProducer<T>[] subscribers;

    /* renamed from: rx.internal.operators.OnSubscribePublishMulticast$ParentSubscriber */
    static final class ParentSubscriber<T> extends Subscriber<T> {
        final OnSubscribePublishMulticast<T> state;

        public ParentSubscriber(OnSubscribePublishMulticast<T> state2) {
            this.state = state2;
        }

        public void onNext(T t) {
            this.state.onNext(t);
        }

        public void onError(Throwable e) {
            this.state.onError(e);
        }

        public void onCompleted() {
            this.state.onCompleted();
        }

        public void setProducer(Producer p) {
            this.state.setProducer(p);
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribePublishMulticast$PublishProducer */
    static final class PublishProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = 960704844171597367L;
        final Subscriber<? super T> actual;
        final AtomicBoolean once = new AtomicBoolean();
        final OnSubscribePublishMulticast<T> parent;

        public PublishProducer(Subscriber<? super T> actual2, OnSubscribePublishMulticast<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void request(long n) {
            if (n < 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("n >= 0 required but it was ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            } else if (n != 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }

        public boolean isUnsubscribed() {
            return this.once.get();
        }

        public void unsubscribe() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.remove(this);
            }
        }
    }

    public OnSubscribePublishMulticast(int prefetch2, boolean delayError2) {
        if (prefetch2 <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("prefetch > 0 required but it was ");
            sb.append(prefetch2);
            throw new IllegalArgumentException(sb.toString());
        }
        this.prefetch = prefetch2;
        this.delayError = delayError2;
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.queue = new SpscArrayQueue(prefetch2);
        } else {
            this.queue = new SpscAtomicArrayQueue(prefetch2);
        }
        this.subscribers = (PublishProducer[]) EMPTY;
        this.parent = new ParentSubscriber<>(this);
    }

    public void call(Subscriber<? super T> t) {
        PublishProducer<T> pp = new PublishProducer<>(t, this);
        t.add(pp);
        t.setProducer(pp);
        if (!add(pp)) {
            Throwable e = this.error;
            if (e != null) {
                t.onError(e);
            } else {
                t.onCompleted();
            }
        } else if (pp.isUnsubscribed()) {
            remove(pp);
        } else {
            drain();
        }
    }

    public void onNext(T t) {
        if (!this.queue.offer(t)) {
            this.parent.unsubscribe();
            this.error = new MissingBackpressureException("Queue full?!");
            this.done = true;
        }
        drain();
    }

    public void onError(Throwable e) {
        this.error = e;
        this.done = true;
        drain();
    }

    public void onCompleted() {
        this.done = true;
        drain();
    }

    /* access modifiers changed from: 0000 */
    public void setProducer(Producer p) {
        this.producer = p;
        p.request((long) this.prefetch);
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (getAndIncrement() == 0) {
            Queue<T> q = this.queue;
            int missed = 0;
            do {
                PublishProducer[] a = this.subscribers;
                int n = a.length;
                long r = Long.MAX_VALUE;
                for (PublishProducer publishProducer : a) {
                    r = Math.min(r, publishProducer.get());
                }
                if (n != 0) {
                    long e = 0;
                    while (e != r) {
                        boolean d = this.done;
                        T v = q.poll();
                        boolean empty = v == null;
                        if (!checkTerminated(d, empty)) {
                            if (empty) {
                                break;
                            }
                            PublishProducer[] arr$ = a;
                            int len$ = arr$.length;
                            int i$ = 0;
                            while (true) {
                                int i$2 = i$;
                                if (i$2 >= len$) {
                                    break;
                                }
                                int len$2 = len$;
                                arr$[i$2].actual.onNext(v);
                                i$ = i$2 + 1;
                                len$ = len$2;
                            }
                            e++;
                        } else {
                            return;
                        }
                    }
                    if (e == r && checkTerminated(this.done, q.isEmpty())) {
                        return;
                    }
                    if (e != 0) {
                        Producer p = this.producer;
                        if (p != null) {
                            p.request(e);
                        }
                        for (PublishProducer produced : a) {
                            BackpressureUtils.produced(produced, e);
                        }
                    }
                }
                missed = addAndGet(-missed);
            } while (missed != 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean checkTerminated(boolean d, boolean empty) {
        int i$ = 0;
        if (d) {
            if (!this.delayError) {
                Throwable ex = this.error;
                if (ex != null) {
                    this.queue.clear();
                    PublishProducer<T>[] arr$ = terminate();
                    int len$ = arr$.length;
                    while (i$ < len$) {
                        arr$[i$].actual.onError(ex);
                        i$++;
                    }
                    return true;
                } else if (empty) {
                    PublishProducer<T>[] arr$2 = terminate();
                    int len$2 = arr$2.length;
                    while (i$ < len$2) {
                        arr$2[i$].actual.onCompleted();
                        i$++;
                    }
                    return true;
                }
            } else if (empty) {
                PublishProducer<T>[] a = terminate();
                Throwable ex2 = this.error;
                if (ex2 != null) {
                    PublishProducer<T>[] arr$3 = a;
                    int len$3 = arr$3.length;
                    while (i$ < len$3) {
                        arr$3[i$].actual.onError(ex2);
                        i$++;
                    }
                } else {
                    PublishProducer<T>[] arr$4 = a;
                    int len$4 = arr$4.length;
                    while (i$ < len$4) {
                        arr$4[i$].actual.onCompleted();
                        i$++;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public PublishProducer<T>[] terminate() {
        PublishProducer<T>[] a = this.subscribers;
        if (a != TERMINATED) {
            synchronized (this) {
                a = this.subscribers;
                if (a != TERMINATED) {
                    this.subscribers = (PublishProducer[]) TERMINATED;
                }
            }
        }
        return a;
    }

    /* access modifiers changed from: 0000 */
    public boolean add(PublishProducer<T> inner) {
        if (this.subscribers == TERMINATED) {
            return false;
        }
        synchronized (this) {
            PublishProducer<T>[] a = this.subscribers;
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            PublishProducer<T>[] b = new PublishProducer[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
            this.subscribers = b;
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0048, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(p008rx.internal.operators.OnSubscribePublishMulticast.PublishProducer<T> r8) {
        /*
            r7 = this;
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<T>[] r0 = r7.subscribers
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r1 = TERMINATED
            if (r0 == r1) goto L_0x004c
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r1 = EMPTY
            if (r0 != r1) goto L_0x000b
            goto L_0x004c
        L_0x000b:
            monitor-enter(r7)
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<T>[] r1 = r7.subscribers     // Catch:{ all -> 0x0049 }
            r0 = r1
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r1 = TERMINATED     // Catch:{ all -> 0x0049 }
            if (r0 == r1) goto L_0x0047
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r1 = EMPTY     // Catch:{ all -> 0x0049 }
            if (r0 != r1) goto L_0x0018
            goto L_0x0047
        L_0x0018:
            r1 = -1
            int r2 = r0.length     // Catch:{ all -> 0x0049 }
            r3 = 0
            r4 = 0
        L_0x001c:
            if (r4 >= r2) goto L_0x0027
            r5 = r0[r4]     // Catch:{ all -> 0x0049 }
            if (r5 != r8) goto L_0x0024
            r1 = r4
            goto L_0x0027
        L_0x0024:
            int r4 = r4 + 1
            goto L_0x001c
        L_0x0027:
            if (r1 >= 0) goto L_0x002b
            monitor-exit(r7)     // Catch:{ all -> 0x0049 }
            return
        L_0x002b:
            r4 = 1
            if (r2 != r4) goto L_0x0033
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer<?>[] r3 = EMPTY     // Catch:{ all -> 0x0049 }
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer[] r3 = (p008rx.internal.operators.OnSubscribePublishMulticast.PublishProducer[]) r3     // Catch:{ all -> 0x0049 }
            goto L_0x0043
        L_0x0033:
            int r5 = r2 + -1
            rx.internal.operators.OnSubscribePublishMulticast$PublishProducer[] r5 = new p008rx.internal.operators.OnSubscribePublishMulticast.PublishProducer[r5]     // Catch:{ all -> 0x0049 }
            java.lang.System.arraycopy(r0, r3, r5, r3, r1)     // Catch:{ all -> 0x0049 }
            int r3 = r1 + 1
            int r6 = r2 - r1
            int r6 = r6 - r4
            java.lang.System.arraycopy(r0, r3, r5, r1, r6)     // Catch:{ all -> 0x0049 }
            r3 = r5
        L_0x0043:
            r7.subscribers = r3     // Catch:{ all -> 0x0049 }
            monitor-exit(r7)     // Catch:{ all -> 0x0049 }
            return
        L_0x0047:
            monitor-exit(r7)     // Catch:{ all -> 0x0049 }
            return
        L_0x0049:
            r1 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0049 }
            throw r1
        L_0x004c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.operators.OnSubscribePublishMulticast.remove(rx.internal.operators.OnSubscribePublishMulticast$PublishProducer):void");
    }

    public Subscriber<T> subscriber() {
        return this.parent;
    }

    public void unsubscribe() {
        this.parent.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return this.parent.isUnsubscribed();
    }
}
