package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;

public interface ConnectionStateChangeListener {
    void onConnectionStateChange(RxBleConnectionState rxBleConnectionState);
}
