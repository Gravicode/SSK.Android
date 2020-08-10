package com.si_ware.neospectra.BluetoothSDK;

import p005io.reactivex.Observable;
import p005io.reactivex.functions.Consumer;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$30 implements Consumer {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$30(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$SetNotificationOnMemTx$20$SWS_P3ConnectionServices((Observable) obj);
    }
}
