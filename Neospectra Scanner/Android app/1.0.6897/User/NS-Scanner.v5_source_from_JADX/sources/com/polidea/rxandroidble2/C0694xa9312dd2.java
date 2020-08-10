package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideBluetoothInteractionExecutorServiceFactory */
public final class C0694xa9312dd2 implements Factory<ExecutorService> {
    private static final C0694xa9312dd2 INSTANCE = new C0694xa9312dd2();

    public ExecutorService get() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideBluetoothInteractionExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0694xa9312dd2 create() {
        return INSTANCE;
    }

    public static ExecutorService proxyProvideBluetoothInteractionExecutorService() {
        return (ExecutorService) Preconditions.checkNotNull(ClientModule.provideBluetoothInteractionExecutorService(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
