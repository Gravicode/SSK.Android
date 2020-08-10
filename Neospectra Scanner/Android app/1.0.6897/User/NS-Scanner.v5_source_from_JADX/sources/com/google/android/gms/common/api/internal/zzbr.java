package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;

final class zzbr implements Runnable {
    private /* synthetic */ zzbo zzftr;
    private /* synthetic */ ConnectionResult zzfts;

    zzbr(zzbo zzbo, ConnectionResult connectionResult) {
        this.zzftr = zzbo;
        this.zzfts = connectionResult;
    }

    public final void run() {
        this.zzftr.onConnectionFailed(this.zzfts);
    }
}
