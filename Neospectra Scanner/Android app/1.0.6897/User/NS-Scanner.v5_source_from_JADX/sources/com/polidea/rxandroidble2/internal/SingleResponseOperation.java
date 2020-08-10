package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothGatt;
import android.os.DeadObjectException;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattCallbackTimeoutException;
import com.polidea.rxandroidble2.exceptions.BleGattCannotStartException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.QueueReleasingEmitterWrapper;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;

public abstract class SingleResponseOperation<T> extends QueueOperation<T> {
    private final BluetoothGatt bluetoothGatt;
    private final BleGattOperationType operationType;
    private final RxBleGattCallback rxBleGattCallback;
    private final TimeoutConfiguration timeoutConfiguration;

    /* access modifiers changed from: protected */
    public abstract Single<T> getCallback(RxBleGattCallback rxBleGattCallback2);

    /* access modifiers changed from: protected */
    public abstract boolean startOperation(BluetoothGatt bluetoothGatt2);

    public SingleResponseOperation(BluetoothGatt bluetoothGatt2, RxBleGattCallback rxBleGattCallback2, BleGattOperationType gattOperationType, TimeoutConfiguration timeoutConfiguration2) {
        this.bluetoothGatt = bluetoothGatt2;
        this.rxBleGattCallback = rxBleGattCallback2;
        this.operationType = gattOperationType;
        this.timeoutConfiguration = timeoutConfiguration2;
    }

    /* access modifiers changed from: protected */
    public final void protectedRun(ObservableEmitter<T> emitter, QueueReleaseInterface queueReleaseInterface) throws Throwable {
        QueueReleasingEmitterWrapper<T> emitterWrapper = new QueueReleasingEmitterWrapper<>(emitter, queueReleaseInterface);
        getCallback(this.rxBleGattCallback).timeout(this.timeoutConfiguration.timeout, this.timeoutConfiguration.timeoutTimeUnit, this.timeoutConfiguration.timeoutScheduler, timeoutFallbackProcedure(this.bluetoothGatt, this.rxBleGattCallback, this.timeoutConfiguration.timeoutScheduler)).toObservable().subscribe((Observer<? super T>) emitterWrapper);
        if (!startOperation(this.bluetoothGatt)) {
            emitterWrapper.cancel();
            emitterWrapper.onError(new BleGattCannotStartException(this.bluetoothGatt, this.operationType));
        }
    }

    /* access modifiers changed from: protected */
    public Single<T> timeoutFallbackProcedure(BluetoothGatt bluetoothGatt2, RxBleGattCallback rxBleGattCallback2, Scheduler timeoutScheduler) {
        return Single.error((Throwable) new BleGattCallbackTimeoutException(this.bluetoothGatt, this.operationType));
    }

    /* access modifiers changed from: protected */
    public BleException provideException(DeadObjectException deadObjectException) {
        return new BleDisconnectedException(deadObjectException, this.bluetoothGatt.getDevice().getAddress());
    }
}
