package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcjs implements Runnable {
    private /* synthetic */ String zzimf;
    private /* synthetic */ String zzjgq;
    private /* synthetic */ String zzjgr;
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ AtomicReference zzjhe;
    private /* synthetic */ boolean zzjhf;

    zzcjs(zzcjn zzcjn, AtomicReference atomicReference, String str, String str2, String str3, boolean z) {
        this.zzjhc = zzcjn;
        this.zzjhe = atomicReference;
        this.zzimf = str;
        this.zzjgq = str2;
        this.zzjgr = str3;
        this.zzjhf = z;
    }

    public final void run() {
        this.zzjhc.zziwf.zzawp().zza(this.zzjhe, this.zzimf, this.zzjgq, this.zzjgr, this.zzjhf);
    }
}
