package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.zzf;

final class zzap implements Runnable {
    private /* synthetic */ zzao zzfrl;

    zzap(zzao zzao) {
        this.zzfrl = zzao;
    }

    public final void run() {
        zzf.zzce(this.zzfrl.mContext);
    }
}
