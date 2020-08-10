package com.polidea.rxandroidble2.internal.scan;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.ClientScope;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.util.UUIDUtil;
import com.polidea.rxandroidble2.scan.ScanCallbackType;

@ClientScope
@RestrictTo({Scope.LIBRARY_GROUP})
public class InternalScanResultCreator {
    private final UUIDUtil uuidUtil;

    @Inject
    public InternalScanResultCreator(UUIDUtil uuidUtil2) {
        this.uuidUtil = uuidUtil2;
    }

    public RxBleInternalScanResult create(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        RxBleInternalScanResult rxBleInternalScanResult = new RxBleInternalScanResult(bluetoothDevice, rssi, System.nanoTime(), this.uuidUtil.parseFromBytes(scanRecord), ScanCallbackType.CALLBACK_TYPE_UNSPECIFIED);
        return rxBleInternalScanResult;
    }

    @RequiresApi(api = 21)
    public RxBleInternalScanResult create(ScanResult result) {
        RxBleInternalScanResult rxBleInternalScanResult = new RxBleInternalScanResult(result.getDevice(), result.getRssi(), result.getTimestampNanos(), new ScanRecordImplNativeWrapper(result.getScanRecord()), ScanCallbackType.CALLBACK_TYPE_BATCH);
        return rxBleInternalScanResult;
    }

    @RequiresApi(api = 21)
    public RxBleInternalScanResult create(int callbackType, ScanResult result) {
        RxBleInternalScanResult rxBleInternalScanResult = new RxBleInternalScanResult(result.getDevice(), result.getRssi(), result.getTimestampNanos(), new ScanRecordImplNativeWrapper(result.getScanRecord()), toScanCallbackType(callbackType));
        return rxBleInternalScanResult;
    }

    @RequiresApi(21)
    private static ScanCallbackType toScanCallbackType(int callbackType) {
        if (callbackType == 4) {
            return ScanCallbackType.CALLBACK_TYPE_MATCH_LOST;
        }
        switch (callbackType) {
            case 1:
                return ScanCallbackType.CALLBACK_TYPE_ALL_MATCHES;
            case 2:
                return ScanCallbackType.CALLBACK_TYPE_FIRST_MATCH;
            default:
                RxBleLog.m25w("Unknown callback type %d -> check android.bluetooth.le.ScanSettings", new Object[0]);
                return ScanCallbackType.CALLBACK_TYPE_UNKNOWN;
        }
    }
}
