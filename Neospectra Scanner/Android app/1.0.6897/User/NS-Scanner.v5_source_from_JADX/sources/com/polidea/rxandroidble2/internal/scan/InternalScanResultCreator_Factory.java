package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.util.UUIDUtil;

public final class InternalScanResultCreator_Factory implements Factory<InternalScanResultCreator> {
    private final Provider<UUIDUtil> uuidUtilProvider;

    public InternalScanResultCreator_Factory(Provider<UUIDUtil> uuidUtilProvider2) {
        this.uuidUtilProvider = uuidUtilProvider2;
    }

    public InternalScanResultCreator get() {
        return new InternalScanResultCreator((UUIDUtil) this.uuidUtilProvider.get());
    }

    public static InternalScanResultCreator_Factory create(Provider<UUIDUtil> uuidUtilProvider2) {
        return new InternalScanResultCreator_Factory(uuidUtilProvider2);
    }
}
