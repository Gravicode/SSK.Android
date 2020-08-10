package com.polidea.rxandroidble2.internal;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.UUID;

public class BleIllegalOperationException extends RuntimeException {
    public final UUID characteristicUUID;
    public final int neededProperties;
    public final int supportedProperties;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public BleIllegalOperationException(String message, UUID characteristicUUID2, int supportedProperties2, int neededProperties2) {
        super(message);
        this.characteristicUUID = characteristicUUID2;
        this.supportedProperties = supportedProperties2;
        this.neededProperties = neededProperties2;
    }
}
