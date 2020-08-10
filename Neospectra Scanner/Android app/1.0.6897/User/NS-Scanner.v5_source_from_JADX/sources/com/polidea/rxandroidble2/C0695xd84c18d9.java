package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;
import p005io.reactivex.Scheduler;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideBluetoothInteractionSchedulerFactory */
public final class C0695xd84c18d9 implements Factory<Scheduler> {
    private final Provider<ExecutorService> serviceProvider;

    public C0695xd84c18d9(Provider<ExecutorService> serviceProvider2) {
        this.serviceProvider = serviceProvider2;
    }

    public Scheduler get() {
        return (Scheduler) Preconditions.checkNotNull(ClientModule.provideBluetoothInteractionScheduler((ExecutorService) this.serviceProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0695xd84c18d9 create(Provider<ExecutorService> serviceProvider2) {
        return new C0695xd84c18d9(serviceProvider2);
    }

    public static Scheduler proxyProvideBluetoothInteractionScheduler(ExecutorService service) {
        return (Scheduler) Preconditions.checkNotNull(ClientModule.provideBluetoothInteractionScheduler(service), "Cannot return null from a non-@Nullable @Provides method");
    }
}
