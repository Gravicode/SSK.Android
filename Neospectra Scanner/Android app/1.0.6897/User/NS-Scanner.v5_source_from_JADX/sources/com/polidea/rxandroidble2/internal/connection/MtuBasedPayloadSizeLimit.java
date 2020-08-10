package com.polidea.rxandroidble2.internal.connection;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleConnection;

@RestrictTo({Scope.LIBRARY_GROUP})
@ConnectionScope
class MtuBasedPayloadSizeLimit implements PayloadSizeLimitProvider {
    private final int gattWriteMtuOverhead;
    private final RxBleConnection rxBleConnection;

    @Inject
    MtuBasedPayloadSizeLimit(RxBleConnection rxBleConnection2, @Named("GATT_WRITE_MTU_OVERHEAD") int gattWriteMtuOverhead2) {
        this.rxBleConnection = rxBleConnection2;
        this.gattWriteMtuOverhead = gattWriteMtuOverhead2;
    }

    public int getPayloadSizeLimit() {
        return this.rxBleConnection.getMtu() - this.gattWriteMtuOverhead;
    }
}
