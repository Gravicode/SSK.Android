package com.polidea.rxandroidble2.internal.util;

import android.bluetooth.BluetoothGattDescriptor;
import java.util.UUID;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

public class ByteAssociationUtil {
    private ByteAssociationUtil() {
    }

    public static Predicate<? super ByteAssociation<UUID>> characteristicUUIDPredicate(final UUID characteristicUUID) {
        return new Predicate<ByteAssociation<UUID>>() {
            public boolean test(ByteAssociation<UUID> uuidPair) {
                return ((UUID) uuidPair.first).equals(characteristicUUID);
            }
        };
    }

    public static Function<ByteAssociation<?>, byte[]> getBytesFromAssociation() {
        return new Function<ByteAssociation<?>, byte[]>() {
            public byte[] apply(ByteAssociation<?> byteAssociation) {
                return byteAssociation.second;
            }
        };
    }

    public static Predicate<? super ByteAssociation<BluetoothGattDescriptor>> descriptorPredicate(final BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new Predicate<ByteAssociation<BluetoothGattDescriptor>>() {
            public boolean test(ByteAssociation<BluetoothGattDescriptor> uuidPair) throws Exception {
                return ((BluetoothGattDescriptor) uuidPair.first).equals(bluetoothGattDescriptor);
            }
        };
    }
}
