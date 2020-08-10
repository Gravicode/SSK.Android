package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class AndroidScanObjectsConverter_Factory implements Factory<AndroidScanObjectsConverter> {
    private final Provider<Integer> deviceSdkProvider;

    public AndroidScanObjectsConverter_Factory(Provider<Integer> deviceSdkProvider2) {
        this.deviceSdkProvider = deviceSdkProvider2;
    }

    public AndroidScanObjectsConverter get() {
        return new AndroidScanObjectsConverter(((Integer) this.deviceSdkProvider.get()).intValue());
    }

    public static AndroidScanObjectsConverter_Factory create(Provider<Integer> deviceSdkProvider2) {
        return new AndroidScanObjectsConverter_Factory(deviceSdkProvider2);
    }
}
