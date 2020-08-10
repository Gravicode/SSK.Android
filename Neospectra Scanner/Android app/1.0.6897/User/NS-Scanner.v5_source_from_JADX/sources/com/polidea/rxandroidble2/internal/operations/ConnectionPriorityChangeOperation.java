package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.support.annotation.RequiresApi;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.exceptions.BleGattCannotStartException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.SingleResponseOperation;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;

public class ConnectionPriorityChangeOperation extends SingleResponseOperation<Long> {
    private final int connectionPriority;
    private final Scheduler delayScheduler;
    private final long operationTimeout;
    private final TimeUnit timeUnit;

    @Inject
    ConnectionPriorityChangeOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration, int connectionPriority2, long operationTimeout2, TimeUnit timeUnit2, Scheduler delayScheduler2) {
        super(bluetoothGatt, rxBleGattCallback, BleGattOperationType.CONNECTION_PRIORITY_CHANGE, timeoutConfiguration);
        this.connectionPriority = connectionPriority2;
        this.operationTimeout = operationTimeout2;
        this.timeUnit = timeUnit2;
        this.delayScheduler = delayScheduler2;
    }

    /* access modifiers changed from: protected */
    public Single<Long> getCallback(RxBleGattCallback rxBleGattCallback) {
        return Single.timer(this.operationTimeout, this.timeUnit, this.delayScheduler);
    }

    /* access modifiers changed from: protected */
    @RequiresApi(api = 21)
    public boolean startOperation(BluetoothGatt bluetoothGatt) throws IllegalArgumentException, BleGattCannotStartException {
        return bluetoothGatt.requestConnectionPriority(this.connectionPriority);
    }
}
