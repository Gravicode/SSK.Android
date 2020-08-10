package p008rx.internal.schedulers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Scheduler;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.functions.Action0;
import p008rx.internal.util.RxThreadFactory;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.schedulers.CachedThreadScheduler */
public final class CachedThreadScheduler extends Scheduler implements SchedulerLifecycle {
    private static final long KEEP_ALIVE_TIME = 60;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
    static final CachedWorkerPool NONE = new CachedWorkerPool(null, 0, null);
    static final ThreadWorker SHUTDOWN_THREADWORKER = new ThreadWorker(RxThreadFactory.NONE);
    final AtomicReference<CachedWorkerPool> pool = new AtomicReference<>(NONE);
    final ThreadFactory threadFactory;

    /* renamed from: rx.internal.schedulers.CachedThreadScheduler$CachedWorkerPool */
    static final class CachedWorkerPool {
        private final CompositeSubscription allWorkers;
        private final ScheduledExecutorService evictorService;
        private final Future<?> evictorTask;
        private final ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue;
        private final long keepAliveTime;
        private final ThreadFactory threadFactory;

        CachedWorkerPool(final ThreadFactory threadFactory2, long keepAliveTime2, TimeUnit unit) {
            this.threadFactory = threadFactory2;
            this.keepAliveTime = unit != null ? unit.toNanos(keepAliveTime2) : 0;
            this.expiringWorkerQueue = new ConcurrentLinkedQueue<>();
            this.allWorkers = new CompositeSubscription();
            ScheduledExecutorService evictor = null;
            Future<?> task = null;
            if (unit != null) {
                evictor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        Thread thread = threadFactory2.newThread(r);
                        StringBuilder sb = new StringBuilder();
                        sb.append(thread.getName());
                        sb.append(" (Evictor)");
                        thread.setName(sb.toString());
                        return thread;
                    }
                });
                NewThreadWorker.tryEnableCancelPolicy(evictor);
                task = evictor.scheduleWithFixedDelay(new Runnable() {
                    public void run() {
                        CachedWorkerPool.this.evictExpiredWorkers();
                    }
                }, this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
            }
            this.evictorService = evictor;
            this.evictorTask = task;
        }

        /* access modifiers changed from: 0000 */
        public ThreadWorker get() {
            if (this.allWorkers.isUnsubscribed()) {
                return CachedThreadScheduler.SHUTDOWN_THREADWORKER;
            }
            while (!this.expiringWorkerQueue.isEmpty()) {
                ThreadWorker threadWorker = (ThreadWorker) this.expiringWorkerQueue.poll();
                if (threadWorker != null) {
                    return threadWorker;
                }
            }
            ThreadWorker w = new ThreadWorker(this.threadFactory);
            this.allWorkers.add(w);
            return w;
        }

        /* access modifiers changed from: 0000 */
        public void release(ThreadWorker threadWorker) {
            threadWorker.setExpirationTime(now() + this.keepAliveTime);
            this.expiringWorkerQueue.offer(threadWorker);
        }

        /* access modifiers changed from: 0000 */
        public void evictExpiredWorkers() {
            if (!this.expiringWorkerQueue.isEmpty()) {
                long currentTimestamp = now();
                Iterator i$ = this.expiringWorkerQueue.iterator();
                while (i$.hasNext()) {
                    ThreadWorker threadWorker = (ThreadWorker) i$.next();
                    if (threadWorker.getExpirationTime() > currentTimestamp) {
                        return;
                    }
                    if (this.expiringWorkerQueue.remove(threadWorker)) {
                        this.allWorkers.remove(threadWorker);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public long now() {
            return System.nanoTime();
        }

        /* access modifiers changed from: 0000 */
        public void shutdown() {
            try {
                if (this.evictorTask != null) {
                    this.evictorTask.cancel(true);
                }
                if (this.evictorService != null) {
                    this.evictorService.shutdownNow();
                }
            } finally {
                this.allWorkers.unsubscribe();
            }
        }
    }

    /* renamed from: rx.internal.schedulers.CachedThreadScheduler$EventLoopWorker */
    static final class EventLoopWorker extends Worker implements Action0 {
        private final CompositeSubscription innerSubscription = new CompositeSubscription();
        final AtomicBoolean once;
        private final CachedWorkerPool pool;
        private final ThreadWorker threadWorker;

        EventLoopWorker(CachedWorkerPool pool2) {
            this.pool = pool2;
            this.once = new AtomicBoolean();
            this.threadWorker = pool2.get();
        }

        public void unsubscribe() {
            if (this.once.compareAndSet(false, true)) {
                this.threadWorker.schedule(this);
            }
            this.innerSubscription.unsubscribe();
        }

        public void call() {
            this.pool.release(this.threadWorker);
        }

        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action) {
            return schedule(action, 0, null);
        }

        public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
            if (this.innerSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            ScheduledAction s = this.threadWorker.scheduleActual(new Action0() {
                public void call() {
                    if (!EventLoopWorker.this.isUnsubscribed()) {
                        action.call();
                    }
                }
            }, delayTime, unit);
            this.innerSubscription.add(s);
            s.addParent(this.innerSubscription);
            return s;
        }
    }

    /* renamed from: rx.internal.schedulers.CachedThreadScheduler$ThreadWorker */
    static final class ThreadWorker extends NewThreadWorker {
        private long expirationTime = 0;

        ThreadWorker(ThreadFactory threadFactory) {
            super(threadFactory);
        }

        public long getExpirationTime() {
            return this.expirationTime;
        }

        public void setExpirationTime(long expirationTime2) {
            this.expirationTime = expirationTime2;
        }
    }

    static {
        SHUTDOWN_THREADWORKER.unsubscribe();
        NONE.shutdown();
    }

    public CachedThreadScheduler(ThreadFactory threadFactory2) {
        this.threadFactory = threadFactory2;
        start();
    }

    public void start() {
        CachedWorkerPool update = new CachedWorkerPool(this.threadFactory, KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT);
        if (!this.pool.compareAndSet(NONE, update)) {
            update.shutdown();
        }
    }

    public void shutdown() {
        CachedWorkerPool curr;
        do {
            curr = (CachedWorkerPool) this.pool.get();
            if (curr == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(curr, NONE));
        curr.shutdown();
    }

    public Worker createWorker() {
        return new EventLoopWorker((CachedWorkerPool) this.pool.get());
    }
}
