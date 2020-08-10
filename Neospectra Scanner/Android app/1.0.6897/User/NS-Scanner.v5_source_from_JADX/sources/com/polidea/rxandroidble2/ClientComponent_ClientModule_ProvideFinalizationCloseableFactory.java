package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientComponentFinalizer;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import java.util.concurrent.ExecutorService;

public final class ClientComponent_ClientModule_ProvideFinalizationCloseableFactory implements Factory<ClientComponentFinalizer> {
    private final Provider<ExecutorService> callbacksExecutorServiceProvider;
    private final Provider<ExecutorService> connectionQueueExecutorServiceProvider;
    private final Provider<ExecutorService> interactionExecutorServiceProvider;

    public ClientComponent_ClientModule_ProvideFinalizationCloseableFactory(Provider<ExecutorService> interactionExecutorServiceProvider2, Provider<ExecutorService> callbacksExecutorServiceProvider2, Provider<ExecutorService> connectionQueueExecutorServiceProvider2) {
        this.interactionExecutorServiceProvider = interactionExecutorServiceProvider2;
        this.callbacksExecutorServiceProvider = callbacksExecutorServiceProvider2;
        this.connectionQueueExecutorServiceProvider = connectionQueueExecutorServiceProvider2;
    }

    public ClientComponentFinalizer get() {
        return (ClientComponentFinalizer) Preconditions.checkNotNull(ClientModule.provideFinalizationCloseable((ExecutorService) this.interactionExecutorServiceProvider.get(), (ExecutorService) this.callbacksExecutorServiceProvider.get(), (ExecutorService) this.connectionQueueExecutorServiceProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideFinalizationCloseableFactory create(Provider<ExecutorService> interactionExecutorServiceProvider2, Provider<ExecutorService> callbacksExecutorServiceProvider2, Provider<ExecutorService> connectionQueueExecutorServiceProvider2) {
        return new ClientComponent_ClientModule_ProvideFinalizationCloseableFactory(interactionExecutorServiceProvider2, callbacksExecutorServiceProvider2, connectionQueueExecutorServiceProvider2);
    }

    public static ClientComponentFinalizer proxyProvideFinalizationCloseable(ExecutorService interactionExecutorService, ExecutorService callbacksExecutorService, ExecutorService connectionQueueExecutorService) {
        return (ClientComponentFinalizer) Preconditions.checkNotNull(ClientModule.provideFinalizationCloseable(interactionExecutorService, callbacksExecutorService, connectionQueueExecutorService), "Cannot return null from a non-@Nullable @Provides method");
    }
}
