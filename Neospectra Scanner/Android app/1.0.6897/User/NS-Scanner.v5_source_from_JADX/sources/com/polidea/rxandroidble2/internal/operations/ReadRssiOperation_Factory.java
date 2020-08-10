package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;

public final class ReadRssiOperation_Factory implements Factory<ReadRssiOperation> {
    private final Provider<RxBleGattCallback> bleGattCallbackProvider;
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;

    public ReadRssiOperation_Factory(Provider<RxBleGattCallback> bleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2) {
        this.bleGattCallbackProvider = bleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
    }

    public ReadRssiOperation get() {
        return new ReadRssiOperation((RxBleGattCallback) this.bleGattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get());
    }

    public static ReadRssiOperation_Factory create(Provider<RxBleGattCallback> bleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2) {
        return new ReadRssiOperation_Factory(bleGattCallbackProvider2, bluetoothGattProvider2, timeoutConfigurationProvider2);
    }

    public static ReadRssiOperation newReadRssiOperation(RxBleGattCallback bleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration) {
        return new ReadRssiOperation(bleGattCallback, bluetoothGatt, timeoutConfiguration);
    }
}
