package com.si_ware.neospectra.BluetoothSDK;

import com.polidea.rxandroidble2.RxBleConnection;
import p005io.reactivex.functions.Function;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$2 implements Function {
    static final Function $instance = new SWS_P3ConnectionServices$$Lambda$2();

    private SWS_P3ConnectionServices$$Lambda$2() {
    }

    public Object apply(Object obj) {
        return ((RxBleConnection) obj).discoverServices();
    }
}
