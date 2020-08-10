package com.google.android.gms.internal;

import android.content.Intent;

final /* synthetic */ class zzclb implements Runnable {
    private final zzcla zzjiv;
    private final int zzjiw;
    private final zzchm zzjix;
    private final Intent zzjiy;

    zzclb(zzcla zzcla, int i, zzchm zzchm, Intent intent) {
        this.zzjiv = zzcla;
        this.zzjiw = i;
        this.zzjix = zzchm;
        this.zzjiy = intent;
    }

    public final void run() {
        this.zzjiv.zza(this.zzjiw, this.zzjix, this.zzjiy);
    }
}
