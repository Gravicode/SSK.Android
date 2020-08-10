package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;

public final class LongWriteOperationBuilderImpl_Factory implements Factory<LongWriteOperationBuilderImpl> {
    private final Provider<MtuBasedPayloadSizeLimit> defaultMaxBatchSizeProvider;
    private final Provider<ConnectionOperationQueue> operationQueueProvider;
    private final Provider<OperationsProvider> operationsProvider;
    private final Provider<RxBleConnection> rxBleConnectionProvider;

    public LongWriteOperationBuilderImpl_Factory(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<MtuBasedPayloadSizeLimit> defaultMaxBatchSizeProvider2, Provider<RxBleConnection> rxBleConnectionProvider2, Provider<OperationsProvider> operationsProvider2) {
        this.operationQueueProvider = operationQueueProvider2;
        this.defaultMaxBatchSizeProvider = defaultMaxBatchSizeProvider2;
        this.rxBleConnectionProvider = rxBleConnectionProvider2;
        this.operationsProvider = operationsProvider2;
    }

    public LongWriteOperationBuilderImpl get() {
        return new LongWriteOperationBuilderImpl((ConnectionOperationQueue) this.operationQueueProvider.get(), (MtuBasedPayloadSizeLimit) this.defaultMaxBatchSizeProvider.get(), (RxBleConnection) this.rxBleConnectionProvider.get(), (OperationsProvider) this.operationsProvider.get());
    }

    public static LongWriteOperationBuilderImpl_Factory create(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<MtuBasedPayloadSizeLimit> defaultMaxBatchSizeProvider2, Provider<RxBleConnection> rxBleConnectionProvider2, Provider<OperationsProvider> operationsProvider2) {
        return new LongWriteOperationBuilderImpl_Factory(operationQueueProvider2, defaultMaxBatchSizeProvider2, rxBleConnectionProvider2, operationsProvider2);
    }

    public static LongWriteOperationBuilderImpl newLongWriteOperationBuilderImpl(ConnectionOperationQueue operationQueue, Object defaultMaxBatchSizeProvider2, RxBleConnection rxBleConnection, OperationsProvider operationsProvider2) {
        return new LongWriteOperationBuilderImpl(operationQueue, (MtuBasedPayloadSizeLimit) defaultMaxBatchSizeProvider2, rxBleConnection, operationsProvider2);
    }
}
