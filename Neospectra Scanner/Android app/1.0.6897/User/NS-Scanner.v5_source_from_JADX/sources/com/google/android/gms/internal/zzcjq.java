package com.google.android.gms.internal;

import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;

final class zzcjq implements Runnable {
    private /* synthetic */ zzcjn zzjhc;
    private /* synthetic */ ConditionalUserProperty zzjhd;

    zzcjq(zzcjn zzcjn, ConditionalUserProperty conditionalUserProperty) {
        this.zzjhc = zzcjn;
        this.zzjhd = conditionalUserProperty;
    }

    public final void run() {
        this.zzjhc.zzc(this.zzjhd);
    }
}
