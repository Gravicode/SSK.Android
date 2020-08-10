package com.si_ware.neospectra.BluetoothSDK;

import com.polidea.rxandroidble2.RxBleConnection;
import p005io.reactivex.functions.Function;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$7 implements Function {
    static final Function $instance = new SWS_P3ConnectionServices$$Lambda$7();

    private SWS_P3ConnectionServices$$Lambda$7() {
    }

    public Object apply(Object obj) {
        return ((RxBleConnection) obj).readCharacteristic(SWS_P3ConnectionServices.P3_TX_CHAR_UUID);
    }
}
