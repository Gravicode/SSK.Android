package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import p005io.reactivex.Observable;

public interface RxBleDevice {
    Observable<RxBleConnection> establishConnection(boolean z);

    Observable<RxBleConnection> establishConnection(boolean z, @NonNull Timeout timeout);

    BluetoothDevice getBluetoothDevice();

    RxBleConnectionState getConnectionState();

    String getMacAddress();

    @Nullable
    String getName();

    Observable<RxBleConnectionState> observeConnectionStateChanges();
}
