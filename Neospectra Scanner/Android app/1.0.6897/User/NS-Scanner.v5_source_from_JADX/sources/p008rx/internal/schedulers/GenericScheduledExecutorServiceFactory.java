package p008rx.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import p008rx.functions.Func0;
import p008rx.internal.util.RxThreadFactory;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.internal.schedulers.GenericScheduledExecutorServiceFactory */
enum GenericScheduledExecutorServiceFactory {
    ;
    
    static final RxThreadFactory THREAD_FACTORY = null;
    static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";

    static {
        THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    }

    static ThreadFactory threadFactory() {
        return THREAD_FACTORY;
    }

    public static ScheduledExecutorService create() {
        Func0<? extends ScheduledExecutorService> f = RxJavaHooks.getOnGenericScheduledExecutorService();
        if (f == null) {
            return createDefault();
        }
        return (ScheduledExecutorService) f.call();
    }

    static ScheduledExecutorService createDefault() {
        return Executors.newScheduledThreadPool(1, threadFactory());
    }
}
