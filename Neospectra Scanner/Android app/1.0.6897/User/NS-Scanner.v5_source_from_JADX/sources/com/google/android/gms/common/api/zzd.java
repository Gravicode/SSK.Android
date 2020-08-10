package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApi.zza;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzg;
import com.google.android.gms.common.internal.zzbq;

public final class zzd {
    private Looper zzall;
    private zzcz zzfmh;

    public final zzd zza(Looper looper) {
        zzbq.checkNotNull(looper, "Looper must not be null.");
        this.zzall = looper;
        return this;
    }

    public final zzd zza(zzcz zzcz) {
        zzbq.checkNotNull(zzcz, "StatusExceptionMapper must not be null.");
        this.zzfmh = zzcz;
        return this;
    }

    public final zza zzagq() {
        if (this.zzfmh == null) {
            this.zzfmh = new zzg();
        }
        if (this.zzall == null) {
            this.zzall = Looper.getMainLooper();
        }
        return new zza(this.zzfmh, this.zzall);
    }
}
