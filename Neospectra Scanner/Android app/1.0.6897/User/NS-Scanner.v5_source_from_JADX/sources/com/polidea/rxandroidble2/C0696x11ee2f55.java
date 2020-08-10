package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideConnectionQueueExecutorServiceFactory */
public final class C0696x11ee2f55 implements Factory<ExecutorService> {
    private static final C0696x11ee2f55 INSTANCE = new C0696x11ee2f55();

    public ExecutorService get() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideConnectionQueueExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0696x11ee2f55 create() {
        return INSTANCE;
    }

    public static ExecutorService proxyProvideConnectionQueueExecutorService() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideConnectionQueueExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
