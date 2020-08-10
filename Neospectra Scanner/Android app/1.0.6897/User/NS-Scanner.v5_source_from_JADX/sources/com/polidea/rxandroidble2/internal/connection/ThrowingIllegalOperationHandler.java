package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.BleIllegalOperationException;
import com.polidea.rxandroidble2.internal.RxBleLog;

public class ThrowingIllegalOperationHandler extends IllegalOperationHandler {
    @Inject
    public ThrowingIllegalOperationHandler(IllegalOperationMessageCreator messageCreator) {
        super(messageCreator);
    }

    public BleIllegalOperationException handleMismatchData(BluetoothGattCharacteristic characteristic, int neededProperties) {
        String message = this.messageCreator.createMismatchMessage(characteristic, neededProperties);
        RxBleLog.m19e(message, new Object[0]);
        return new BleIllegalOperationException(message, characteristic.getUuid(), characteristic.getProperties(), neededProperties);
    }
}
