package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;
import bleshadow.javax.inject.Inject;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.ConnectionSetup;
import com.polidea.rxandroidble2.ConnectionSetup.Builder;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.Timeout;
import com.polidea.rxandroidble2.exceptions.BleAlreadyConnectedException;
import com.polidea.rxandroidble2.internal.connection.Connector;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.functions.Action;

@DeviceScope
class RxBleDeviceImpl implements RxBleDevice {
    /* access modifiers changed from: private */
    public final BluetoothDevice bluetoothDevice;
    private final BehaviorRelay<RxBleConnectionState> connectionStateRelay;
    /* access modifiers changed from: private */
    public final Connector connector;
    /* access modifiers changed from: private */
    public AtomicBoolean isConnected = new AtomicBoolean(false);

    @Inject
    RxBleDeviceImpl(BluetoothDevice bluetoothDevice2, Connector connector2, BehaviorRelay<RxBleConnectionState> connectionStateRelay2) {
        this.bluetoothDevice = bluetoothDevice2;
        this.connector = connector2;
        this.connectionStateRelay = connectionStateRelay2;
    }

    public Observable<RxBleConnectionState> observeConnectionStateChanges() {
        return this.connectionStateRelay.distinctUntilChanged().skip(1);
    }

    public RxBleConnectionState getConnectionState() {
        return (RxBleConnectionState) this.connectionStateRelay.getValue();
    }

    public Observable<RxBleConnection> establishConnection(boolean autoConnect) {
        return establishConnection(new Builder().setAutoConnect(autoConnect).setSuppressIllegalOperationCheck(true).build());
    }

    public Observable<RxBleConnection> establishConnection(boolean autoConnect, Timeout timeout) {
        return establishConnection(new Builder().setAutoConnect(autoConnect).setOperationTimeout(timeout).setSuppressIllegalOperationCheck(true).build());
    }

    public Observable<RxBleConnection> establishConnection(final ConnectionSetup options) {
        return Observable.defer(new Callable<ObservableSource<RxBleConnection>>() {
            public ObservableSource<RxBleConnection> call() throws Exception {
                if (RxBleDeviceImpl.this.isConnected.compareAndSet(false, true)) {
                    return RxBleDeviceImpl.this.connector.prepareConnection(options).doFinally(new Action() {
                        public void run() {
                            RxBleDeviceImpl.this.isConnected.set(false);
                        }
                    });
                }
                return Observable.error((Throwable) new BleAlreadyConnectedException(RxBleDeviceImpl.this.bluetoothDevice.getAddress()));
            }
        });
    }

    @Nullable
    public String getName() {
        return this.bluetoothDevice.getName();
    }

    public String getMacAddress() {
        return this.bluetoothDevice.getAddress();
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RxBleDeviceImpl)) {
            return false;
        }
        return this.bluetoothDevice.equals(((RxBleDeviceImpl) o).bluetoothDevice);
    }

    public int hashCode() {
        return this.bluetoothDevice.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RxBleDeviceImpl{bluetoothDevice=");
        sb.append(this.bluetoothDevice.getName());
        sb.append('(');
        sb.append(this.bluetoothDevice.getAddress());
        sb.append(')');
        sb.append('}');
        return sb.toString();
    }
}
