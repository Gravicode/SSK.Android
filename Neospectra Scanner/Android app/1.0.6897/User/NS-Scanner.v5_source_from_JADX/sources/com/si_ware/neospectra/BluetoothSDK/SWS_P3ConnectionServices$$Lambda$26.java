package com.si_ware.neospectra.BluetoothSDK;

import p005io.reactivex.Observable;
import p005io.reactivex.functions.Function;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$26 implements Function {
    static final Function $instance = new SWS_P3ConnectionServices$$Lambda$26();

    private SWS_P3ConnectionServices$$Lambda$26() {
    }

    public Object apply(Object obj) {
        return ((Observable) obj).flatMap(SWS_P3ConnectionServices$$Lambda$51.$instance);
    }
}
