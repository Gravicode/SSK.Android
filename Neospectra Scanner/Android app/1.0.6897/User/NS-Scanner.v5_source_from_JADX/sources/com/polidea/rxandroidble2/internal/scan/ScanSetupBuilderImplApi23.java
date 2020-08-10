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
public class ScanSetupBuilderImplApi23 implements ScanSetupBuilder {
    private final AndroidScanObjectsConverter androidScanObjectsConverter;
    private final InternalScanResultCreator internalScanResultCreator;
    private final RxBleAdapterWrapper rxBleAdapterWrapper;

    @Inject
    ScanSetupBuilderImplApi23(RxBleAdapterWrapper rxBleAdapterWrapper2, InternalScanResultCreator internalScanResultCreator2, AndroidScanObjectsConverter androidScanObjectsConverter2) {
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
        this.internalScanResultCreator = internalScanResultCreator2;
        this.androidScanObjectsConverter = androidScanObjectsConverter2;
    }

    @RequiresApi(api = 21)
    public ScanSetup build(ScanSettings scanSettings, ScanFilter... scanFilters) {
        if (scanSettings.getCallbackType() != 1 && scanFilters.length == 0) {
            scanFilters = new ScanFilter[]{ScanFilter.empty()};
        }
        ScanOperationApi21 scanOperationApi21 = new ScanOperationApi21(this.rxBleAdapterWrapper, this.internalScanResultCreator, this.androidScanObjectsConverter, scanSettings, new EmulatedScanFilterMatcher(new ScanFilter[0]), scanFilters);
        return new ScanSetup(scanOperationApi21, new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                return observable;
            }
        });
    }
}
