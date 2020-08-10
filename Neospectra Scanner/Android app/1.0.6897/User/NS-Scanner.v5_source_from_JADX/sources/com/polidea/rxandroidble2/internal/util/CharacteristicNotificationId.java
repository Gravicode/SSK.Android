package com.polidea.rxandroidble2.internal.util;

import android.util.Pair;
import java.util.UUID;

public class CharacteristicNotificationId extends Pair<UUID, Integer> {
    public CharacteristicNotificationId(UUID uuid, Integer instanceId) {
        super(uuid, instanceId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CharacteristicNotificationId{UUID=");
        sb.append(((UUID) this.first).toString());
        sb.append(", instanceId=");
        sb.append(((Integer) this.second).toString());
        sb.append('}');
        return sb.toString();
    }
}
