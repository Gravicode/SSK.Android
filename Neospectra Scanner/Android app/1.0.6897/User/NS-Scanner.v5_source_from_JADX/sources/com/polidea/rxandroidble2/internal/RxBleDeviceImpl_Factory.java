package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.internal.connection.Connector;

public final class RxBleDeviceImpl_Factory implements Factory<RxBleDeviceImpl> {
    private final Provider<BluetoothDevice> bluetoothDeviceProvider;
    private final Provider<BehaviorRelay<RxBleConnectionState>> connectionStateRelayProvider;
    private final Provider<Connector> connectorProvider;

    public RxBleDeviceImpl_Factory(Provider<BluetoothDevice> bluetoothDeviceProvider2, Provider<Connector> connectorProvider2, Provider<BehaviorRelay<RxBleConnectionState>> connectionStateRelayProvider2) {
        this.bluetoothDeviceProvider = bluetoothDeviceProvider2;
        this.connectorProvider = connectorProvider2;
        this.connectionStateRelayProvider = connectionStateRelayProvider2;
    }

    public RxBleDeviceImpl get() {
        return new RxBleDeviceImpl((BluetoothDevice) this.bluetoothDeviceProvider.get(), (Connector) this.connectorProvider.get(), (BehaviorRelay) this.connectionStateRelayProvider.get());
    }

    public static RxBleDeviceImpl_Factory create(Provider<BluetoothDevice> bluetoothDeviceProvider2, Provider<Connector> connectorProvider2, Provider<BehaviorRelay<RxBleConnectionState>> connectionStateRelayProvider2) {
        return new RxBleDeviceImpl_Factory(bluetoothDeviceProvider2, connectorProvider2, connectionStateRelayProvider2);
    }

    public static RxBleDeviceImpl newRxBleDeviceImpl(BluetoothDevice bluetoothDevice, Connector connector, BehaviorRelay<RxBleConnectionState> connectionStateRelay) {
        return new RxBleDeviceImpl(bluetoothDevice, connector, connectionStateRelay);
    }
}
