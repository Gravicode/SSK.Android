package com.google.android.gms.internal;

final class zzchw implements Runnable {
    private /* synthetic */ boolean zzjcn;
    private /* synthetic */ zzchv zzjco;

    zzchw(zzchv zzchv, boolean z) {
        this.zzjco = zzchv;
        this.zzjcn = z;
    }

    public final void run() {
        this.zzjco.zziwf.zzbo(this.zzjcn);
    }
}
