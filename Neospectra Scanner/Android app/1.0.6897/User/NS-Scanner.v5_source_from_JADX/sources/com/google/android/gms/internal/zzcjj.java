package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement.zzb;

final class zzcjj implements Runnable {
    private /* synthetic */ String zzimf;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ String zzjgu;
    private /* synthetic */ String zzjgv;
    private /* synthetic */ long zzjgw;

    zzcjj(zzcir zzcir, String str, String str2, String str3, long j) {
        this.zzjgo = zzcir;
        this.zzjgu = str;
        this.zzimf = str2;
        this.zzjgv = str3;
        this.zzjgw = j;
    }

    public final void run() {
        if (this.zzjgu == null) {
            this.zzjgo.zziwf.zzawq().zza(this.zzimf, (zzb) null);
            return;
        }
        zzb zzb = new zzb();
        zzb.zziwk = this.zzjgv;
        zzb.zziwl = this.zzjgu;
        zzb.zziwm = this.zzjgw;
        this.zzjgo.zziwf.zzawq().zza(this.zzimf, zzb);
    }
}
