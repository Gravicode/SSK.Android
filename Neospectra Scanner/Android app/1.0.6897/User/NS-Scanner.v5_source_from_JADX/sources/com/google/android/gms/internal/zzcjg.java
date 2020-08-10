package com.google.android.gms.internal;

final class zzcjg implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcln zzjgt;

    zzcjg(zzcir zzcir, zzcln zzcln, zzcgi zzcgi) {
        this.zzjgo = zzcir;
        this.zzjgt = zzcln;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzb(this.zzjgt, this.zzjgn);
    }
}
