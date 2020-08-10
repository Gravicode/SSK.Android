package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;

public final class DescriptorReadOperation_Factory implements Factory<DescriptorReadOperation> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<BluetoothGattDescriptor> descriptorProvider;
    private final Provider<RxBleGattCallback> rxBleGattCallbackProvider;
    private final Provider<TimeoutConfiguration> timeoutConfigurationProvider;

    public DescriptorReadOperation_Factory(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<BluetoothGattDescriptor> descriptorProvider2) {
        this.rxBleGattCallbackProvider = rxBleGattCallbackProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.timeoutConfigurationProvider = timeoutConfigurationProvider2;
        this.descriptorProvider = descriptorProvider2;
    }

    public DescriptorReadOperation get() {
        return new DescriptorReadOperation((RxBleGattCallback) this.rxBleGattCallbackProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (TimeoutConfiguration) this.timeoutConfigurationProvider.get(), (BluetoothGattDescriptor) this.descriptorProvider.get());
    }

    public static DescriptorReadOperation_Factory create(Provider<RxBleGattCallback> rxBleGattCallbackProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<TimeoutConfiguration> timeoutConfigurationProvider2, Provider<BluetoothGattDescriptor> descriptorProvider2) {
        return new DescriptorReadOperation_Factory(rxBleGattCallbackProvider2, bluetoothGattProvider2, timeoutConfigurationProvider2, descriptorProvider2);
    }

    public static DescriptorReadOperation newDescriptorReadOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration, BluetoothGattDescriptor descriptor) {
        return new DescriptorReadOperation(rxBleGattCallback, bluetoothGatt, timeoutConfiguration, descriptor);
    }
}
