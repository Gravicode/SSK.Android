package p008rx.internal.util;

import java.io.PrintStream;
import java.util.Queue;
import p008rx.Observer;
import p008rx.Subscription;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.internal.operators.NotificationLite;
import p008rx.internal.util.unsafe.SpmcArrayQueue;
import p008rx.internal.util.unsafe.SpscArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;

/* renamed from: rx.internal.util.RxRingBuffer */
public class RxRingBuffer implements Subscription {
    public static final int SIZE;
    public static final ObjectPool<Queue<Object>> SPMC_POOL = new ObjectPool<Queue<Object>>() {
        /* access modifiers changed from: protected */
        public SpmcArrayQueue<Object> createObject() {
            return new SpmcArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    public static final ObjectPool<Queue<Object>> SPSC_POOL = new ObjectPool<Queue<Object>>() {
        /* access modifiers changed from: protected */
        public SpscArrayQueue<Object> createObject() {
            return new SpscArrayQueue<>(RxRingBuffer.SIZE);
        }
    };
    private final ObjectPool<Queue<Object>> pool;
    private Queue<Object> queue;
    private final int size;
    public volatile Object terminalState;

    static {
        int defaultSize = 128;
        if (PlatformDependent.isAndroid()) {
            defaultSize = 16;
        }
        String sizeFromProperty = System.getProperty("rx.ring-buffer.size");
        if (sizeFromProperty != null) {
            try {
                defaultSize = Integer.parseInt(sizeFromProperty);
            } catch (NumberFormatException e) {
                PrintStream printStream = System.err;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to set 'rx.buffer.size' with value ");
                sb.append(sizeFromProperty);
                sb.append(" => ");
                sb.append(e.getMessage());
                printStream.println(sb.toString());
            }
        }
        SIZE = defaultSize;
    }

    public static RxRingBuffer getSpscInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPSC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    public static RxRingBuffer getSpmcInstance() {
        if (UnsafeAccess.isUnsafeAvailable()) {
            return new RxRingBuffer(SPMC_POOL, SIZE);
        }
        return new RxRingBuffer();
    }

    private RxRingBuffer(Queue<Object> queue2, int size2) {
        this.queue = queue2;
        this.pool = null;
        this.size = size2;
    }

    private RxRingBuffer(ObjectPool<Queue<Object>> pool2, int size2) {
        this.pool = pool2;
        this.queue = (Queue) pool2.borrowObject();
        this.size = size2;
    }

    public synchronized void release() {
        Queue<Object> q = this.queue;
        ObjectPool<Queue<Object>> p = this.pool;
        if (!(p == null || q == null)) {
            q.clear();
            this.queue = null;
            p.returnObject(q);
        }
    }

    public void unsubscribe() {
        release();
    }

    RxRingBuffer() {
        this((Queue<Object>) new SynchronizedQueue<Object>(SIZE), SIZE);
    }

    public void onNext(Object o) throws MissingBackpressureException {
        boolean iae = false;
        boolean mbe = false;
        synchronized (this) {
            Queue<Object> q = this.queue;
            if (q != null) {
                mbe = !q.offer(NotificationLite.next(o));
            } else {
                iae = true;
            }
        }
        if (iae) {
            throw new IllegalStateException("This instance has been unsubscribed and the queue is no longer usable.");
        } else if (mbe) {
            throw new MissingBackpressureException();
        }
    }

    public void onCompleted() {
        if (this.terminalState == null) {
            this.terminalState = NotificationLite.completed();
        }
    }

    public void onError(Throwable t) {
        if (this.terminalState == null) {
            this.terminalState = NotificationLite.error(t);
        }
    }

    public int available() {
        return this.size - count();
    }

    public int capacity() {
        return this.size;
    }

    public int count() {
        Queue<Object> q = this.queue;
        if (q == null) {
            return 0;
        }
        return q.size();
    }

    public boolean isEmpty() {
        Queue<Object> q = this.queue;
        return q == null || q.isEmpty();
    }

    public Object poll() {
        Object o;
        synchronized (this) {
            try {
                Queue<Object> q = this.queue;
                if (q == null) {
                    return null;
                }
                o = q.poll();
                Object ts = this.terminalState;
                if (o == null && ts != null && q.peek() == null) {
                    o = ts;
                    this.terminalState = null;
                }
                Object o2 = o;
                return o2;
            } catch (Throwable th) {
                th = th;
                throw th;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001b, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object peek() {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 0
            java.util.Queue<java.lang.Object> r1 = r4.queue     // Catch:{ all -> 0x001c }
            if (r1 != 0) goto L_0x0008
            monitor-exit(r4)     // Catch:{ all -> 0x001c }
            return r0
        L_0x0008:
            java.lang.Object r2 = r1.peek()     // Catch:{ all -> 0x001c }
            r0 = r2
            java.lang.Object r2 = r4.terminalState     // Catch:{ all -> 0x001c }
            if (r0 != 0) goto L_0x001a
            if (r2 == 0) goto L_0x001a
            java.lang.Object r3 = r1.peek()     // Catch:{ all -> 0x001c }
            if (r3 != 0) goto L_0x001a
            r0 = r2
        L_0x001a:
            monitor-exit(r4)     // Catch:{ all -> 0x001c }
            return r0
        L_0x001c:
            r1 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x001c }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.util.RxRingBuffer.peek():java.lang.Object");
    }

    public boolean isCompleted(Object o) {
        return NotificationLite.isCompleted(o);
    }

    public boolean isError(Object o) {
        return NotificationLite.isError(o);
    }

    public Object getValue(Object o) {
        return NotificationLite.getValue(o);
    }

    public boolean accept(Object o, Observer child) {
        return NotificationLite.accept(child, o);
    }

    public Throwable asError(Object o) {
        return NotificationLite.getError(o);
    }

    public boolean isUnsubscribed() {
        return this.queue == null;
    }
}
