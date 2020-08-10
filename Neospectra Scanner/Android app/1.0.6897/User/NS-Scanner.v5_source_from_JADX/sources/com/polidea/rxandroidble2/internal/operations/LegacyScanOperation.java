package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResultLegacy;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import com.polidea.rxandroidble2.internal.util.UUIDUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import p005io.reactivex.Emitter;

public class LegacyScanOperation extends ScanOperation<RxBleInternalScanResultLegacy, LeScanCallback> {
    /* access modifiers changed from: private */
    public final Set<UUID> filterUuids;
    /* access modifiers changed from: private */
    public final boolean isFilterDefined;
    /* access modifiers changed from: private */
    public final UUIDUtil uuidUtil;

    public LegacyScanOperation(UUID[] filterServiceUUIDs, RxBleAdapterWrapper rxBleAdapterWrapper, UUIDUtil uuidUtil2) {
        super(rxBleAdapterWrapper);
        this.isFilterDefined = filterServiceUUIDs != null && filterServiceUUIDs.length > 0;
        this.uuidUtil = uuidUtil2;
        if (this.isFilterDefined) {
            this.filterUuids = new HashSet(filterServiceUUIDs.length);
            Collections.addAll(this.filterUuids, filterServiceUUIDs);
            return;
        }
        this.filterUuids = null;
    }

    /* access modifiers changed from: 0000 */
    public LeScanCallback createScanCallback(final Emitter<RxBleInternalScanResultLegacy> emitter) {
        return new LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (!LegacyScanOperation.this.isFilterDefined || LegacyScanOperation.this.uuidUtil.extractUUIDs(scanRecord).containsAll(LegacyScanOperation.this.filterUuids)) {
                    emitter.onNext(new RxBleInternalScanResultLegacy(device, rssi, scanRecord));
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
