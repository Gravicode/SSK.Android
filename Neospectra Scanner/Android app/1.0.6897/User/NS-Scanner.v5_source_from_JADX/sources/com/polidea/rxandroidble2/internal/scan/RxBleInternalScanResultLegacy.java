package com.polidea.rxandroidble2.internal.scan;

import android.bluetooth.BluetoothDevice;

public class RxBleInternalScanResultLegacy {
    private final BluetoothDevice bluetoothDevice;
    private final int rssi;
    private final byte[] scanRecord;

    public RxBleInternalScanResultLegacy(BluetoothDevice bleDevice, int rssi2, byte[] scanRecords) {
        this.bluetoothDevice = bleDevice;
        this.rssi = rssi2;
        this.scanRecord = scanRecords;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    public int getRssi() {
        return this.rssi;
    }

    public byte[] getScanRecord() {
        return this.scanRecord;
    }
}
