package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;

public final class DeviceModule_ProvideConnectionStateRelayFactory implements Factory<BehaviorRelay<RxBleConnectionState>> {
    private static final DeviceModule_ProvideConnectionStateRelayFactory INSTANCE = new DeviceModule_ProvideConnectionStateRelayFactory();

    public BehaviorRelay<RxBleConnectionState> get() {
        return (BehaviorRelay) Preconditions.checkNotNull(DeviceModule.provideConnectionStateRelay(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvideConnectionStateRelayFactory create() {
        return INSTANCE;
    }

    public static BehaviorRelay<RxBleConnectionState> proxyProvideConnectionStateRelay() {
        return (BehaviorRelay) Preconditions.checkNotNull(DeviceModule.provideConnectionStateRelay(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
