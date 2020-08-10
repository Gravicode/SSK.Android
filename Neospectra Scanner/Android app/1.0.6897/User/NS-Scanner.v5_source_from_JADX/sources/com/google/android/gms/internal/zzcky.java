package com.google.android.gms.internal;

import android.content.ComponentName;

final class zzcky implements Runnable {
    private /* synthetic */ zzcku zzjit;

    zzcky(zzcku zzcku) {
        this.zzjit = zzcku;
    }

    public final void run() {
        this.zzjit.zzjij.onServiceDisconnected(new ComponentName(this.zzjit.zzjij.getContext(), "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
