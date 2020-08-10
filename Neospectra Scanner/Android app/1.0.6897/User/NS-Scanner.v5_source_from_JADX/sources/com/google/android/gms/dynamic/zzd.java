package com.google.android.gms.dynamic;

import android.os.Bundle;

final class zzd implements zzi {
    private /* synthetic */ Bundle zzail;
    private /* synthetic */ zza zzgwh;

    zzd(zza zza, Bundle bundle) {
        this.zzgwh = zza;
        this.zzail = bundle;
    }

    public final int getState() {
        return 1;
    }

    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzgwh.zzgwd.onCreate(this.zzail);
    }
}
