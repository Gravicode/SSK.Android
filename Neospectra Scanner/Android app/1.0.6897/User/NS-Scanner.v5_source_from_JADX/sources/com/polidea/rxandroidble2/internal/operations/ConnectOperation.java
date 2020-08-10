package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattCallbackTimeoutException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.connection.BluetoothGattProvider;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.BleConnectionCompat;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;
import java.util.concurrent.Callable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Single;
import p005io.reactivex.SingleEmitter;
import p005io.reactivex.SingleOnSubscribe;
import p005io.reactivex.SingleTransformer;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.observers.DisposableSingleObserver;

public class ConnectOperation extends QueueOperation<BluetoothGatt> {
    /* access modifiers changed from: private */
    public final boolean autoConnect;
    /* access modifiers changed from: private */
    public final BluetoothDevice bluetoothDevice;
    /* access modifiers changed from: private */
    public final BluetoothGattProvider bluetoothGattProvider;
    /* access modifiers changed from: private */
    public final TimeoutConfiguration connectTimeout;
    /* access modifiers changed from: private */
    public final BleConnectionCompat connectionCompat;
    /* access modifiers changed from: private */
    public final ConnectionStateChangeListener connectionStateChangedAction;
    /* access modifiers changed from: private */
    public final RxBleGattCallback rxBleGattCallback;

    @Inject
    ConnectOperation(BluetoothDevice bluetoothDevice2, BleConnectionCompat connectionCompat2, RxBleGattCallback rxBleGattCallback2, BluetoothGattProvider bluetoothGattProvider2, @Named("connect-timeout") TimeoutConfiguration connectTimeout2, @Named("autoConnect") boolean autoConnect2, ConnectionStateChangeListener connectionStateChangedAction2) {
        this.bluetoothDevice = bluetoothDevice2;
        this.connectionCompat = connectionCompat2;
        this.rxBleGattCallback = rxBleGattCallback2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.connectTimeout = connectTimeout2;
        this.autoConnect = autoConnect2;
        this.connectionStateChangedAction = connectionStateChangedAction2;
    }

    /* access modifiers changed from: protected */
    public void protectedRun(ObservableEmitter<BluetoothGatt> emitter, final QueueReleaseInterface queueReleaseInterface) {
        emitter.setDisposable((DisposableSingleObserver) getConnectedBluetoothGatt().compose(wrapWithTimeoutWhenNotAutoconnecting()).doFinally(new Action() {
            public void run() {
                queueReleaseInterface.release();
            }
        }).subscribeWith(DisposableUtil.disposableSingleObserverFromEmitter(emitter)));
        if (this.autoConnect) {
            queueReleaseInterface.release();
        }
    }

    private SingleTransformer<BluetoothGatt, BluetoothGatt> wrapWithTimeoutWhenNotAutoconnecting() {
        return new SingleTransformer<BluetoothGatt, BluetoothGatt>() {
            public Single<BluetoothGatt> apply(Single<BluetoothGatt> bluetoothGattSingle) {
                if (ConnectOperation.this.autoConnect) {
                    return bluetoothGattSingle;
                }
                return bluetoothGattSingle.timeout(ConnectOperation.this.connectTimeout.timeout, ConnectOperation.this.connectTimeout.timeoutTimeUnit, ConnectOperation.this.connectTimeout.timeoutScheduler, ConnectOperation.this.prepareConnectionTimeoutError());
            }
        };
    }

    /* access modifiers changed from: private */
    @NonNull
    public Single<BluetoothGatt> prepareConnectionTimeoutError() {
        return Single.fromCallable(new Callable<BluetoothGatt>() {
            public BluetoothGatt call() {
                throw new BleGattCallbackTimeoutException(ConnectOperation.this.bluetoothGattProvider.getBluetoothGatt(), BleGattOperationType.CONNECTION_STATE);
            }
        });
    }

    @NonNull
    private Single<BluetoothGatt> getConnectedBluetoothGatt() {
        return Single.create(new SingleOnSubscribe<BluetoothGatt>() {
            public void subscribe(SingleEmitter<BluetoothGatt> emitter) throws Exception {
                emitter.setDisposable((DisposableSingleObserver) ConnectOperation.this.getBluetoothGattAndChangeStatusToConnected().delaySubscription((ObservableSource<U>) ConnectOperation.this.rxBleGattCallback.getOnConnectionStateChange().filter(new Predicate<RxBleConnectionState>() {
                    public boolean test(RxBleConnectionState rxBleConnectionState) throws Exception {
                        return rxBleConnectionState == RxBleConnectionState.CONNECTED;
                    }
                })).mergeWith(ConnectOperation.this.rxBleGattCallback.observeDisconnect().firstOrError()).firstOrError().subscribeWith(DisposableUtil.disposableSingleObserverFromEmitter(emitter)));
                ConnectOperation.this.connectionStateChangedAction.onConnectionStateChange(RxBleConnectionState.CONNECTING);
                ConnectOperation.this.bluetoothGattProvider.updateBluetoothGatt(ConnectOperation.this.connectionCompat.connectGatt(ConnectOperation.this.bluetoothDevice, ConnectOperation.this.autoConnect, ConnectOperation.this.rxBleGattCallback.getBluetoothGattCallback()));
            }
        });
    }

    /* access modifiers changed from: private */
    public Single<BluetoothGatt> getBluetoothGattAndChangeStatusToConnected() {
        return Single.fromCallable(new Callable<BluetoothGatt>() {
            public BluetoothGatt call() {
                ConnectOperation.this.connectionStateChangedAction.onConnectionStateChange(RxBleConnectionState.CONNECTED);
                return ConnectOperation.this.bluetoothGattProvider.getBluetoothGatt();
            }
        });
    }

    /* access modifiers changed from: protected */
    public BleException provideException(DeadObjectException deadObjectException) {
        return new BleDisconnectedException(deadObjectException, this.bluetoothDevice.getAddress());
    }
}
