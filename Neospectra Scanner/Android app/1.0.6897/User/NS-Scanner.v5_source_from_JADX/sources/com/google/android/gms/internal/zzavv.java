package com.google.android.gms.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzn;

final class zzavv extends zzavn {
    private zzn<Status> zzegg;

    zzavv(zzn<Status> zzn) {
        this.zzegg = zzn;
    }

    public final void zze(Status status) {
        this.zzegg.setResult(status);
    }
}
