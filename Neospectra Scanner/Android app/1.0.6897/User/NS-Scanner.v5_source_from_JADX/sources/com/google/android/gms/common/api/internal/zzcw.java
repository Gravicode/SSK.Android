package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;

final class zzcw implements Runnable {
    private /* synthetic */ zzcv zzfuv;

    zzcw(zzcv zzcv) {
        this.zzfuv = zzcv;
    }

    public final void run() {
        this.zzfuv.zzfuu.zzh(new ConnectionResult(4));
    }
}
