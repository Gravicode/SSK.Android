package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.util.RxBleServicesLogger;
import p005io.reactivex.Scheduler;

public final class OperationsProviderImpl_Factory implements Factory<OperationsProviderImpl> {
    private final Provider<RxBleServicesLogger> bleServicesLoggerProvider;
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<Scheduler> bluetoothInteractionSchedulerProvider;
    private final Provider<ReadRssiOperation> rssiReadOperationProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;
    private final Provider<Scheduler> timeoutSchedulerProvider;

    public OperationsProviderImpl_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<RxBleServicesLogger> bleServicesLoggerProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<Scheduler> timeoutSchedulerProvider2, Provider<ReadRssiOperation> rssiReadOperationProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.bleServicesLoggerProvider = bleServicesLoggerProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
        this.bluetoothInteractionSchedulerProvider = bluetoothInteractionSchedulerProvider2;
        this.timeoutSchedulerProvider = timeoutSchedulerProvider2;
        this.rssiReadOperationProvider = rssiReadOperationProvider2;
    }

    public OperationsProviderImpl get() {
        OperationsProviderImpl operationsProviderImpl = new OperationsProviderImpl((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (RxBleServicesLogger) this.bleServicesLoggerProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get(), (Scheduler) this.bluetoothInteractionSchedulerProvider.get(), (Scheduler) this.timeoutSchedulerProvider.get(), this.rssiReadOperationProvider);
        return operationsProviderImpl;
    }

    public static OperationsProviderImpl_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<RxBleServicesLogger> bleServicesLoggerProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<Scheduler> timeoutSchedulerProvider2, Provider<ReadRssiOperation> rssiReadOperationProvider2) {
        OperationsProviderImpl_Factory operationsProviderImpl_Factory = new OperationsProviderImpl_Factory(rxBleGattCallbackProvider2, bluetoothGattProvider2, bleServicesLoggerProvider2, timeoutConfigurationProvider2, bluetoothInteractionSchedulerProvider2, timeoutSchedulerProvider2, rssiReadOperationProvider2);
        return operationsProviderImpl_Factory;
    }

    public static OperationsProviderImpl newOperationsProviderImpl(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, RxBleServicesLogger bleServicesLogger, TimeoutConfiguration timeoutConfiguration, Scheduler bluetoothInteractionScheduler, Scheduler timeoutScheduler, Provider<ReadRssiOperation> rssiReadOperationProvider2) {
        OperationsProviderImpl operationsProviderImpl = new OperationsProviderImpl(rxBleGattCallback, bluetoothGatt, bleServicesLogger, timeoutConfiguration, bluetoothInteractionScheduler, timeoutScheduler, rssiReadOperationProvider2);
        return operationsProviderImpl;
    }
}
