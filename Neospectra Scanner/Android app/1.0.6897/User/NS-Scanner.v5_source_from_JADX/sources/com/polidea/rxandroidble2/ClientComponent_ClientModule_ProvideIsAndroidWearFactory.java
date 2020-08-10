package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideIsAndroidWearFactory implements Factory<Boolean> {
    private final Provider<Integer> deviceSdkProvider;
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideIsAndroidWearFactory(ClientModule module2, Provider<Integer> deviceSdkProvider2) {
        this.module = module2;
        this.deviceSdkProvider = deviceSdkProvider2;
    }

    public Boolean get() {
        return Boolean.valueOf(this.module.provideIsAndroidWear(((Integer) this.deviceSdkProvider.get()).intValue()));
    }

    public static ClientComponent_ClientModule_ProvideIsAndroidWearFactory create(ClientModule module2, Provider<Integer> deviceSdkProvider2) {
        return new ClientComponent_ClientModule_ProvideIsAndroidWearFactory(module2, deviceSdkProvider2);
    }

    public static boolean proxyProvideIsAndroidWear(ClientModule instance, int deviceSdk) {
        return instance.provideIsAndroidWear(deviceSdk);
    }
}
