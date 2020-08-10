package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.BleIllegalOperationException;
import p005io.reactivex.Completable;
import p005io.reactivex.functions.Action;

public class IllegalOperationChecker {
    /* access modifiers changed from: private */
    public IllegalOperationHandler resultHandler;

    @Inject
    public IllegalOperationChecker(IllegalOperationHandler resultHandler2) {
        this.resultHandler = resultHandler2;
    }

    public Completable checkAnyPropertyMatches(final BluetoothGattCharacteristic characteristic, final int neededProperties) {
        return Completable.fromAction(new Action() {
            public void run() {
                if ((neededProperties & characteristic.getProperties()) == 0) {
                    BleIllegalOperationException exception = IllegalOperationChecker.this.resultHandler.handleMismatchData(characteristic, neededProperties);
                    if (exception != null) {
                        throw exception;
                    }
                }
            }
        });
    }
}
