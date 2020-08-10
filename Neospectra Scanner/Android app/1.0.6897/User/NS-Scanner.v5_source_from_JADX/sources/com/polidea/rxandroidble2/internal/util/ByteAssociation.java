package com.polidea.rxandroidble2.internal.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import java.util.Arrays;
import java.util.UUID;

public class ByteAssociation<T> {
    public final T first;
    public final byte[] second;

    public ByteAssociation(@NonNull T first2, byte[] second2) {
        this.first = first2;
        this.second = second2;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof ByteAssociation)) {
            return false;
        }
        ByteAssociation<?> ba = (ByteAssociation) o;
        if (Arrays.equals(ba.second, this.second) && ba.first.equals(this.first)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.first.hashCode() ^ Arrays.hashCode(this.second);
    }

    public String toString() {
        String firstDescription;
        if (this.first instanceof BluetoothGattCharacteristic) {
            StringBuilder sb = new StringBuilder();
            sb.append(BluetoothGattCharacteristic.class.getSimpleName());
            sb.append("(");
            sb.append(((BluetoothGattCharacteristic) this.first).getUuid().toString());
            sb.append(")");
            firstDescription = sb.toString();
        } else if (this.first instanceof BluetoothGattDescriptor) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(BluetoothGattDescriptor.class.getSimpleName());
            sb2.append("(");
            sb2.append(((BluetoothGattDescriptor) this.first).getUuid().toString());
            sb2.append(")");
            firstDescription = sb2.toString();
        } else if (this.first instanceof UUID) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(UUID.class.getSimpleName());
            sb3.append("(");
            sb3.append(this.first.toString());
            sb3.append(")");
            firstDescription = sb3.toString();
        } else {
            firstDescription = this.first.getClass().getSimpleName();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(getClass().getSimpleName());
        sb4.append("[first=");
        sb4.append(firstDescription);
        sb4.append(", second=");
        sb4.append(Arrays.toString(this.second));
        sb4.append("]");
        return sb4.toString();
    }

    public static <T> ByteAssociation<T> create(T first2, byte[] bytes) {
        return new ByteAssociation<>(first2, bytes);
    }
}
