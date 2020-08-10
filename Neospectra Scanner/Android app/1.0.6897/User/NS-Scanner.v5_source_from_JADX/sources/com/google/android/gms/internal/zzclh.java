package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;

final class zzclh extends zzcgs {
    private /* synthetic */ zzclf zzjjf;

    zzclh(zzclf zzclf, zzcim zzcim) {
        this.zzjjf = zzclf;
        super(zzcim);
    }

    @WorkerThread
    public final void run() {
        this.zzjjf.zzbaw();
    }
}
