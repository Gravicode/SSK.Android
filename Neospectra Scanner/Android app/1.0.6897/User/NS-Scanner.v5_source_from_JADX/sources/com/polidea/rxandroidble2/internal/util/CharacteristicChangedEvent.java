package com.polidea.rxandroidble2.internal.util;

import java.util.Arrays;
import java.util.UUID;

public class CharacteristicChangedEvent extends CharacteristicNotificationId {
    public final byte[] data;

    public CharacteristicChangedEvent(UUID uuid, Integer instanceId, byte[] data2) {
        super(uuid, instanceId);
        this.data = data2;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacteristicChangedEvent)) {
            if (!(o instanceof CharacteristicNotificationId) || !super.equals(o)) {
                z = false;
            }
            return z;
        } else if (!super.equals(o)) {
            return false;
        } else {
            return Arrays.equals(this.data, ((CharacteristicChangedEvent) o).data);
        }
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(this.data);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CharacteristicChangedEvent{UUID=");
        sb.append(((UUID) this.first).toString());
        sb.append(", instanceId=");
        sb.append(((Integer) this.second).toString());
        sb.append(", data=");
        sb.append(Arrays.toString(this.data));
        sb.append('}');
        return sb.toString();
    }
}
