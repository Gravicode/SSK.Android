package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;

public final class zzm implements zzj {
    private /* synthetic */ zzd zzfza;

    public zzm(zzd zzd) {
        this.zzfza = zzd;
    }

    public final void zzf(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.isSuccess()) {
            this.zzfza.zza((zzan) null, this.zzfza.zzakp());
            return;
        }
        if (this.zzfza.zzfys != null) {
            this.zzfza.zzfys.onConnectionFailed(connectionResult);
        }
    }
}
