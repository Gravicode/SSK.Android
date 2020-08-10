package com.polidea.rxandroidble2.internal.scan;

import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.SparseArray;
import com.polidea.rxandroidble2.scan.ScanRecord;
import java.util.List;
import java.util.Map;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScanRecordImplCompat implements ScanRecord {
    private final int advertiseFlags;
    private final byte[] bytes;
    private final String deviceName;
    private final SparseArray<byte[]> manufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> serviceData;
    @Nullable
    private final List<ParcelUuid> serviceUuids;
    private final int txPowerLevel;

    public ScanRecordImplCompat(@Nullable List<ParcelUuid> serviceUuids2, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData2, int advertiseFlags2, int txPowerLevel2, String localName, byte[] bytes2) {
        this.serviceUuids = serviceUuids2;
        this.manufacturerSpecificData = manufacturerData;
        this.serviceData = serviceData2;
        this.deviceName = localName;
        this.advertiseFlags = advertiseFlags2;
        this.txPowerLevel = txPowerLevel2;
        this.bytes = bytes2;
    }

    public int getAdvertiseFlags() {
        return this.advertiseFlags;
    }

    @Nullable
    public List<ParcelUuid> getServiceUuids() {
        return this.serviceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.manufacturerSpecificData;
    }

    @Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return (byte[]) this.manufacturerSpecificData.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.serviceData;
    }

    @Nullable
    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return (byte[]) this.serviceData.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.txPowerLevel;
    }

    @Nullable
    public String getDeviceName() {
        return this.deviceName;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
