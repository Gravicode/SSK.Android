package com.google.android.gms.internal;

final class zzcjd implements Runnable {
    private /* synthetic */ String zzimf;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcha zzjgs;

    zzcjd(zzcir zzcir, zzcha zzcha, String str) {
        this.zzjgo = zzcir;
        this.zzjgs = zzcha;
        this.zzimf = str;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzb(this.zzjgs, this.zzimf);
    }
}
