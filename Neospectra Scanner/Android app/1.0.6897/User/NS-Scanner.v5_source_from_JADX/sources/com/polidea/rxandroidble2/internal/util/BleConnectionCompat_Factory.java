package com.polidea.rxandroidble2.internal.util;

import android.content.Context;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class BleConnectionCompat_Factory implements Factory<BleConnectionCompat> {
    private final Provider<Context> contextProvider;

    public BleConnectionCompat_Factory(Provider<Context> contextProvider2) {
        this.contextProvider = contextProvider2;
    }

    public BleConnectionCompat get() {
        return new BleConnectionCompat((Context) this.contextProvider.get());
    }

    public static BleConnectionCompat_Factory create(Provider<Context> contextProvider2) {
        return new BleConnectionCompat_Factory(contextProvider2);
    }
}
