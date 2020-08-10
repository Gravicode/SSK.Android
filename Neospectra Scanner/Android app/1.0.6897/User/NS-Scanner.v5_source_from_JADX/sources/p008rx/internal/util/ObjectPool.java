package p008rx.internal.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.internal.schedulers.GenericScheduledExecutorService;
import p008rx.internal.schedulers.SchedulerLifecycle;
import p008rx.internal.util.unsafe.MpmcArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.util.ObjectPool */
public abstract class ObjectPool<T> implements SchedulerLifecycle {
    final int maxSize;
    final int minSize;
    private final AtomicReference<Future<?>> periodicTask;
    Queue<T> pool;
    private final long validationInterval;

    /* access modifiers changed from: protected */
    public abstract T createObject();

    public ObjectPool() {
        this(0, 0, 67);
    }

    private ObjectPool(int min, int max, long validationInterval2) {
        this.minSize = min;
        this.maxSize = max;
        this.validationInterval = validationInterval2;
        this.periodicTask = new AtomicReference<>();
        initialize(min);
        start();
    }

    public T borrowObject() {
        Object poll = this.pool.poll();
        Object obj = poll;
        if (poll == null) {
            return createObject();
        }
        return obj;
    }

    public void returnObject(T object) {
        if (object != null) {
            this.pool.offer(object);
        }
    }

    public void shutdown() {
        Future<?> f = (Future) this.periodicTask.getAndSet(null);
        if (f != null) {
            f.cancel(false);
        }
    }

    public void start() {
        while (this.periodicTask.get() == null) {
            try {
                Future<?> f = GenericScheduledExecutorService.getInstance().scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        int size = ObjectPool.this.pool.size();
                        int i = 0;
                        if (size < ObjectPool.this.minSize) {
                            int sizeToBeAdded = ObjectPool.this.maxSize - size;
                            while (i < sizeToBeAdded) {
                                ObjectPool.this.pool.add(ObjectPool.this.createObject());
                                i++;
                            }
                        } else if (size > ObjectPool.this.maxSize) {
                            int sizeToBeRemoved = size - ObjectPool.this.maxSize;
                            while (i < sizeToBeRemoved) {
                                ObjectPool.this.pool.poll();
                                i++;
                            }
                        }
                    }
                }, this.validationInterval, this.validationInterval, TimeUnit.SECONDS);
                if (!this.periodicTask.compareAndSet(null, f)) {
                    f.cancel(false);
                } else {
                    return;
                }
            } catch (RejectedExecutionException ex) {
                RxJavaHooks.onError(ex);
            }
        }
    }

    private void initialize(int min) {
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.pool = new MpmcArrayQueue(Math.max(this.maxSize, 1024));
        } else {
            this.pool = new ConcurrentLinkedQueue();
        }
        for (int i = 0; i < min; i++) {
            this.pool.add(createObject());
        }
    }
}
