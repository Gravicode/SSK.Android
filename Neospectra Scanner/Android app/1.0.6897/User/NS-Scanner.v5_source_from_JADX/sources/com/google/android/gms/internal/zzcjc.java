package com.google.android.gms.internal;

final class zzcjc implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcha zzjgs;

    zzcjc(zzcir zzcir, zzcha zzcha, zzcgi zzcgi) {
        this.zzjgo = zzcir;
        this.zzjgs = zzcha;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzb(this.zzjgs, this.zzjgn);
    }
}
