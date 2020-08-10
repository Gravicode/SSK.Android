package com.polidea.rxandroidble2.internal;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientScope;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.internal.DeviceComponent.Builder;
import com.polidea.rxandroidble2.internal.cache.DeviceComponentCache;
import java.util.Map;

@ClientScope
public class RxBleDeviceProvider {
    private final Map<String, DeviceComponent> cachedDeviceComponents;
    private final Provider<Builder> deviceComponentBuilder;

    @Inject
    public RxBleDeviceProvider(DeviceComponentCache deviceComponentCache, Provider<Builder> deviceComponentBuilder2) {
        this.cachedDeviceComponents = deviceComponentCache;
        this.deviceComponentBuilder = deviceComponentBuilder2;
    }

    public RxBleDevice getBleDevice(String macAddress) {
        DeviceComponent cachedDeviceComponent = (DeviceComponent) this.cachedDeviceComponents.get(macAddress);
        if (cachedDeviceComponent != null) {
            return cachedDeviceComponent.provideDevice();
        }
        synchronized (this.cachedDeviceComponents) {
            DeviceComponent secondCheckRxBleDevice = (DeviceComponent) this.cachedDeviceComponents.get(macAddress);
            if (secondCheckRxBleDevice != null) {
                RxBleDevice provideDevice = secondCheckRxBleDevice.provideDevice();
                return provideDevice;
            }
            DeviceComponent deviceComponent = ((Builder) this.deviceComponentBuilder.get()).deviceModule(new DeviceModule(macAddress)).build();
            RxBleDevice newRxBleDevice = deviceComponent.provideDevice();
            this.cachedDeviceComponents.put(macAddress, deviceComponent);
            return newRxBleDevice;
        }
    }
}
