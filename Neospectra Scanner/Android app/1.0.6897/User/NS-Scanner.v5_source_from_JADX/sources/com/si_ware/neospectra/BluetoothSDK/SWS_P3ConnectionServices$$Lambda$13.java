package com.si_ware.neospectra.BluetoothSDK;

import com.polidea.rxandroidble2.RxBleConnection;
import p005io.reactivex.functions.Function;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$13 implements Function {
    private final byte[] arg$1;

    SWS_P3ConnectionServices$$Lambda$13(byte[] bArr) {
        this.arg$1 = bArr;
    }

    public Object apply(Object obj) {
        return ((RxBleConnection) obj).writeCharacteristic(SWS_P3ConnectionServices.MEM_RX_CHAR_UUID, this.arg$1);
    }
}
