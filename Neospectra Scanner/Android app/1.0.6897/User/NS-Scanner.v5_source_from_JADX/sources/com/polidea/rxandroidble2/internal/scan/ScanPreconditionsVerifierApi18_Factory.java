package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;

public final class ScanPreconditionsVerifierApi18_Factory implements Factory<ScanPreconditionsVerifierApi18> {
    private final Provider<LocationServicesStatus> locationServicesStatusProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;

    public ScanPreconditionsVerifierApi18_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.locationServicesStatusProvider = locationServicesStatusProvider2;
    }

    public ScanPreconditionsVerifierApi18 get() {
        return new ScanPreconditionsVerifierApi18((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (LocationServicesStatus) this.locationServicesStatusProvider.get());
    }

    public static ScanPreconditionsVerifierApi18_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2) {
        return new ScanPreconditionsVerifierApi18_Factory(rxBleAdapterWrapperProvider2, locationServicesStatusProvider2);
    }
}
