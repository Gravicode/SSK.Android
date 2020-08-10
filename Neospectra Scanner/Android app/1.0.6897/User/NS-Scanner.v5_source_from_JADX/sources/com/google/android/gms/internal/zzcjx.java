package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcjx implements Runnable {
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ AtomicReference zzjhe;
    private /* synthetic */ boolean zzjhf;

    zzcjx(zzcjn zzcjn, AtomicReference atomicReference, boolean z) {
        this.zzjhc = zzcjn;
        this.zzjhe = atomicReference;
        this.zzjhf = z;
    }

    public final void run() {
        this.zzjhc.zzawp().zza(this.zzjhe, this.zzjhf);
    }
}
