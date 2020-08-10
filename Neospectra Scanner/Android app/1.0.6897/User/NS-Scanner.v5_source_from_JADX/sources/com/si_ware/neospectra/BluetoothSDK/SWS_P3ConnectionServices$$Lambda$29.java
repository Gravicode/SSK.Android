package com.si_ware.neospectra.BluetoothSDK;

import com.polidea.rxandroidble2.RxBleConnection;
import p005io.reactivex.functions.Function;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$29 implements Function {
    static final Function $instance = new SWS_P3ConnectionServices$$Lambda$29();

    private SWS_P3ConnectionServices$$Lambda$29() {
    }

    public Object apply(Object obj) {
        return ((RxBleConnection) obj).setupNotification(SWS_P3ConnectionServices.MEM_TX_CHAR_UUID);
    }
}
