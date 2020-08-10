package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;

public final class DeviceModule_ProvideConnectionStateChangeListenerFactory implements Factory<ConnectionStateChangeListener> {
    private final Provider<BehaviorRelay<RxBleConnectionState>> connectionStateBehaviorRelayProvider;

    public DeviceModule_ProvideConnectionStateChangeListenerFactory(Provider<BehaviorRelay<RxBleConnectionState>> connectionStateBehaviorRelayProvider2) {
        this.connectionStateBehaviorRelayProvider = connectionStateBehaviorRelayProvider2;
    }

    public ConnectionStateChangeListener get() {
        return (ConnectionStateChangeListener) Preconditions.checkNotNull(DeviceModule.provideConnectionStateChangeListener((BehaviorRelay) this.connectionStateBehaviorRelayProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvideConnectionStateChangeListenerFactory create(Provider<BehaviorRelay<RxBleConnectionState>> connectionStateBehaviorRelayProvider2) {
        return new DeviceModule_ProvideConnectionStateChangeListenerFactory(connectionStateBehaviorRelayProvider2);
    }

    public static ConnectionStateChangeListener proxyProvideConnectionStateChangeListener(BehaviorRelay<RxBleConnectionState> connectionStateBehaviorRelay) {
        return (ConnectionStateChangeListener) Preconditions.checkNotNull(DeviceModule.provideConnectionStateChangeListener(connectionStateBehaviorRelay), "Cannot return null from a non-@Nullable @Provides method");
    }
}
