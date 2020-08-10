package com.google.android.gms.internal;

final class zzk implements Runnable {
    private final zzr zzx;
    private final zzw zzy;
    private final Runnable zzz;

    public zzk(zzi zzi, zzr zzr, zzw zzw, Runnable runnable) {
        this.zzx = zzr;
        this.zzy = zzw;
        this.zzz = runnable;
    }

    public final void run() {
        if (this.zzy.zzbi == null) {
            this.zzx.zza(this.zzy.result);
        } else {
            this.zzx.zzb(this.zzy.zzbi);
        }
        if (this.zzy.zzbj) {
            this.zzx.zzb("intermediate-response");
        } else {
            this.zzx.zzc("done");
        }
        if (this.zzz != null) {
            this.zzz.run();
        }
    }
}
