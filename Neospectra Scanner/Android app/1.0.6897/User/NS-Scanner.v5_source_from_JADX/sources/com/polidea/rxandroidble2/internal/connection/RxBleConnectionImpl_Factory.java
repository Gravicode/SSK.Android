package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleConnection.LongWriteOperationBuilder;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import p005io.reactivex.Scheduler;

public final class RxBleConnectionImpl_Factory implements Factory<RxBleConnectionImpl> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<Scheduler> callbackSchedulerProvider;
    private final Provider<DescriptorWriter> descriptorWriterProvider;
    private final Provider<RxBleGattCallback> gattCallbackProvider;
    private final Provider<IllegalOperationChecker> illegalOperationCheckerProvider;
    private final Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider;
    private final Provider<MtuProvider> mtuProvider;
    private final Provider<NotificationAndIndicationManager> notificationIndicationManagerProvider;
    private final Provider<OperationsProvider> operationProvider;
    private final Provider<ConnectionOperationQueue> operationQueueProvider;
    private final Provider<ServiceDiscoveryManager> serviceDiscoveryManagerProvider;

    public RxBleConnectionImpl_Factory(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<RxBleGattCallback> gattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<ServiceDiscoveryManager> serviceDiscoveryManagerProvider2, Provider<NotificationAndIndicationManager> notificationIndicationManagerProvider2, Provider<MtuProvider> mtuProvider2, Provider<DescriptorWriter> descriptorWriterProvider2, Provider<OperationsProvider> operationProvider2, Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider2, Provider<Scheduler> callbackSchedulerProvider2, Provider<IllegalOperationChecker> illegalOperationCheckerProvider2) {
        this.operationQueueProvider = operationQueueProvider2;
        this.gattCallbackProvider = gattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.serviceDiscoveryManagerProvider = serviceDiscoveryManagerProvider2;
        this.notificationIndicationManagerProvider = notificationIndicationManagerProvider2;
        this.mtuProvider = mtuProvider2;
        this.descriptorWriterProvider = descriptorWriterProvider2;
        this.operationProvider = operationProvider2;
        this.longWriteOperationBuilderProvider = longWriteOperationBuilderProvider2;
        this.callbackSchedulerProvider = callbackSchedulerProvider2;
        this.illegalOperationCheckerProvider = illegalOperationCheckerProvider2;
    }

    public RxBleConnectionImpl get() {
        RxBleConnectionImpl rxBleConnectionImpl = new RxBleConnectionImpl((ConnectionOperationQueue) this.operationQueueProvider.get(), (RxBleGattCallback) this.gattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (ServiceDiscoveryManager) this.serviceDiscoveryManagerProvider.get(), (NotificationAndIndicationManager) this.notificationIndicationManagerProvider.get(), (MtuProvider) this.mtuProvider.get(), (DescriptorWriter) this.descriptorWriterProvider.get(), (OperationsProvider) this.operationProvider.get(), this.longWriteOperationBuilderProvider, (Scheduler) this.callbackSchedulerProvider.get(), (IllegalOperationChecker) this.illegalOperationCheckerProvider.get());
        return rxBleConnectionImpl;
    }

    public static RxBleConnectionImpl_Factory create(Provider<ConnectionOperationQueue> operationQueueProvider2, Provider<RxBleGattCallback> gattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<ServiceDiscoveryManager> serviceDiscoveryManagerProvider2, Provider<NotificationAndIndicationManager> notificationIndicationManagerProvider2, Provider<MtuProvider> mtuProvider2, Provider<DescriptorWriter> descriptorWriterProvider2, Provider<OperationsProvider> operationProvider2, Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider2, Provider<Scheduler> callbackSchedulerProvider2, Provider<IllegalOperationChecker> illegalOperationCheckerProvider2) {
        RxBleConnectionImpl_Factory rxBleConnectionImpl_Factory = new RxBleConnectionImpl_Factory(operationQueueProvider2, gattCallbackProvider2, bluetoothGattProvider2, serviceDiscoveryManagerProvider2, notificationIndicationManagerProvider2, mtuProvider2, descriptorWriterProvider2, operationProvider2, longWriteOperationBuilderProvider2, callbackSchedulerProvider2, illegalOperationCheckerProvider2);
        return rxBleConnectionImpl_Factory;
    }

    public static RxBleConnectionImpl newRxBleConnectionImpl(ConnectionOperationQueue operationQueue, RxBleGattCallback gattCallback, BluetoothGatt bluetoothGatt, Object serviceDiscoveryManager, Object notificationIndicationManager, Object mtuProvider2, Object descriptorWriter, OperationsProvider operationProvider2, Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider2, Scheduler callbackScheduler, IllegalOperationChecker illegalOperationChecker) {
        RxBleConnectionImpl rxBleConnectionImpl = new RxBleConnectionImpl(operationQueue, gattCallback, bluetoothGatt, (ServiceDiscoveryManager) serviceDiscoveryManager, (NotificationAndIndicationManager) notificationIndicationManager, (MtuProvider) mtuProvider2, (DescriptorWriter) descriptorWriter, operationProvider2, longWriteOperationBuilderProvider2, callbackScheduler, illegalOperationChecker);
        return rxBleConnectionImpl;
    }
}
