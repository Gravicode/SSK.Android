package com.polidea.rxandroidble2.exceptions;

import java.util.UUID;

public class BleDescriptorNotFoundException extends BleException {
    private final UUID descriptorUUID;

    public BleDescriptorNotFoundException(UUID descriptorUUID2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Descriptor not found with UUID ");
        sb.append(descriptorUUID2);
        super(sb.toString());
        this.descriptorUUID = descriptorUUID2;
    }

    public UUID getDescriptorUUID() {
        return this.descriptorUUID;
    }
}
