package com.google.android.gms.internal;

final class zzcjo implements Runnable {
    private /* synthetic */ boolean zzecs;
    private /* synthetic */ zzcjn zzjhc;

    zzcjo(zzcjn zzcjn, boolean z) {
        this.zzjhc = zzcjn;
        this.zzecs = z;
    }

    public final void run() {
        this.zzjhc.zzbp(this.zzecs);
    }
}
