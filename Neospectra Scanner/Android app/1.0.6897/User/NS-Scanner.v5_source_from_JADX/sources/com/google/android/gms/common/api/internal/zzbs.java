package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.internal.zzp;

final class zzbs implements zzp {
    final /* synthetic */ zzbo zzftr;

    zzbs(zzbo zzbo) {
        this.zzftr = zzbo;
    }

    public final void zzajf() {
        this.zzftr.zzfti.mHandler.post(new zzbt(this));
    }
}
