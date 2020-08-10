package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement.zzb;

final class zzckd implements Runnable {
    private /* synthetic */ boolean zzjhw;
    private /* synthetic */ zzb zzjhx;
    private /* synthetic */ zzckf zzjhy;
    private /* synthetic */ zzckc zzjhz;

    zzckd(zzckc zzckc, boolean z, zzb zzb, zzckf zzckf) {
        this.zzjhz = zzckc;
        this.zzjhw = z;
        this.zzjhx = zzb;
        this.zzjhy = zzckf;
    }

    public final void run() {
        if (this.zzjhw && this.zzjhz.zzjhn != null) {
            this.zzjhz.zza(this.zzjhz.zzjhn);
        }
        if (this.zzjhx == null || this.zzjhx.zziwm != this.zzjhy.zziwm || !zzclq.zzas(this.zzjhx.zziwl, this.zzjhy.zziwl) || !zzclq.zzas(this.zzjhx.zziwk, this.zzjhy.zziwk)) {
            Bundle bundle = new Bundle();
            zzckc.zza((zzb) this.zzjhy, bundle);
            if (this.zzjhx != null) {
                if (this.zzjhx.zziwk != null) {
                    bundle.putString("_pn", this.zzjhx.zziwk);
                }
                bundle.putString("_pc", this.zzjhx.zziwl);
                bundle.putLong("_pi", this.zzjhx.zziwm);
            }
            this.zzjhz.zzawm().zzc("auto", "_vs", bundle);
        }
        this.zzjhz.zzjhn = this.zzjhy;
        this.zzjhz.zzawp().zza((zzb) this.zzjhy);
    }
}
