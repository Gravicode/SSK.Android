package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;

public final class ServiceDiscoveryManager_Factory implements Factory<ServiceDiscoveryManager> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<OperationsProvider> operationProvider;
    private final Provider<ConnectionOperationQueue> operationQueueProvider;

    public ServiceDiscoveryManager_Factory(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<OperationsProvider> operationProvider2) {
        this.operationQueueProvider = operationQueueProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.operationProvider = operationProvider2;
    }

    public ServiceDiscoveryManager get() {
        return new ServiceDiscoveryManager((ConnectionOperationQueue) this.operationQueueProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (OperationsProvider) this.operationProvider.get());
    }

    public static ServiceDiscoveryManager_Factory create(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<OperationsProvider> operationProvider2) {
        return new ServiceDiscoveryManager_Factory(operationQueueProvider2, bluetoothGattProvider2, operationProvider2);
    }

    public static ServiceDiscoveryManager newServiceDiscoveryManager(ConnectionOperationQueue operationQueue, BluetoothGatt bluetoothGatt, OperationsProvider operationProvider2) {
        return new ServiceDiscoveryManager(operationQueue, bluetoothGatt, operationProvider2);
    }
}
