package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement.zzb;

final class zzcke implements Runnable {
    private /* synthetic */ zzckc zzjhz;
    private /* synthetic */ zzckf zzjia;

    zzcke(zzckc zzckc, zzckf zzckf) {
        this.zzjhz = zzckc;
        this.zzjia = zzckf;
    }

    public final void run() {
        this.zzjhz.zza(this.zzjia);
        this.zzjhz.zzjhn = null;
        this.zzjhz.zzawp().zza((zzb) null);
    }
}
