package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;

public final class ScanSetupBuilderImplApi18_Factory implements Factory<ScanSetupBuilderImplApi18> {
    private final Provider<InternalScanResultCreator> internalScanResultCreatorProvider;
    private final Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider;
    private final Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider;

    public ScanSetupBuilderImplApi18_Factory(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider2) {
        this.rxBleAdapterWrapperProvider = rxBleAdapterWrapperProvider2;
        this.internalScanResultCreatorProvider = internalScanResultCreatorProvider2;
        this.scanSettingsEmulatorProvider = scanSettingsEmulatorProvider2;
    }

    public ScanSetupBuilderImplApi18 get() {
        return new ScanSetupBuilderImplApi18((RxBleAdapterWrapper) this.rxBleAdapterWrapperProvider.get(), (InternalScanResultCreator) this.internalScanResultCreatorProvider.get(), (ScanSettingsEmulator) this.scanSettingsEmulatorProvider.get());
    }

    public static ScanSetupBuilderImplApi18_Factory create(Provider<RxBleAdapterWrapper> rxBleAdapterWrapperProvider2, Provider<InternalScanResultCreator> internalScanResultCreatorProvider2, Provider<ScanSettingsEmulator> scanSettingsEmulatorProvider2) {
        return new ScanSetupBuilderImplApi18_Factory(rxBleAdapterWrapperProvider2, internalScanResultCreatorProvider2, scanSettingsEmulatorProvider2);
    }

    public static ScanSetupBuilderImplApi18 newScanSetupBuilderImplApi18(RxBleAdapterWrapper rxBleAdapterWrapper, InternalScanResultCreator internalScanResultCreator, ScanSettingsEmulator scanSettingsEmulator) {
        return new ScanSetupBuilderImplApi18(rxBleAdapterWrapper, internalScanResultCreator, scanSettingsEmulator);
    }
}
