package com.google.android.gms.internal;

final class zzcgf implements Runnable {
    private /* synthetic */ String zzbfa;
    private /* synthetic */ long zziwu;
    private /* synthetic */ zzcgd zziwv;

    zzcgf(zzcgd zzcgd, String str, long j) {
        this.zziwv = zzcgd;
        this.zzbfa = str;
        this.zziwu = j;
    }

    public final void run() {
        this.zziwv.zze(this.zzbfa, this.zziwu);
    }
}
