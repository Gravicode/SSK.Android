package com.google.android.gms.dynamic;

import android.app.Activity;
import android.os.Bundle;

final class zzc implements zzi {
    private /* synthetic */ Activity val$activity;
    private /* synthetic */ Bundle zzail;
    private /* synthetic */ zza zzgwh;
    private /* synthetic */ Bundle zzgwi;

    zzc(zza zza, Activity activity, Bundle bundle, Bundle bundle2) {
        this.zzgwh = zza;
        this.val$activity = activity;
        this.zzgwi = bundle;
        this.zzail = bundle2;
    }

    public final int getState() {
        return 0;
    }

    public final void zzb(LifecycleDelegate lifecycleDelegate) {
        this.zzgwh.zzgwd.onInflate(this.val$activity, this.zzgwi, this.zzail);
    }
}
