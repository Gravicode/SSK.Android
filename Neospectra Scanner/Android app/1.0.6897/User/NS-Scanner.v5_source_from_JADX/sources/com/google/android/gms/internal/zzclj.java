package com.google.android.gms.internal;

final class zzclj implements Runnable {
    private /* synthetic */ long zziwu;
    private /* synthetic */ zzclf zzjjf;

    zzclj(zzclf zzclf, long j) {
        this.zzjjf = zzclf;
        this.zziwu = j;
    }

    public final void run() {
        this.zzjjf.zzbf(this.zziwu);
    }
}
