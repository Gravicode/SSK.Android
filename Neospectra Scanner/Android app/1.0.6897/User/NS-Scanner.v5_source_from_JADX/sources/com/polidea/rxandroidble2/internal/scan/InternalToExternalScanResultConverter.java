package com.polidea.rxandroidble2.internal.scan;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider;
import com.polidea.rxandroidble2.scan.ScanResult;
import p005io.reactivex.functions.Function;

@RestrictTo({Scope.LIBRARY_GROUP})
public class InternalToExternalScanResultConverter implements Function<RxBleInternalScanResult, ScanResult> {
    private final RxBleDeviceProvider deviceProvider;

    @Inject
    public InternalToExternalScanResultConverter(RxBleDeviceProvider deviceProvider2) {
        this.deviceProvider = deviceProvider2;
    }

    public ScanResult apply(RxBleInternalScanResult rxBleInternalScanResult) {
        ScanResult scanResult = new ScanResult(this.deviceProvider.getBleDevice(rxBleInternalScanResult.getBluetoothDevice().getAddress()), rxBleInternalScanResult.getRssi(), rxBleInternalScanResult.getTimestampNanos(), rxBleInternalScanResult.getScanCallbackType(), rxBleInternalScanResult.getScanRecord());
        return scanResult;
    }
}
