package com.polidea.rxandroidble2.exceptions;

import java.util.UUID;

public class BleConflictingNotificationAlreadySetException extends BleException {
    private final boolean alreadySetIsIndication;
    private final UUID characteristicUuid;

    public BleConflictingNotificationAlreadySetException(UUID characteristicUuid2, boolean alreadySetIsIndication2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Characteristic ");
        sb.append(characteristicUuid2);
        sb.append(" notification already set to ");
        sb.append(alreadySetIsIndication2 ? "indication" : "notification");
        super(sb.toString());
        this.characteristicUuid = characteristicUuid2;
        this.alreadySetIsIndication = alreadySetIsIndication2;
    }

    public UUID getCharacteristicUuid() {
        return this.characteristicUuid;
    }

    public boolean indicationAlreadySet() {
        return this.alreadySetIsIndication;
    }

    public boolean notificationAlreadySet() {
        return !this.alreadySetIsIndication;
    }
}
