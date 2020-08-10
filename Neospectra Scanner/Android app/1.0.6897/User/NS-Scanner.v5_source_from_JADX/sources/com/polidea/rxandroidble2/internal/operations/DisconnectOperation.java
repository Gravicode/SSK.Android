package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.os.DeadObjectException;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import p005io.reactivex.Emitter;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

public class DisconnectOperation extends QueueOperation<Void> {
    private final BluetoothGattProvider bluetoothGattProvider;
    private final Scheduler bluetoothInteractionScheduler;
    private final BluetoothManager bluetoothManager;
    private final ConnectionStateChangeListener connectionStateChangeListener;
    private final String macAddress;
    private final RxBleGattCallback rxBleGattCallback;
    private final TimeoutConfiguration timeoutConfiguration;

    private static class DisconnectGattObservable extends Single<BluetoothGatt> {
        /* access modifiers changed from: private */
        public final BluetoothGatt bluetoothGatt;
        private final Scheduler disconnectScheduler;
        private final RxBleGattCallback rxBleGattCallback;

        DisconnectGattObservable(BluetoothGatt bluetoothGatt2, RxBleGattCallback rxBleGattCallback2, Scheduler disconnectScheduler2) {
            this.bluetoothGatt = bluetoothGatt2;
            this.rxBleGattCallback = rxBleGattCallback2;
            this.disconnectScheduler = disconnectScheduler2;
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(SingleObserver<? super BluetoothGatt> observer) {
            this.rxBleGattCallback.getOnConnectionStateChange().filter(new Predicate<RxBleConnectionState>() {
                public boolean test(RxBleConnectionState rxBleConnectionState) {
                    return rxBleConnectionState == RxBleConnectionState.DISCONNECTED;
                }
            }).firstOrError().map(new Function<RxBleConnectionState, BluetoothGatt>() {
                public BluetoothGatt apply(RxBleConnectionState rxBleConnectionState) {
                    return DisconnectGattObservable.this.bluetoothGatt;
                }
            }).subscribe(observer);
            this.disconnectScheduler.createWorker().schedule(new Runnable() {
                public void run() {
                    DisconnectGattObservable.this.bluetoothGatt.disconnect();
                }
            });
        }
    }

    @Inject
    DisconnectOperation(RxBleGattCallback rxBleGattCallback2, BluetoothGattProvider bluetoothGattProvider2, @Named("mac-address") String macAddress2, BluetoothManager bluetoothManager2, @Named("bluetooth_interaction") Scheduler bluetoothInteractionScheduler2, @Named("disconnect-timeout") TimeoutConfiguration timeoutConfiguration2, ConnectionStateChangeListener connectionStateChangeListener2) {
        this.rxBleGattCallback = rxBleGattCallback2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.macAddress = macAddress2;
        this.bluetoothManager = bluetoothManager2;
        this.bluetoothInteractionScheduler = bluetoothInteractionScheduler2;
        this.timeoutConfiguration = timeoutConfiguration2;
        this.connectionStateChangeListener = connectionStateChangeListener2;
    }

    /* access modifiers changed from: protected */
    public void protectedRun(final ObservableEmitter<Void> emitter, final QueueReleaseInterface queueReleaseInterface) {
        this.connectionStateChangeListener.onConnectionStateChange(RxBleConnectionState.DISCONNECTING);
        BluetoothGatt bluetoothGatt = this.bluetoothGattProvider.getBluetoothGatt();
        if (bluetoothGatt == null) {
            RxBleLog.m25w("Disconnect operation has been executed but GATT instance was null - considering disconnected.", new Object[0]);
            considerGattDisconnected(emitter, queueReleaseInterface);
            return;
        }
        disconnectIfRequired(bluetoothGatt).observeOn(this.bluetoothInteractionScheduler).subscribe((SingleObserver<? super T>) new SingleObserver<BluetoothGatt>() {
            public void onSubscribe(Disposable d) {
            }

            public void onSuccess(BluetoothGatt bluetoothGatt) {
                bluetoothGatt.close();
                DisconnectOperation.this.considerGattDisconnected(emitter, queueReleaseInterface);
            }

            public void onError(Throwable throwable) {
                RxBleLog.m26w(throwable, "Disconnect operation has been executed but finished with an error - considering disconnected.", new Object[0]);
                DisconnectOperation.this.considerGattDisconnected(emitter, queueReleaseInterface);
            }
        });
    }

    private Single<BluetoothGatt> disconnectIfRequired(BluetoothGatt bluetoothGatt) {
        if (isDisconnected(bluetoothGatt)) {
            return Single.just(bluetoothGatt);
        }
        return disconnect(bluetoothGatt);
    }

    /* access modifiers changed from: 0000 */
    @RestrictTo({Scope.SUBCLASSES})
    public void considerGattDisconnected(Emitter<Void> emitter, QueueReleaseInterface queueReleaseInterface) {
        this.connectionStateChangeListener.onConnectionStateChange(RxBleConnectionState.DISCONNECTED);
        queueReleaseInterface.release();
        emitter.onComplete();
    }

    private boolean isDisconnected(BluetoothGatt bluetoothGatt) {
        return this.bluetoothManager.getConnectionState(bluetoothGatt.getDevice(), 7) == 0;
    }

    private Single<BluetoothGatt> disconnect(BluetoothGatt bluetoothGatt) {
        return new DisconnectGattObservable(bluetoothGatt, this.rxBleGattCallback, this.bluetoothInteractionScheduler).timeout(this.timeoutConfiguration.timeout, this.timeoutConfiguration.timeoutTimeUnit, this.timeoutConfiguration.timeoutScheduler, Single.just(bluetoothGatt));
    }

    /* access modifiers changed from: protected */
    public BleException provideException(DeadObjectException deadObjectException) {
        return new BleDisconnectedException(deadObjectException, this.macAddress);
    }
}
