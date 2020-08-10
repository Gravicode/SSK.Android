package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;

public final class DeviceModule_ProvideMacAddressFactory implements Factory<String> {
    private final DeviceModule module;

    public DeviceModule_ProvideMacAddressFactory(DeviceModule module2) {
        this.module = module2;
    }

    public String get() {
        return (String) Preconditions.checkNotNull(this.module.provideMacAddress(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvideMacAddressFactory create(DeviceModule module2) {
        return new DeviceModule_ProvideMacAddressFactory(module2);
    }

    public static String proxyProvideMacAddress(DeviceModule instance) {
        return (String) Preconditions.checkNotNull(instance.provideMacAddress(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
