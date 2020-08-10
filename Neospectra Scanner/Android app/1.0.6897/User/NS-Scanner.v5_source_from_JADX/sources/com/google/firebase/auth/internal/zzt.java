package com.google.firebase.auth.internal;

import com.google.android.gms.common.api.internal.zzl;

final class zzt implements zzl {
    private /* synthetic */ zzs zzmid;

    zzt(zzs zzs) {
        this.zzmid = zzs;
    }

    public final void zzbf(boolean z) {
        if (z) {
            this.zzmid.zzmic = true;
            this.zzmid.cancel();
            return;
        }
        this.zzmid.zzmic = false;
        if (this.zzmid.zzbsi()) {
            this.zzmid.zzmib.zzbsd();
        }
    }
}
