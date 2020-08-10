package com.google.android.gms.common.api.internal;

import android.app.Dialog;

final class zzr extends zzby {
    private /* synthetic */ Dialog zzfor;
    private /* synthetic */ zzq zzfos;

    zzr(zzq zzq, Dialog dialog) {
        this.zzfos = zzq;
        this.zzfor = dialog;
    }

    public final void zzahg() {
        this.zzfos.zzfoq.zzahd();
        if (this.zzfor.isShowing()) {
            this.zzfor.dismiss();
        }
    }
}
