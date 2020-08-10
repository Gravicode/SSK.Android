package com.polidea.rxandroidble2.exceptions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BleGattException extends BleException {
    public static final int UNKNOWN_STATUS = -1;
    private final BleGattOperationType bleGattOperationType;
    @Nullable
    private final BluetoothGatt gatt;
    private final int status;

    @Deprecated
    public BleGattException(int status2, BleGattOperationType bleGattOperationType2) {
        super(createMessage(null, status2, bleGattOperationType2));
        this.gatt = null;
        this.status = status2;
        this.bleGattOperationType = bleGattOperationType2;
    }

    public BleGattException(@NonNull BluetoothGatt gatt2, int status2, BleGattOperationType bleGattOperationType2) {
        super(createMessage(gatt2, status2, bleGattOperationType2));
        this.gatt = gatt2;
        this.status = status2;
        this.bleGattOperationType = bleGattOperationType2;
    }

    public BleGattException(BluetoothGatt gatt2, BleGattOperationType bleGattOperationType2) {
        this(gatt2, -1, bleGattOperationType2);
    }

    public String getMacAddress() {
        return getMacAddress(this.gatt);
    }

    public BleGattOperationType getBleGattOperationType() {
        return this.bleGattOperationType;
    }

    public int getStatus() {
        return this.status;
    }

    private static String getMacAddress(@Nullable BluetoothGatt gatt2) {
        if (gatt2 == null || gatt2.getDevice() == null) {
            return null;
        }
        return gatt2.getDevice().getAddress();
    }

    @SuppressLint({"DefaultLocale"})
    private static String createMessage(@Nullable BluetoothGatt gatt2, int status2, BleGattOperationType bleGattOperationType2) {
        if (status2 == -1) {
            return String.format("GATT exception from MAC address %s, with type %s", new Object[]{getMacAddress(gatt2), bleGattOperationType2});
        }
        String str = "https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h";
        return String.format("GATT exception from MAC address %s, status %d, type %s. (Look up status 0x%02x here %s)", new Object[]{getMacAddress(gatt2), Integer.valueOf(status2), bleGattOperationType2, Integer.valueOf(status2), "https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h"});
    }
}
