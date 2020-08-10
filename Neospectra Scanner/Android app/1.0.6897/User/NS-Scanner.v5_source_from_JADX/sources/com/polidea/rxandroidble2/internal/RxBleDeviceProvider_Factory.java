package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.DeviceComponent.Builder;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentCache;

public final class RxBleDeviceProvider_Factory implements Factory<RxBleDeviceProvider> {
    private final Provider<Builder> deviceComponentBuilderProvider;
    private final Provider<DeviceComponentCache> deviceComponentCacheProvider;

    public RxBleDeviceProvider_Factory(Provider<DeviceComponentCache> deviceComponentCacheProvider2, Provider<Builder> deviceComponentBuilderProvider2) {
        this.deviceComponentCacheProvider = deviceComponentCacheProvider2;
        this.deviceComponentBuilderProvider = deviceComponentBuilderProvider2;
    }

    public RxBleDeviceProvider get() {
        return new RxBleDeviceProvider((DeviceComponentCache) this.deviceComponentCacheProvider.get(), this.deviceComponentBuilderProvider);
    }

    public static RxBleDeviceProvider_Factory create(Provider<DeviceComponentCache> deviceComponentCacheProvider2, Provider<Builder> deviceComponentBuilderProvider2) {
        return new RxBleDeviceProvider_Factory(deviceComponentCacheProvider2, deviceComponentBuilderProvider2);
    }
}
