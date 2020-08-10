package com.google.android.gms.internal;

final class zzcli implements Runnable {
    private /* synthetic */ long zziwu;
    private /* synthetic */ zzclf zzjjf;

    zzcli(zzclf zzclf, long j) {
        this.zzjjf = zzclf;
        this.zziwu = j;
    }

    public final void run() {
        this.zzjjf.zzbe(this.zziwu);
    }
}
