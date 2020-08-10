package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.Binds;
import bleshadow.dagger.Module;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.internal.connection.Connector;
import com.polidea.rxandroidble2.internal.connection.ConnectorImpl;

@Module
abstract class DeviceModuleBinder {
    /* access modifiers changed from: 0000 */
    @Binds
    public abstract Connector bindConnector(ConnectorImpl connectorImpl);

    /* access modifiers changed from: 0000 */
    @Binds
    public abstract RxBleDevice bindDevice(RxBleDeviceImpl rxBleDeviceImpl);

    DeviceModuleBinder() {
    }
}
