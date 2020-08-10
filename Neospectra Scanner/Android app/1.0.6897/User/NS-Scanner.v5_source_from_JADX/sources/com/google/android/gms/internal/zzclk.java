package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;

final class zzclk {
    private long mStartTime;
    private final zzd zzata;

    public zzclk(zzd zzd) {
        zzbq.checkNotNull(zzd);
        this.zzata = zzd;
    }

    public final void clear() {
        this.mStartTime = 0;
    }

    public final void start() {
        this.mStartTime = this.zzata.elapsedRealtime();
    }

    public final boolean zzu(long j) {
        return this.mStartTime == 0 || this.zzata.elapsedRealtime() - this.mStartTime >= 3600000;
    }
}
