package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothDevice;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.util.BleConnectionCompat;

public final class ConnectOperation_Factory implements Factory<ConnectOperation> {
    private final Provider<Boolean> autoConnectProvider;
    private final Provider<BluetoothDevice> bluetoothDeviceProvider;
    private final Provider<BluetoothGattProvider> bluetoothGattProvider;
    private final Provider<TimeoutConfiguration> connectTimeoutProvider;
    private final Provider<BleConnectionCompat> connectionCompatProvider;
    private final Provider<ConnectionStateChangeListener> connectionStateChangedActionProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;

    public ConnectOperation_Factory(Provider<BluetoothDevice> bluetoothDeviceProvider2, Provider<BleConnectionCompat> connectionCompatProvider2, Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<TimeoutConfiguration> connectTimeoutProvider2, Provider<Boolean> autoConnectProvider2, Provider<ConnectionStateChangeListener> connectionStateChangedActionProvider2) {
        this.bluetoothDeviceProvider = bluetoothDeviceProvider2;
        this.connectionCompatProvider = connectionCompatProvider2;
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.connectTimeoutProvider = connectTimeoutProvider2;
        this.autoConnectProvider = autoConnectProvider2;
        this.connectionStateChangedActionProvider = connectionStateChangedActionProvider2;
    }

    public ConnectOperation get() {
        ConnectOperation connectOperation = new ConnectOperation((BluetoothDevice) this.bluetoothDeviceProvider.get(), (BleConnectionCompat) this.connectionCompatProvider.get(), (RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGattProvider) this.bluetoothGattProvider.get(), (TimeoutConfiguration) this.connectTimeoutProvider.get(), ((Boolean) this.autoConnectProvider.get()).booleanValue(), (ConnectionStateChangeListener) this.connectionStateChangedActionProvider.get());
        return connectOperation;
    }

    public static ConnectOperation_Factory create(Provider<BluetoothDevice> bluetoothDeviceProvider2, Provider<BleConnectionCompat> connectionCompatProvider2, Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGattProvider> bluetoothGattProvider2, Provider<TimeoutConfiguration> connectTimeoutProvider2, Provider<Boolean> autoConnectProvider2, Provider<ConnectionStateChangeListener> connectionStateChangedActionProvider2) {
        ConnectOperation_Factory connectOperation_Factory = new ConnectOperation_Factory(bluetoothDeviceProvider2, connectionCompatProvider2, rxBleGattCallbackProvider2, bluetoothGattProvider2, connectTimeoutProvider2, autoConnectProvider2, connectionStateChangedActionProvider2);
        return connectOperation_Factory;
    }

    public static ConnectOperation newConnectOperation(BluetoothDevice bluetoothDevice, BleConnectionCompat connectionCompat, RxBleGattCallback rxBleGattCallback, BluetoothGattProvider bluetoothGattProvider2, TimeoutConfiguration connectTimeout, boolean autoConnect, ConnectionStateChangeListener connectionStateChangedAction) {
        ConnectOperation connectOperation = new ConnectOperation(bluetoothDevice, connectionCompat, rxBleGattCallback, bluetoothGattProvider2, connectTimeout, autoConnect, connectionStateChangedAction);
        return connectOperation;
    }
}
