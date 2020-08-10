package com.google.android.gms.internal;

final class zzcjt implements Runnable {
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ long zzjhg;

    zzcjt(zzcjn zzcjn, long j) {
        this.zzjhc = zzcjn;
        this.zzjhg = j;
    }

    public final void run() {
        this.zzjhc.zzawz().zzjde.set(this.zzjhg);
        this.zzjhc.zzawy().zzazi().zzj("Minimum session duration set", Long.valueOf(this.zzjhg));
    }
}
