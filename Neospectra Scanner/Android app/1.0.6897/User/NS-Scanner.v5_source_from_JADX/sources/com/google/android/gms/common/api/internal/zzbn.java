package com.google.android.gms.common.api.internal;

final class zzbn implements zzl {
    private /* synthetic */ zzbm zzfti;

    zzbn(zzbm zzbm) {
        this.zzfti = zzbm;
    }

    public final void zzbf(boolean z) {
        this.zzfti.mHandler.sendMessage(this.zzfti.mHandler.obtainMessage(1, Boolean.valueOf(z)));
    }
}
