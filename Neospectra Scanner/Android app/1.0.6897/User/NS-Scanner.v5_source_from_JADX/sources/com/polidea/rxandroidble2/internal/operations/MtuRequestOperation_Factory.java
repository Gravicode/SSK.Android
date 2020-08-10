package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;

public final class MtuRequestOperation_Factory implements Factory<MtuRequestOperation> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<Integer> requestedMtuProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;

    public MtuRequestOperation_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Integer> requestedMtuProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
        this.requestedMtuProvider = requestedMtuProvider2;
    }

    public MtuRequestOperation get() {
        return new MtuRequestOperation((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get(), ((Integer) this.requestedMtuProvider.get()).intValue());
    }

    public static MtuRequestOperation_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<Integer> requestedMtuProvider2) {
        return new MtuRequestOperation_Factory(rxBleGattCallbackProvider2, bluetoothGattProvider2, timeoutConfigurationProvider2, requestedMtuProvider2);
    }

    public static MtuRequestOperation newMtuRequestOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration, int requestedMtu) {
        return new MtuRequestOperation(rxBleGattCallback, bluetoothGatt, timeoutConfiguration, requestedMtu);
    }
}
