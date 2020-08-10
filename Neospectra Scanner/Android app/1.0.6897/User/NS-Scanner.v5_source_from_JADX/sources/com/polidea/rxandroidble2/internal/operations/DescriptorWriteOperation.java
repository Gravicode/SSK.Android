package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.SingleResponseOperation;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.util.ByteAssociationUtil;
import p005io.reactivex.Single;

public class DescriptorWriteOperation extends SingleResponseOperation<byte[]> {
    private final int bluetoothGattCharacteristicDefaultWriteType;
    private BluetoothGattDescriptor bluetoothGattDescriptor;
    private byte[] data;

    DescriptorWriteOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, @Named("operation-timeout") TimeoutConfiguration timeoutConfiguration, int bluetoothGattCharacteristicDefaultWriteType2, BluetoothGattDescriptor bluetoothGattDescriptor2, byte[] data2) {
        super(bluetoothGatt, rxBleGattCallback, BleGattOperationType.DESCRIPTOR_WRITE, timeoutConfiguration);
        this.bluetoothGattCharacteristicDefaultWriteType = bluetoothGattCharacteristicDefaultWriteType2;
        this.bluetoothGattDescriptor = bluetoothGattDescriptor2;
        this.data = data2;
    }

    /* access modifiers changed from: protected */
    public Single<byte[]> getCallback(RxBleGattCallback rxBleGattCallback) {
        return rxBleGattCallback.getOnDescriptorWrite().filter(ByteAssociationUtil.descriptorPredicate(this.bluetoothGattDescriptor)).firstOrError().map(ByteAssociationUtil.getBytesFromAssociation());
    }

    /* access modifiers changed from: protected */
    public boolean startOperation(BluetoothGatt bluetoothGatt) {
        this.bluetoothGattDescriptor.setValue(this.data);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.bluetoothGattDescriptor.getCharacteristic();
        int originalWriteType = bluetoothGattCharacteristic.getWriteType();
        bluetoothGattCharacteristic.setWriteType(this.bluetoothGattCharacteristicDefaultWriteType);
        boolean success = bluetoothGatt.writeDescriptor(this.bluetoothGattDescriptor);
        bluetoothGattCharacteristic.setWriteType(originalWriteType);
        return success;
    }
}
