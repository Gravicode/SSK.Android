package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;

public final class ScanSetupBuilderImplApi23_Factory implements Factory<ScanSetupBuilderImplApi23> {
    private final Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider;
    private final Provider<InternalScanResultCreator> internalScanResultCreatorProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;

    public ScanSetupBuilderImplApi23_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.internalScanResultCreatorProvider = internalScanResultCreatorProvider2;
        this.androidScanObjectsConverterProvider = androidScanObjectsConverterProvider2;
    }

    public ScanSetupBuilderImplApi23 get() {
        return new ScanSetupBuilderImplApi23((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (InternalScanResultCreator) this.internalScanResultCreatorProvider.get(), (AndroidScanObjectsConverter) this.androidScanObjectsConverterProvider.get());
    }

    public static ScanSetupBuilderImplApi23_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider2) {
        return new ScanSetupBuilderImplApi23_Factory(rxBleAdapterWrapperProvider2, internalScanResultCreatorProvider2, androidScanObjectsConverterProvider2);
    }

    public static ScanSetupBuilderImplApi23 newScanSetupBuilderImplApi23(RxBleAdapterWrapper rxBleAdapterWrapper, InternalScanResultCreator internalScanResultCreator, AndroidScanObjectsConverter androidScanObjectsConverter) {
        return new ScanSetupBuilderImplApi23(rxBleAdapterWrapper, internalScanResultCreator, androidScanObjectsConverter);
    }
}
