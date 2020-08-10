package com.si_ware.neospectra.BluetoothSDK;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final /* synthetic */ class SWS_P3ConnectionServices$$Lambda$43 implements OnClickListener {
    private final SWS_P3ConnectionServices arg$1;

    SWS_P3ConnectionServices$$Lambda$43(SWS_P3ConnectionServices sWS_P3ConnectionServices) {
        this.arg$1 = sWS_P3ConnectionServices;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.arg$1.lambda$askForLocationPermissions$31$SWS_P3ConnectionServices(dialogInterface, i);
    }
}
