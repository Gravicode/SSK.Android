package com.google.android.gms.internal;

final class zzciu implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ zzcgl zzjgp;

    zzciu(zzcir zzcir, zzcgl zzcgl, zzcgi zzcgi) {
        this.zzjgo = zzcir;
        this.zzjgp = zzcgl;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        this.zzjgo.zziwf.zzbal();
        this.zzjgo.zziwf.zzb(this.zzjgp, this.zzjgn);
    }
}
