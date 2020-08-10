package com.google.android.gms.internal;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

final class zzclg extends zzcgs {
    private /* synthetic */ zzclf zzjjf;

    zzclg(zzclf zzclf, zzcim zzcim) {
        this.zzjjf = zzclf;
        super(zzcim);
    }

    @WorkerThread
    public final void run() {
        zzclf zzclf = this.zzjjf;
        zzclf.zzve();
        zzclf.zzawy().zzazj().zzj("Session started, time", Long.valueOf(zzclf.zzws().elapsedRealtime()));
        zzclf.zzawz().zzjdg.set(false);
        zzclf.zzawm().zzc("auto", "_s", new Bundle());
        zzclf.zzawz().zzjdh.set(zzclf.zzws().currentTimeMillis());
    }
}
