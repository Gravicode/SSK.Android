package com.google.android.gms.common.api.internal;

import com.google.android.gms.internal.zzcxq;

final class zzcx implements Runnable {
    private /* synthetic */ zzcxq zzfrt;
    private /* synthetic */ zzcv zzfuv;

    zzcx(zzcv zzcv, zzcxq zzcxq) {
        this.zzfuv = zzcv;
        this.zzfrt = zzcxq;
    }

    public final void run() {
        this.zzfuv.zzc(this.zzfrt);
    }
}
