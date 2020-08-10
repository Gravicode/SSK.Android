package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideTargetSdkFactory implements Factory<Integer> {
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideTargetSdkFactory(ClientModule module2) {
        this.module = module2;
    }

    public Integer get() {
        return Integer.valueOf(this.module.provideTargetSdk());
    }

    public static ClientComponent_ClientModule_ProvideTargetSdkFactory create(ClientModule module2) {
        return new ClientComponent_ClientModule_ProvideTargetSdkFactory(module2);
    }

    public static int proxyProvideTargetSdk(ClientModule instance) {
        return instance.provideTargetSdk();
    }
}
