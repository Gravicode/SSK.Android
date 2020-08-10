package p008rx.internal.schedulers;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Scheduler.Worker;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.internal.util.PlatformDependent;
import p008rx.internal.util.RxThreadFactory;
import p008rx.internal.util.SubscriptionList;
import p008rx.internal.util.SuppressAnimalSniffer;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.Subscriptions;

/* renamed from: rx.internal.schedulers.NewThreadWorker */
public class NewThreadWorker extends Worker implements Subscription {
    private static final ConcurrentHashMap<ScheduledThreadPoolExecutor, ScheduledThreadPoolExecutor> EXECUTORS = new ConcurrentHashMap<>();
    private static final String FREQUENCY_KEY = "rx.scheduler.jdk6.purge-frequency-millis";
    private static final AtomicReference<ScheduledExecutorService> PURGE = new AtomicReference<>();
    private static final String PURGE_FORCE_KEY = "rx.scheduler.jdk6.purge-force";
    public static final int PURGE_FREQUENCY = Integer.getInteger(FREQUENCY_KEY, 1000).intValue();
    private static final String PURGE_THREAD_PREFIX = "RxSchedulerPurge-";
    private static final Object SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED = new Object();
    private static final boolean SHOULD_TRY_ENABLE_CANCEL_POLICY;
    private static volatile Object cachedSetRemoveOnCancelPolicyMethod;
    private final ScheduledExecutorService executor;
    volatile boolean isUnsubscribed;

    static {
        boolean purgeForce = Boolean.getBoolean(PURGE_FORCE_KEY);
        int androidApiVersion = PlatformDependent.getAndroidApiVersion();
        SHOULD_TRY_ENABLE_CANCEL_POLICY = !purgeForce && (androidApiVersion == 0 || androidApiVersion >= 21);
    }

    public static void registerExecutor(ScheduledThreadPoolExecutor service) {
        while (true) {
            if (((ScheduledExecutorService) PURGE.get()) != null) {
                break;
            }
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, new RxThreadFactory(PURGE_THREAD_PREFIX));
            if (PURGE.compareAndSet(null, exec)) {
                exec.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        NewThreadWorker.purgeExecutors();
                    }
                }, (long) PURGE_FREQUENCY, (long) PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
                break;
            }
            exec.shutdownNow();
        }
        EXECUTORS.putIfAbsent(service, service);
    }

    public static void deregisterExecutor(ScheduledExecutorService service) {
        EXECUTORS.remove(service);
    }

    @SuppressAnimalSniffer
    static void purgeExecutors() {
        try {
            Iterator<ScheduledThreadPoolExecutor> it = EXECUTORS.keySet().iterator();
            while (it.hasNext()) {
                ScheduledThreadPoolExecutor exec = (ScheduledThreadPoolExecutor) it.next();
                if (!exec.isShutdown()) {
                    exec.purge();
                } else {
                    it.remove();
                }
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            RxJavaHooks.onError(t);
        }
    }

    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v7, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.lang.reflect.Method] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean tryEnableCancelPolicy(java.util.concurrent.ScheduledExecutorService r6) {
        /*
            boolean r0 = SHOULD_TRY_ENABLE_CANCEL_POLICY
            r1 = 0
            if (r0 == 0) goto L_0x0047
            boolean r0 = r6 instanceof java.util.concurrent.ScheduledThreadPoolExecutor
            if (r0 == 0) goto L_0x0025
            java.lang.Object r2 = cachedSetRemoveOnCancelPolicyMethod
            java.lang.Object r3 = SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED
            if (r2 != r3) goto L_0x0010
            return r1
        L_0x0010:
            if (r2 != 0) goto L_0x0020
            java.lang.reflect.Method r3 = findSetRemoveOnCancelPolicyMethod(r6)
            if (r3 == 0) goto L_0x001a
            r4 = r3
            goto L_0x001c
        L_0x001a:
            java.lang.Object r4 = SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED
        L_0x001c:
            cachedSetRemoveOnCancelPolicyMethod = r4
            goto L_0x0023
        L_0x0020:
            r3 = r2
            java.lang.reflect.Method r3 = (java.lang.reflect.Method) r3
        L_0x0023:
            r2 = r3
            goto L_0x0029
        L_0x0025:
            java.lang.reflect.Method r2 = findSetRemoveOnCancelPolicyMethod(r6)
        L_0x0029:
            if (r2 == 0) goto L_0x0047
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ InvocationTargetException -> 0x0042, IllegalAccessException -> 0x003d, IllegalArgumentException -> 0x0038 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r3)     // Catch:{ InvocationTargetException -> 0x0042, IllegalAccessException -> 0x003d, IllegalArgumentException -> 0x0038 }
            r4[r1] = r5     // Catch:{ InvocationTargetException -> 0x0042, IllegalAccessException -> 0x003d, IllegalArgumentException -> 0x0038 }
            r2.invoke(r6, r4)     // Catch:{ InvocationTargetException -> 0x0042, IllegalAccessException -> 0x003d, IllegalArgumentException -> 0x0038 }
            return r3
        L_0x0038:
            r3 = move-exception
            p008rx.plugins.RxJavaHooks.onError(r3)
            goto L_0x0047
        L_0x003d:
            r3 = move-exception
            p008rx.plugins.RxJavaHooks.onError(r3)
            goto L_0x0046
        L_0x0042:
            r3 = move-exception
            p008rx.plugins.RxJavaHooks.onError(r3)
        L_0x0046:
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p008rx.internal.schedulers.NewThreadWorker.tryEnableCancelPolicy(java.util.concurrent.ScheduledExecutorService):boolean");
    }

    static Method findSetRemoveOnCancelPolicyMethod(ScheduledExecutorService executor2) {
        Method[] arr$;
        for (Method method : executor2.getClass().getMethods()) {
            if (method.getName().equals("setRemoveOnCancelPolicy")) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == Boolean.TYPE) {
                    return method;
                }
            }
        }
        return null;
    }

    public NewThreadWorker(ThreadFactory threadFactory) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, threadFactory);
        if (!tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
            registerExecutor((ScheduledThreadPoolExecutor) exec);
        }
        this.executor = exec;
    }

    public Subscription schedule(Action0 action) {
        return schedule(action, 0, null);
    }

    public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
        if (this.isUnsubscribed) {
            return Subscriptions.unsubscribed();
        }
        return scheduleActual(action, delayTime, unit);
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit) {
        Future<?> f;
        ScheduledAction run = new ScheduledAction(RxJavaHooks.onScheduledAction(action));
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, CompositeSubscription parent) {
        Future<?> f;
        ScheduledAction run = new ScheduledAction(RxJavaHooks.onScheduledAction(action), parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, SubscriptionList parent) {
        Future<?> f;
        ScheduledAction run = new ScheduledAction(RxJavaHooks.onScheduledAction(action), parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public void unsubscribe() {
        this.isUnsubscribed = true;
        this.executor.shutdownNow();
        deregisterExecutor(this.executor);
    }

    public boolean isUnsubscribed() {
        return this.isUnsubscribed;
    }
}
