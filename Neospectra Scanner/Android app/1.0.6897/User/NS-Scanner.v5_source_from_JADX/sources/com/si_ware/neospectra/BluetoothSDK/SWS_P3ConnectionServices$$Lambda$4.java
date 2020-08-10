package com.si_ware.neospectra.BluetoothSDK;

import com.polidea.rxandroidble2.RxBleDeviceServices;
import p005io.reactivex.functions.Consumer;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$4 implements Consumer {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$4(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$ConnectToP3$3$SWS_P3ConnectionServices((RxBleDeviceServices) obj);
    }
}
