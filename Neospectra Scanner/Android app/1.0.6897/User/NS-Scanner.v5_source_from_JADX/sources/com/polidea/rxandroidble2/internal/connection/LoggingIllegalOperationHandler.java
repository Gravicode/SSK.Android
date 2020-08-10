package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.BleIllegalOperationException;
import com.polidea.rxandroidble2.internal.RxBleLog;

public class LoggingIllegalOperationHandler extends IllegalOperationHandler {
    @Inject
    public LoggingIllegalOperationHandler(IllegalOperationMessageCreator messageCreator) {
        super(messageCreator);
    }

    public BleIllegalOperationException handleMismatchData(BluetoothGattCharacteristic characteristic, int neededProperties) {
        RxBleLog.m25w(this.messageCreator.createMismatchMessage(characteristic, neededProperties), new Object[0]);
        return null;
    }
}
