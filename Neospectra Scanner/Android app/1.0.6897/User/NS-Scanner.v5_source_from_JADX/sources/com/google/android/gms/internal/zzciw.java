package com.google.android.gms.internal;

final class zzciw implements Runnable {
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcgl zzjgp;

    zzciw(zzcir zzcir, zzcgl zzcgl) {
        this.zzjgo = zzcir;
        this.zzjgp = zzcgl;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzd(this.zzjgp);
    }
}
