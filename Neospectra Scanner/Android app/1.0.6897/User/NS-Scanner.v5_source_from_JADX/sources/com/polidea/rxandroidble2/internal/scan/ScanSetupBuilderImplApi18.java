package com.polidea.rxandroidble2.internal.scan;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.operations.ScanOperationApi18;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanSettings;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableTransformer;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScanSetupBuilderImplApi18 implements ScanSetupBuilder {
    private final InternalScanResultCreator internalScanResultCreator;
    private final RxBleAdapterWrapper rxBleAdapterWrapper;
    private final ScanSettingsEmulator scanSettingsEmulator;

    @Inject
    ScanSetupBuilderImplApi18(RxBleAdapterWrapper rxBleAdapterWrapper2, InternalScanResultCreator internalScanResultCreator2, ScanSettingsEmulator scanSettingsEmulator2) {
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
        this.internalScanResultCreator = internalScanResultCreator2;
        this.scanSettingsEmulator = scanSettingsEmulator2;
    }

    public ScanSetup build(ScanSettings scanSettings, ScanFilter... scanFilters) {
        final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanModeTransformer = this.scanSettingsEmulator.emulateScanMode(scanSettings.getScanMode());
        final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> callbackTypeTransformer = this.scanSettingsEmulator.emulateCallbackType(scanSettings.getCallbackType());
        return new ScanSetup(new ScanOperationApi18(this.rxBleAdapterWrapper, this.internalScanResultCreator, new EmulatedScanFilterMatcher(scanFilters)), new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                return observable.compose(scanModeTransformer).compose(callbackTypeTransformer);
            }
        });
    }
}
