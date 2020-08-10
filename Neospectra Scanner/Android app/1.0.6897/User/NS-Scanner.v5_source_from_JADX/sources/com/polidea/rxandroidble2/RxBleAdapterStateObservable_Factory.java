package com.polidea.rxandroidble2;

import android.content.Context;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class RxBleAdapterStateObservable_Factory implements Factory<RxBleAdapterStateObservable> {
    private final Provider<Context> contextProvider;

    public RxBleAdapterStateObservable_Factory(Provider<Context> contextProvider2) {
        this.contextProvider = contextProvider2;
    }

    public RxBleAdapterStateObservable get() {
        return new RxBleAdapterStateObservable((Context) this.contextProvider.get());
    }

    public static RxBleAdapterStateObservable_Factory create(Provider<Context> contextProvider2) {
        return new RxBleAdapterStateObservable_Factory(contextProvider2);
    }
}
