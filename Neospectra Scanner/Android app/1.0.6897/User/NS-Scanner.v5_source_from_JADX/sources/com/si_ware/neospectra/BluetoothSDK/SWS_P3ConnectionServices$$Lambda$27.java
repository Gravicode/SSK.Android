package com.si_ware.neospectra.BluetoothSDK;

import p005io.reactivex.functions.Consumer;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$27 implements Consumer {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$27(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$5$SWS_P3ConnectionServices((byte[]) obj);
    }
}
