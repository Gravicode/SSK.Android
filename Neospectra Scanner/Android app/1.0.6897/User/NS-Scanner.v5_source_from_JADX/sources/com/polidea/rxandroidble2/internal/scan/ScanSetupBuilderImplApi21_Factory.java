package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;

public final class ScanSetupBuilderImplApi21_Factory implements Factory<ScanSetupBuilderImplApi21> {
    private final Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider;
    private final Provider<InternalScanResultCreator> internalScanResultCreatorProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;
    private final Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider;

    public ScanSetupBuilderImplApi21_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider2, Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.internalScanResultCreatorProvider = internalScanResultCreatorProvider2;
        this.scanSettingsEmulatorProvider = scanSettingsEmulatorProvider2;
        this.androidScanObjectsConverterProvider = androidScanObjectsConverterProvider2;
    }

    public ScanSetupBuilderImplApi21 get() {
        return new ScanSetupBuilderImplApi21((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (InternalScanResultCreator) this.internalScanResultCreatorProvider.get(), (ScanSettingsEmulator) this.scanSettingsEmulatorProvider.get(), (AndroidScanObjectsConverter) this.androidScanObjectsConverterProvider.get());
    }

    public static ScanSetupBuilderImplApi21_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider2, Provider<AndroidScanObjectsConverter> androidScanObjectsConverterProvider2) {
        return new ScanSetupBuilderImplApi21_Factory(rxBleAdapterWrapperProvider2, internalScanResultCreatorProvider2, scanSettingsEmulatorProvider2, androidScanObjectsConverterProvider2);
    }

    public static ScanSetupBuilderImplApi21 newScanSetupBuilderImplApi21(RxBleAdapterWrapper rxBleAdapterWrapper, InternalScanResultCreator internalScanResultCreator, ScanSettingsEmulator scanSettingsEmulator, AndroidScanObjectsConverter androidScanObjectsConverter) {
        return new ScanSetupBuilderImplApi21(rxBleAdapterWrapper, internalScanResultCreator, scanSettingsEmulator, androidScanObjectsConverter);
    }
}
