package com.polidea.rxandroidble2.internal.serialization;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.DisconnectionRouterOutput;
import java.util.concurrent.ExecutorService;
import p005io.reactivex.Scheduler;

public final class ConnectionOperationQueueImpl_Factory implements Factory<ConnectionOperationQueueImpl> {
    private final Provider<Scheduler> callbackSchedulerProvider;
    private final Provider<String> deviceMacAddressProvider;
    private final Provider<DisconnectionRouterOutput> disconnectionRouterOutputProvider;
    private final Provider<ExecutorService> executorServiceProvider;

    public ConnectionOperationQueueImpl_Factory(Provider<String> deviceMacAddressProvider2, Provider<DisconnectionRouterOutput> disconnectionRouterOutputProvider2, Provider<ExecutorService> executorServiceProvider2, Provider<Scheduler> callbackSchedulerProvider2) {
        this.deviceMacAddressProvider = deviceMacAddressProvider2;
        this.disconnectionRouterOutputProvider = disconnectionRouterOutputProvider2;
        this.executorServiceProvider = executorServiceProvider2;
        this.callbackSchedulerProvider = callbackSchedulerProvider2;
    }

    public ConnectionOperationQueueImpl get() {
        return new ConnectionOperationQueueImpl((String) this.deviceMacAddressProvider.get(), (DisconnectionRouterOutput) this.disconnectionRouterOutputProvider.get(), (ExecutorService) this.executorServiceProvider.get(), (Scheduler) this.callbackSchedulerProvider.get());
    }

    public static ConnectionOperationQueueImpl_Factory create(Provider<String> deviceMacAddressProvider2, Provider<DisconnectionRouterOutput> disconnectionRouterOutputProvider2, Provider<ExecutorService> executorServiceProvider2, Provider<Scheduler> callbackSchedulerProvider2) {
        return new ConnectionOperationQueueImpl_Factory(deviceMacAddressProvider2, disconnectionRouterOutputProvider2, executorServiceProvider2, callbackSchedulerProvider2);
    }

    public static ConnectionOperationQueueImpl newConnectionOperationQueueImpl(String deviceMacAddress, DisconnectionRouterOutput disconnectionRouterOutput, ExecutorService executorService, Scheduler callbackScheduler) {
        return new ConnectionOperationQueueImpl(deviceMacAddress, disconnectionRouterOutput, executorService, callbackScheduler);
    }
}
