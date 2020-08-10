package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;

public final class DeviceModule_ProvideBluetoothDeviceFactory implements Factory<BluetoothDevice> {
    private final Provider<RxBleAdapterWrapper> adapterWrapperProvider;
    private final DeviceModule module;

    public DeviceModule_ProvideBluetoothDeviceFactory(DeviceModule module2, Provider<RxBleAdapterWrapper> adapterWrapperProvider2) {
        this.module = module2;
        this.adapterWrapperProvider = adapterWrapperProvider2;
    }

    public BluetoothDevice get() {
        return (BluetoothDevice) Preconditions.checkNotNull(this.module.provideBluetoothDevice((RxBleAdapterWrapper) this.adapterWrapperProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvideBluetoothDeviceFactory create(DeviceModule module2, Provider<RxBleAdapterWrapper> adapterWrapperProvider2) {
        return new DeviceModule_ProvideBluetoothDeviceFactory(module2, adapterWrapperProvider2);
    }

    public static BluetoothDevice proxyProvideBluetoothDevice(DeviceModule instance, RxBleAdapterWrapper adapterWrapper) {
        return (BluetoothDevice) Preconditions.checkNotNull(instance.provideBluetoothDevice(adapterWrapper), "Cannot return null from a non-@Nullable @Provides method");
    }
}
