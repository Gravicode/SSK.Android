package com.google.android.gms.internal;

final class zzchn implements Runnable {
    private /* synthetic */ String zzjcd;
    private /* synthetic */ zzchm zzjce;

    zzchn(zzchm zzchm, String str) {
        this.zzjce = zzchm;
        this.zzjcd = str;
    }

    public final void run() {
        zzchx zzawz = this.zzjce.zziwf.zzawz();
        if (!zzawz.isInitialized()) {
            this.zzjce.zzk(6, "Persisted config not initialized. Not logging error/warn");
        } else {
            zzawz.zzjcq.zzf(this.zzjcd, 1);
        }
    }
}
