package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import p005io.reactivex.Scheduler;

public final class ConnectorImpl_Factory implements Factory<ConnectorImpl> {
    private final Provider<Scheduler> callbacksSchedulerProvider;
    private final Provider<ClientOperationQueue> clientOperationQueueProvider;
    private final Provider<Builder> connectionComponentBuilderProvider;

    public ConnectorImpl_Factory(Provider<ClientOperationQueue> clientOperationQueueProvider2, Provider<Builder> connectionComponentBuilderProvider2, Provider<Scheduler> callbacksSchedulerProvider2) {
        this.clientOperationQueueProvider = clientOperationQueueProvider2;
        this.connectionComponentBuilderProvider = connectionComponentBuilderProvider2;
        this.callbacksSchedulerProvider = callbacksSchedulerProvider2;
    }

    public ConnectorImpl get() {
        return new ConnectorImpl((ClientOperationQueue) this.clientOperationQueueProvider.get(), (Builder) this.connectionComponentBuilderProvider.get(), (Scheduler) this.callbacksSchedulerProvider.get());
    }

    public static ConnectorImpl_Factory create(Provider<ClientOperationQueue> clientOperationQueueProvider2, Provider<Builder> connectionComponentBuilderProvider2, Provider<Scheduler> callbacksSchedulerProvider2) {
        return new ConnectorImpl_Factory(clientOperationQueueProvider2, connectionComponentBuilderProvider2, callbacksSchedulerProvider2);
    }
}
