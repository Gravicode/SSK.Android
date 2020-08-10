package com.polidea.rxandroidble2.internal.scan;

import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.operations.ScanOperationApi21;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanSettings;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableTransformer;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScanSetupBuilderImplApi21 implements ScanSetupBuilder {
    private final AndroidScanObjectsConverter androidScanObjectsConverter;
    private final InternalScanResultCreator internalScanResultCreator;
    private final RxBleAdapterWrapper rxBleAdapterWrapper;
    private final ScanSettingsEmulator scanSettingsEmulator;

    @Inject
    ScanSetupBuilderImplApi21(RxBleAdapterWrapper rxBleAdapterWrapper2, InternalScanResultCreator internalScanResultCreator2, ScanSettingsEmulator scanSettingsEmulator2, AndroidScanObjectsConverter androidScanObjectsConverter2) {
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
        this.internalScanResultCreator = internalScanResultCreator2;
        this.scanSettingsEmulator = scanSettingsEmulator2;
        this.androidScanObjectsConverter = androidScanObjectsConverter2;
    }

    @RequiresApi(api = 21)
    public ScanSetup build(ScanSettings scanSettings, ScanFilter... scanFilters) {
        final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> callbackTypeTransformer = this.scanSettingsEmulator.emulateCallbackType(scanSettings.getCallbackType());
        ScanOperationApi21 scanOperationApi21 = new ScanOperationApi21(this.rxBleAdapterWrapper, this.internalScanResultCreator, this.androidScanObjectsConverter, scanSettings, new EmulatedScanFilterMatcher(scanFilters), null);
        return new ScanSetup(scanOperationApi21, new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                return observable.compose(callbackTypeTransformer);
            }
        });
    }
}
