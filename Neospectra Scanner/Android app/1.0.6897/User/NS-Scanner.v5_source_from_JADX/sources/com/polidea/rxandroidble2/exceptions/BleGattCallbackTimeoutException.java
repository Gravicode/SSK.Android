package com.polidea.rxandroidble2.exceptions;

import android.bluetooth.BluetoothGatt;

public class BleGattCallbackTimeoutException extends BleGattException {
    public BleGattCallbackTimeoutException(BluetoothGatt gatt, BleGattOperationType bleGattOperationType) {
        super(gatt, bleGattOperationType);
    }
}
