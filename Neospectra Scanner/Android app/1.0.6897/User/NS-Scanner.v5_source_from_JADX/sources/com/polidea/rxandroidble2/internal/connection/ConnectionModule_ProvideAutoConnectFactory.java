package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;

public final class ConnectionModule_ProvideAutoConnectFactory implements Factory<Boolean> {
    private final ConnectionModule module;

    public ConnectionModule_ProvideAutoConnectFactory(ConnectionModule module2) {
        this.module = module2;
    }

    public Boolean get() {
        return Boolean.valueOf(this.module.provideAutoConnect());
    }

    public static ConnectionModule_ProvideAutoConnectFactory create(ConnectionModule module2) {
        return new ConnectionModule_ProvideAutoConnectFactory(module2);
    }

    public static boolean proxyProvideAutoConnect(ConnectionModule instance) {
        return instance.provideAutoConnect();
    }
}
