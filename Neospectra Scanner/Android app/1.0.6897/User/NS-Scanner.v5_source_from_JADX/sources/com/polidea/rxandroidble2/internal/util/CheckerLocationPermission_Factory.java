package com.polidea.rxandroidble2.internal.util;

import android.content.Context;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class CheckerLocationPermission_Factory implements Factory<CheckerLocationPermission> {
    private final Provider<Context> contextProvider;

    public CheckerLocationPermission_Factory(Provider<Context> contextProvider2) {
        this.contextProvider = contextProvider2;
    }

    public CheckerLocationPermission get() {
        return new CheckerLocationPermission((Context) this.contextProvider.get());
    }

    public static CheckerLocationPermission_Factory create(Provider<Context> contextProvider2) {
        return new CheckerLocationPermission_Factory(contextProvider2);
    }
}
