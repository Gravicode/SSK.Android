package com.polidea.rxandroidble2.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BleDisconnectedException extends BleException {
    @NonNull
    public final String bluetoothDeviceAddress;

    @Deprecated
    public BleDisconnectedException() {
        this.bluetoothDeviceAddress = "";
    }

    public BleDisconnectedException(Throwable throwable, @NonNull String bluetoothDeviceAddress2) {
        super(createMessage(bluetoothDeviceAddress2), throwable);
        this.bluetoothDeviceAddress = bluetoothDeviceAddress2;
    }

    public BleDisconnectedException(@NonNull String bluetoothDeviceAddress2) {
        super(createMessage(bluetoothDeviceAddress2));
        this.bluetoothDeviceAddress = bluetoothDeviceAddress2;
    }

    private static String createMessage(@Nullable String bluetoothDeviceAddress2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Disconnected from ");
        sb.append(bluetoothDeviceAddress2);
        return sb.toString();
    }
}
