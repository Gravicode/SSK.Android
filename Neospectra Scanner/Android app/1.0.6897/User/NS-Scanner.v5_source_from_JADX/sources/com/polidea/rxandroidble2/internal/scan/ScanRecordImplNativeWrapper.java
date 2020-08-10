package com.polidea.rxandroidble2.internal.scan;

import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.SparseArray;
import com.polidea.rxandroidble2.scan.ScanRecord;
import java.util.List;
import java.util.Map;

@RequiresApi(21)
@RestrictTo({Scope.LIBRARY_GROUP})
public class ScanRecordImplNativeWrapper implements ScanRecord {
    private final android.bluetooth.le.ScanRecord nativeScanRecord;

    public ScanRecordImplNativeWrapper(android.bluetooth.le.ScanRecord nativeScanRecord2) {
        this.nativeScanRecord = nativeScanRecord2;
    }

    public int getAdvertiseFlags() {
        return this.nativeScanRecord.getAdvertiseFlags();
    }

    @Nullable
    public List<ParcelUuid> getServiceUuids() {
        return this.nativeScanRecord.getServiceUuids();
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.nativeScanRecord.getManufacturerSpecificData();
    }

    @Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return this.nativeScanRecord.getManufacturerSpecificData(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.nativeScanRecord.getServiceData();
    }

    @Nullable
    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        return this.nativeScanRecord.getServiceData(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.nativeScanRecord.getTxPowerLevel();
    }

    @Nullable
    public String getDeviceName() {
        return this.nativeScanRecord.getDeviceName();
    }

    public byte[] getBytes() {
        return this.nativeScanRecord.getBytes();
    }
}
