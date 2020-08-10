package com.polidea.rxandroidble2.internal.util;

import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.internal.RxBleLog;

public class CharacteristicPropertiesParser {
    private final int[] possibleProperties = getPossibleProperties();
    private final int propertyBroadcast;
    private final int propertyIndicate;
    private final int propertyNotify;
    private final int propertyRead;
    private final int propertySignedWrite;
    private final int propertyWrite;
    private final int propertyWriteNoResponse;

    public CharacteristicPropertiesParser(int propertyBroadcast2, int propertyRead2, int propertyWriteNoResponse2, int propertyWrite2, int propertyNotify2, int propertyIndicate2, int propertySignedWrite2) {
        this.propertyBroadcast = propertyBroadcast2;
        this.propertyRead = propertyRead2;
        this.propertyWriteNoResponse = propertyWriteNoResponse2;
        this.propertyWrite = propertyWrite2;
        this.propertyNotify = propertyNotify2;
        this.propertyIndicate = propertyIndicate2;
        this.propertySignedWrite = propertySignedWrite2;
    }

    @NonNull
    public String propertiesIntToString(int property) {
        int[] iArr;
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for (int possibleProperty : this.possibleProperties) {
            if (propertiesIntContains(property, possibleProperty)) {
                builder.append(propertyToString(possibleProperty));
                builder.append(" ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    @NonNull
    private int[] getPossibleProperties() {
        return new int[]{this.propertyBroadcast, this.propertyRead, this.propertyWriteNoResponse, this.propertyWrite, this.propertyNotify, this.propertyIndicate, this.propertySignedWrite};
    }

    private static boolean propertiesIntContains(int properties, int property) {
        return (properties & property) != 0;
    }

    @NonNull
    private String propertyToString(int property) {
        if (property == this.propertyRead) {
            return "READ";
        }
        if (property == this.propertyWrite) {
            return "WRITE";
        }
        if (property == this.propertyWriteNoResponse) {
            return "WRITE_NO_RESPONSE";
        }
        if (property == this.propertySignedWrite) {
            return "SIGNED_WRITE";
        }
        if (property == this.propertyIndicate) {
            return "INDICATE";
        }
        if (property == this.propertyBroadcast) {
            return "BROADCAST";
        }
        if (property == this.propertyNotify) {
            return "NOTIFY";
        }
        if (property == 0) {
            return "";
        }
        RxBleLog.m19e("Unknown property specified", new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("UNKNOWN (");
        sb.append(property);
        sb.append(" -> check android.bluetooth.BluetoothGattCharacteristic)");
        return sb.toString();
    }
}
