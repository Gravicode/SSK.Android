package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothManager;
import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideBluetoothManagerFactory implements Factory<BluetoothManager> {
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideBluetoothManagerFactory(ClientModule module2) {
        this.module = module2;
    }

    public BluetoothManager get() {
        return (BluetoothManager) Preconditions.checkNotNull(this.module.provideBluetoothManager(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideBluetoothManagerFactory create(ClientModule module2) {
        return new ClientComponent_ClientModule_ProvideBluetoothManagerFactory(module2);
    }

    public static BluetoothManager proxyProvideBluetoothManager(ClientModule instance) {
        return (BluetoothManager) Preconditions.checkNotNull(instance.provideBluetoothManager(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
