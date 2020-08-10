package com.polidea.rxandroidble2.exceptions;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public class BleGattCharacteristicException extends BleGattException {
    public final BluetoothGattCharacteristic characteristic;

    public BleGattCharacteristicException(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic2, int status, BleGattOperationType bleGattOperationType) {
        super(gatt, status, bleGattOperationType);
        this.characteristic = characteristic2;
    }
}
