package com.google.android.gms.internal;

final class zzcji implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;

    zzcji(zzcir zzcir, zzcgi zzcgi) {
        this.zzjgo = zzcir;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzf(this.zzjgn);
    }
}
