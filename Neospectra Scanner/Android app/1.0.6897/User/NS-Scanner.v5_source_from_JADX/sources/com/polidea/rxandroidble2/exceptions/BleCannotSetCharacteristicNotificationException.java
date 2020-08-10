package com.polidea.rxandroidble2.exceptions;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BleCannotSetCharacteristicNotificationException extends BleException {
    public static final int CANNOT_FIND_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = 2;
    public static final int CANNOT_SET_LOCAL_NOTIFICATION = 1;
    public static final int CANNOT_WRITE_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = 3;
    @Deprecated
    public static final int UNKNOWN = -1;
    private final BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private final int reason;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    @Deprecated
    public BleCannotSetCharacteristicNotificationException(BluetoothGattCharacteristic bluetoothGattCharacteristic2) {
        super(createMessage(bluetoothGattCharacteristic2, -1));
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic2;
        this.reason = -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public BleCannotSetCharacteristicNotificationException(BluetoothGattCharacteristic bluetoothGattCharacteristic2, int reason2, Throwable cause) {
        super(createMessage(bluetoothGattCharacteristic2, reason2), cause);
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic2;
        this.reason = reason2;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return this.bluetoothGattCharacteristic;
    }

    public int getReason() {
        return this.reason;
    }

    private static String createMessage(BluetoothGattCharacteristic bluetoothGattCharacteristic2, int reason2) {
        StringBuilder sb = new StringBuilder();
        sb.append(reasonDescription(reason2));
        sb.append(" (code ");
        sb.append(reason2);
        sb.append(") with characteristic UUID ");
        sb.append(bluetoothGattCharacteristic2.getUuid());
        return sb.toString();
    }

    private static String reasonDescription(int reason2) {
        switch (reason2) {
            case 1:
                return "Cannot set local notification";
            case 2:
                return "Cannot find client characteristic config descriptor";
            case 3:
                return "Cannot write client characteristic config descriptor";
            default:
                return "Unknown error";
        }
    }
}
