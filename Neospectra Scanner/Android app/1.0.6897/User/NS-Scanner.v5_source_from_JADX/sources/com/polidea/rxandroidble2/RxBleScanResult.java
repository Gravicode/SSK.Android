package com.polidea.rxandroidble2;

public class RxBleScanResult {
    private final RxBleDevice bleDevice;
    private final int rssi;
    private final byte[] scanRecord;

    public RxBleScanResult(RxBleDevice bleDevice2, int rssi2, byte[] scanRecords) {
        this.bleDevice = bleDevice2;
        this.rssi = rssi2;
        this.scanRecord = scanRecords;
    }

    public RxBleDevice getBleDevice() {
        return this.bleDevice;
    }

    public int getRssi() {
        return this.rssi;
    }

    public byte[] getScanRecord() {
        return this.scanRecord;
    }
}
