package com.google.android.gms.internal;

final class zzcju implements Runnable {
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ long zzjhg;

    zzcju(zzcjn zzcjn, long j) {
        this.zzjhc = zzcjn;
        this.zzjhg = j;
    }

    public final void run() {
        this.zzjhc.zzawz().zzjdf.set(this.zzjhg);
        this.zzjhc.zzawy().zzazi().zzj("Session timeout duration set", Long.valueOf(this.zzjhg));
    }
}
