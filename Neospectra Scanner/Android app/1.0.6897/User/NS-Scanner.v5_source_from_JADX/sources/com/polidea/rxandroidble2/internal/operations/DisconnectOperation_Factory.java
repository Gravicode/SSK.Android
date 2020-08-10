package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothManager;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import p005io.reactivex.Scheduler;

public final class DisconnectOperation_Factory implements Factory<DisconnectOperation> {
    private final Provider<BluetoothGattProvider> bluetoothGattProvider;
    private final Provider<Scheduler> bluetoothInteractionSchedulerProvider;
    private final Provider<BluetoothManager> bluetoothManagerProvider;
    private final Provider<ConnectionStateChangeListener> connectionStateChangeListenerProvider;
    private final Provider<String> macAddressProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;

    public DisconnectOperation_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<String> macAddressProvider2, Provider<BluetoothManager> bluetoothManagerProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<ConnectionStateChangeListener> connectionStateChangeListenerProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.macAddressProvider = macAddressProvider2;
        this.bluetoothManagerProvider = bluetoothManagerProvider2;
        this.bluetoothInteractionSchedulerProvider = bluetoothInteractionSchedulerProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
        this.connectionStateChangeListenerProvider = connectionStateChangeListenerProvider2;
    }

    public DisconnectOperation get() {
        DisconnectOperation disconnectOperation = new DisconnectOperation((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGattProvider) this.bluetoothGattProvider.get(), (String) this.macAddressProvider.get(), (BluetoothManager) this.bluetoothManagerProvider.get(), (Scheduler) this.bluetoothInteractionSchedulerProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get(), (ConnectionStateChangeListener) this.connectionStateChangeListenerProvider.get());
        return disconnectOperation;
    }

    public static DisconnectOperation_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<String> macAddressProvider2, Provider<BluetoothManager> bluetoothManagerProvider2, Provider<Scheduler> bluetoothInteractionSchedulerProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<ConnectionStateChangeListener> connectionStateChangeListenerProvider2) {
        DisconnectOperation_Factory disconnectOperation_Factory = new DisconnectOperation_Factory(rxBleGattCallbackProvider2, bluetoothGattProvider2, macAddressProvider2, bluetoothManagerProvider2, bluetoothInteractionSchedulerProvider2, timeoutConfigurationProvider2, connectionStateChangeListenerProvider2);
        return disconnectOperation_Factory;
    }

    public static DisconnectOperation newDisconnectOperation(RxBleGattCallback rxBleGattCallback, BluetoothGattProvider bluetoothGattProvider2, String macAddress, BluetoothManager bluetoothManager, Scheduler bluetoothInteractionScheduler, TimeoutConfiguration timeoutConfiguration, ConnectionStateChangeListener connectionStateChangeListener) {
        DisconnectOperation disconnectOperation = new DisconnectOperation(rxBleGattCallback, bluetoothGattProvider2, macAddress, bluetoothManager, bluetoothInteractionScheduler, timeoutConfiguration, connectionStateChangeListener);
        return disconnectOperation;
    }
}
