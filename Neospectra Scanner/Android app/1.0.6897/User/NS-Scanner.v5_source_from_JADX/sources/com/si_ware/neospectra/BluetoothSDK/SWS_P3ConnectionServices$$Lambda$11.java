package com.si_ware.neospectra.BluetoothSDK;

import p005io.reactivex.functions.Consumer;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$11 implements Consumer {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$11(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$WriteToP3$6$SWS_P3ConnectionServices((byte[]) obj);
    }
}
