package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattDescriptor;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import p005io.reactivex.Completable;

@ConnectionScope
class DescriptorWriter {
    private final ConnectionOperationQueue operationQueue;
    private final OperationsProvider operationsProvider;

    @Inject
    DescriptorWriter(ConnectionOperationQueue operationQueue2, OperationsProvider operationsProvider2) {
        this.operationQueue = operationQueue2;
        this.operationsProvider = operationsProvider2;
    }

    /* access modifiers changed from: 0000 */
    public Completable writeDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] data) {
        return this.operationQueue.queue(this.operationsProvider.provideWriteDescriptor(bluetoothGattDescriptor, data)).ignoreElements();
    }
}
