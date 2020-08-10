package com.polidea.rxandroidble2.exceptions;

import java.util.UUID;

public class BleServiceNotFoundException extends BleException {
    private final UUID serviceUUID;

    public BleServiceNotFoundException(UUID serviceUUID2) {
        StringBuilder sb = new StringBuilder();
        sb.append("BLE Service not found with UUID ");
        sb.append(serviceUUID2);
        super(sb.toString());
        this.serviceUUID = serviceUUID2;
    }

    public UUID getServiceUUID() {
        return this.serviceUUID;
    }
}
