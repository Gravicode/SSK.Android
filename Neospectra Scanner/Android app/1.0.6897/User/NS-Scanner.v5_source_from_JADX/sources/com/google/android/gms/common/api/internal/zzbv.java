package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import java.util.Collections;

final class zzbv implements Runnable {
    private /* synthetic */ ConnectionResult zzfts;
    private /* synthetic */ zzbu zzftv;

    zzbv(zzbu zzbu, ConnectionResult connectionResult) {
        this.zzftv = zzbu;
        this.zzfts = connectionResult;
    }

    public final void run() {
        if (this.zzfts.isSuccess()) {
            this.zzftv.zzftu = true;
            if (this.zzftv.zzfpv.zzaay()) {
                this.zzftv.zzajg();
            } else {
                this.zzftv.zzfpv.zza(null, Collections.emptySet());
            }
        } else {
            ((zzbo) this.zzftv.zzfti.zzfpy.get(this.zzftv.zzfmf)).onConnectionFailed(this.zzfts);
        }
    }
}
