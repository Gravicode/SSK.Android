package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideBluetoothCallbacksExecutorServiceFactory */
public final class C0692x4a2ba98e implements Factory<ExecutorService> {
    private static final C0692x4a2ba98e INSTANCE = new C0692x4a2ba98e();

    public ExecutorService get() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideBluetoothCallbacksExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0692x4a2ba98e create() {
        return INSTANCE;
    }

    public static ExecutorService proxyProvideBluetoothCallbacksExecutorService() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideBluetoothCallbacksExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
