package com.polidea.rxandroidble2.scan;

import com.polidea.rxandroidble2.RxBleDevice;

public class ScanResult {
    private final RxBleDevice bleDevice;
    private final ScanCallbackType callbackType;
    private final int rssi;
    private final ScanRecord scanRecord;
    private final long timestampNanos;

    public ScanResult(RxBleDevice bleDevice2, int rssi2, long timestampNanos2, ScanCallbackType callbackType2, ScanRecord scanRecord2) {
        this.bleDevice = bleDevice2;
        this.rssi = rssi2;
        this.timestampNanos = timestampNanos2;
        this.callbackType = callbackType2;
        this.scanRecord = scanRecord2;
    }

    public RxBleDevice getBleDevice() {
        return this.bleDevice;
    }

    public int getRssi() {
        return this.rssi;
    }

    public long getTimestampNanos() {
        return this.timestampNanos;
    }

    public ScanCallbackType getCallbackType() {
        return this.callbackType;
    }

    public ScanRecord getScanRecord() {
        return this.scanRecord;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ScanResult{bleDevice=");
        sb.append(this.bleDevice);
        sb.append(", rssi=");
        sb.append(this.rssi);
        sb.append(", timestampNanos=");
        sb.append(this.timestampNanos);
        sb.append(", callbackType=");
        sb.append(this.callbackType);
        sb.append(", scanRecord=");
        sb.append(this.scanRecord);
        sb.append('}');
        return sb.toString();
    }
}
