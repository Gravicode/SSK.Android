package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;
import p005io.reactivex.Scheduler;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideBluetoothCallbacksSchedulerFactory */
public final class C0693x76cd1195 implements Factory<Scheduler> {
    private final Provider<ExecutorService> serviceProvider;

    public C0693x76cd1195(Provider<ExecutorService> serviceProvider2) {
        this.serviceProvider = serviceProvider2;
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(ClientModule.provideBluetoothCallbacksScheduler((ExecutorService) this.serviceProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0693x76cd1195 create(Provider<ExecutorService> serviceProvider2) {
        return new C0693x76cd1195(serviceProvider2);
    }

    public static Scheduler proxyProvideBluetoothCallbacksScheduler(ExecutorService service) {
        return (Scheduler) Preconditions.checkNotNull(ClientModule.provideBluetoothCallbacksScheduler(service), "Cannot return null from a non-@Nullable @Provides method");
    }
}
