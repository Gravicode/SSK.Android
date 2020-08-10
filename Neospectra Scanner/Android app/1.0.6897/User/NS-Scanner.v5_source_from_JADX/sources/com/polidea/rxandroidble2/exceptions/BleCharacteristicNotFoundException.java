package com.polidea.rxandroidble2.exceptions;

import java.util.UUID;

public class BleCharacteristicNotFoundException extends BleException {
    private final UUID charactersisticUUID;

    public BleCharacteristicNotFoundException(UUID charactersisticUUID2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Characteristic not found with UUID ");
        sb.append(charactersisticUUID2);
        super(sb.toString());
        this.charactersisticUUID = charactersisticUUID2;
    }

    public UUID getCharactersisticUUID() {
        return this.charactersisticUUID;
    }
}
