package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;

final class zzas extends zzbj {
    private /* synthetic */ ConnectionResult zzfro;
    private /* synthetic */ zzar zzfrp;

    zzas(zzar zzar, zzbh zzbh, ConnectionResult connectionResult) {
        this.zzfrp = zzar;
        this.zzfro = connectionResult;
        super(zzbh);
    }

    public final void zzaib() {
        this.zzfrp.zzfrl.zze(this.zzfro);
    }
}
