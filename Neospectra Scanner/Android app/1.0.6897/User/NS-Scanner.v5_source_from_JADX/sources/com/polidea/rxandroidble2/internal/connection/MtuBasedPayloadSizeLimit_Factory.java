package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleConnection;

public final class MtuBasedPayloadSizeLimit_Factory implements Factory<MtuBasedPayloadSizeLimit> {
    private final Provider<Integer> gattWriteMtuOverheadProvider;
    private final Provider<RxBleConnection> rxBleConnectionProvider;

    public MtuBasedPayloadSizeLimit_Factory(Provider<RxBleConnection> rxBleConnectionProvider2, Provider<Integer> gattWriteMtuOverheadProvider2) {
        this.rxBleConnectionProvider = rxBleConnectionProvider2;
        this.gattWriteMtuOverheadProvider = gattWriteMtuOverheadProvider2;
    }

    public MtuBasedPayloadSizeLimit get() {
        return new MtuBasedPayloadSizeLimit((RxBleConnection) this.rxBleConnectionProvider.get(), ((Integer) this.gattWriteMtuOverheadProvider.get()).intValue());
    }

    public static MtuBasedPayloadSizeLimit_Factory create(Provider<RxBleConnection> rxBleConnectionProvider2, Provider<Integer> gattWriteMtuOverheadProvider2) {
        return new MtuBasedPayloadSizeLimit_Factory(rxBleConnectionProvider2, gattWriteMtuOverheadProvider2);
    }

    public static MtuBasedPayloadSizeLimit newMtuBasedPayloadSizeLimit(RxBleConnection rxBleConnection, int gattWriteMtuOverhead) {
        return new MtuBasedPayloadSizeLimit(rxBleConnection, gattWriteMtuOverhead);
    }
}
