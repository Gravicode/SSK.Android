package com.google.android.gms.internal;

final class zzciv implements Runnable {
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcgl zzjgp;

    zzciv(zzcir zzcir, zzcgl zzcgl) {
        this.zzjgo = zzcir;
        this.zzjgp = zzcgl;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zze(this.zzjgp);
    }
}
