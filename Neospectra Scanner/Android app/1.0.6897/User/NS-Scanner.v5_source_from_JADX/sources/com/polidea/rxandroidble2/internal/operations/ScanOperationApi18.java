package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.internal.scan.EmulatedScanFilterMatcher;
import com.polidea.rxandroidble2.internal.scan.InternalScanResultCreator;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import p005io.reactivex.Emitter;

public class ScanOperationApi18 extends ScanOperation<RxBleInternalScanResult, LeScanCallback> {
    /* access modifiers changed from: private */
    @NonNull
    public final EmulatedScanFilterMatcher scanFilterMatcher;
    /* access modifiers changed from: private */
    @NonNull
    public final InternalScanResultCreator scanResultCreator;

    public ScanOperationApi18(@NonNull RxBleAdapterWrapper rxBleAdapterWrapper, @NonNull InternalScanResultCreator scanResultCreator2, @NonNull EmulatedScanFilterMatcher scanFilterMatcher2) {
        super(rxBleAdapterWrapper);
        this.scanResultCreator = scanResultCreator2;
        this.scanFilterMatcher = scanFilterMatcher2;
    }

    /* access modifiers changed from: 0000 */
    public LeScanCallback createScanCallback(final Emitter<RxBleInternalScanResult> emitter) {
        return new LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                RxBleInternalScanResult internalScanResult = ScanOperationApi18.this.scanResultCreator.create(device, rssi, scanRecord);
                if (ScanOperationApi18.this.scanFilterMatcher.matches(internalScanResult)) {
                    emitter.onNext(internalScanResult);
                }
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public boolean startScan(RxBleAdapterWrapper rxBleAdapterWrapper, LeScanCallback scanCallback) {
        return rxBleAdapterWrapper.startLegacyLeScan(scanCallback);
    }

    /* access modifiers changed from: 0000 */
    public void stopScan(RxBleAdapterWrapper rxBleAdapterWrapper, LeScanCallback scanCallback) {
        rxBleAdapterWrapper.stopLegacyLeScan(scanCallback);
    }
}
