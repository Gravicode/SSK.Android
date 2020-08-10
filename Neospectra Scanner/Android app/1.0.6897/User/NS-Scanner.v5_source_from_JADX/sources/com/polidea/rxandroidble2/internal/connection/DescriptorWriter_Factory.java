package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;

public final class DescriptorWriter_Factory implements Factory<DescriptorWriter> {
    private final Provider<ConnectionOperationQueue> operationQueueProvider;
    private final Provider<OperationsProvider> operationsProvider;

    public DescriptorWriter_Factory(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<OperationsProvider> operationsProvider2) {
        this.operationQueueProvider = operationQueueProvider2;
        this.operationsProvider = operationsProvider2;
    }

    public DescriptorWriter get() {
        return new DescriptorWriter((ConnectionOperationQueue) this.operationQueueProvider.get(), (OperationsProvider) this.operationsProvider.get());
    }

    public static DescriptorWriter_Factory create(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<OperationsProvider> operationsProvider2) {
        return new DescriptorWriter_Factory(operationQueueProvider2, operationsProvider2);
    }

    public static DescriptorWriter newDescriptorWriter(ConnectionOperationQueue operationQueue, OperationsProvider operationsProvider2) {
        return new DescriptorWriter(operationQueue, operationsProvider2);
    }
}
