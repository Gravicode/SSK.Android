package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.Subcomponent;
import com.polidea.rxandroidble2.RxBleDevice;

@Subcomponent(modules = {DeviceModule.class, DeviceModuleBinder.class})
@DeviceScope
public interface DeviceComponent {

    @bleshadow.dagger.Subcomponent.Builder
    public interface Builder {
        DeviceComponent build();

        Builder deviceModule(DeviceModule deviceModule);
    }

    @DeviceScope
    RxBleDevice provideDevice();
}
