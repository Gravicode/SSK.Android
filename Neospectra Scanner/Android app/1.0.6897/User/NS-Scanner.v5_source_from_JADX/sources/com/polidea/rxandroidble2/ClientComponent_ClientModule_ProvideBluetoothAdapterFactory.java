package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;
import bleshadow.dagger.internal.Factory;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideBluetoothAdapterFactory implements Factory<BluetoothAdapter> {
    private static final ClientComponent_ClientModule_ProvideBluetoothAdapterFactory INSTANCE = new ClientComponent_ClientModule_ProvideBluetoothAdapterFactory();

    @Nullable
    public BluetoothAdapter get() {
        return ClientModule.provideBluetoothAdapter();
    }

    public static ClientComponent_ClientModule_ProvideBluetoothAdapterFactory create() {
        return INSTANCE;
    }

    @Nullable
    public static BluetoothAdapter proxyProvideBluetoothAdapter() {
        return ClientModule.provideBluetoothAdapter();
    }
}
