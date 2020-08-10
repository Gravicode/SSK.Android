package com.google.android.gms.internal;

import android.content.Intent;

final class zzclm extends zzcgs {
    private /* synthetic */ zzcll zzjjh;

    zzclm(zzcll zzcll, zzcim zzcim) {
        this.zzjjh = zzcll;
        super(zzcim);
    }

    public final void run() {
        this.zzjjh.cancel();
        this.zzjjh.zzawy().zzazj().log("Sending upload intent from DelayedRunnable");
        Intent className = new Intent().setClassName(this.zzjjh.getContext(), "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        this.zzjjh.getContext().sendBroadcast(className);
    }
}
