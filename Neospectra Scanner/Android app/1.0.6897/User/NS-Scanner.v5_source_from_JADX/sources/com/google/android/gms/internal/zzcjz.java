package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcjz implements Runnable {
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ AtomicReference zzjhe;

    zzcjz(zzcjn zzcjn, AtomicReference atomicReference) {
        this.zzjhc = zzcjn;
        this.zzjhe = atomicReference;
    }

    public final void run() {
        this.zzjhc.zzawp().zza(this.zzjhe);
    }
}
