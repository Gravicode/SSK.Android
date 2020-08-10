package com.si_ware.neospectra.BluetoothSDK;

import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Consumer;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$3 implements Consumer {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$3(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void accept(Object obj) {
        this.arg$1.lambda$ConnectToP3$2$SWS_P3ConnectionServices((Disposable) obj);
    }
}
