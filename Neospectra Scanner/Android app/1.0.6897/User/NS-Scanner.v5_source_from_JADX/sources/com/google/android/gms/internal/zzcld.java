package com.google.android.gms.internal;

final class zzcld implements Runnable {
    private /* synthetic */ zzcim zzjdt;
    private /* synthetic */ Runnable zzjjb;

    zzcld(zzcla zzcla, zzcim zzcim, Runnable runnable) {
        this.zzjdt = zzcim;
        this.zzjjb = runnable;
    }

    public final void run() {
        this.zzjdt.zzbal();
        this.zzjdt.zzi(this.zzjjb);
        this.zzjdt.zzbah();
    }
}
