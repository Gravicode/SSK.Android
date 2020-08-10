package com.google.android.gms.internal;

final class zzcgg implements Runnable {
    private /* synthetic */ long zziwu;
    private /* synthetic */ zzcgd zziwv;

    zzcgg(zzcgd zzcgd, long j) {
        this.zziwv = zzcgd;
        this.zziwu = j;
    }

    public final void run() {
        this.zziwv.zzak(this.zziwu);
    }
}
