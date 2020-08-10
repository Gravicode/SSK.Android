package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;

public final class ConnectionPriorityChangeOperation_Factory implements Factory<ConnectionPriorityChangeOperation> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<Integer> connectionPriorityProvider;
    private final Provider<Scheduler> delaySchedulerProvider;
    private final Provider<Long> operationTimeoutProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;
    private final Provider<TimeUnit> timeUnitProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;

    public ConnectionPriorityChangeOperation_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Integer> connectionPriorityProvider2, Provider<Long> operationTimeoutProvider2, Provider<TimeUnit> timeUnitProvider2, Provider<Scheduler> delaySchedulerProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
        this.connectionPriorityProvider = connectionPriorityProvider2;
        this.operationTimeoutProvider = operationTimeoutProvider2;
        this.timeUnitProvider = timeUnitProvider2;
        this.delaySchedulerProvider = delaySchedulerProvider2;
    }

    public ConnectionPriorityChangeOperation get() {
        ConnectionPriorityChangeOperation connectionPriorityChangeOperation = new ConnectionPriorityChangeOperation((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get(), ((Integer) this.connectionPriorityProvider.get()).intValue(), ((Long) this.operationTimeoutProvider.get()).longValue(), (TimeUnit) this.timeUnitProvider.get(), (Scheduler) this.delaySchedulerProvider.get());
        return connectionPriorityChangeOperation;
    }

    public static ConnectionPriorityChangeOperation_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Integer> connectionPriorityProvider2, Provider<Long> operationTimeoutProvider2, Provider<TimeUnit> timeUnitProvider2, Provider<Scheduler> delaySchedulerProvider2) {
        ConnectionPriorityChangeOperation_Factory connectionPriorityChangeOperation_Factory = new ConnectionPriorityChangeOperation_Factory(rxBleGattCallbackProvider2, bluetoothGattProvider2, timeoutConfigurationProvider2, connectionPriorityProvider2, operationTimeoutProvider2, timeUnitProvider2, delaySchedulerProvider2);
        return connectionPriorityChangeOperation_Factory;
    }

    public static ConnectionPriorityChangeOperation newConnectionPriorityChangeOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration, int connectionPriority, long operationTimeout, TimeUnit timeUnit, Scheduler delayScheduler) {
        ConnectionPriorityChangeOperation connectionPriorityChangeOperation = new ConnectionPriorityChangeOperation(rxBleGattCallback, bluetoothGatt, timeoutConfiguration, connectionPriority, operationTimeout, timeUnit, delayScheduler);
        return connectionPriorityChangeOperation;
    }
}
