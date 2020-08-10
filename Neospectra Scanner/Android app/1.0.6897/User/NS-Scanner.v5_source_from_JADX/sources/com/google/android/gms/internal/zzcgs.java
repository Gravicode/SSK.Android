package com.google.android.gms.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.zzbq;

abstract class zzcgs {
    private static volatile Handler zzdvp;
    /* access modifiers changed from: private */
    public volatile long zzdvq;
    /* access modifiers changed from: private */
    public final zzcim zziwf;
    /* access modifiers changed from: private */
    public boolean zzizd = true;
    private final Runnable zzz = new zzcgt(this);

    zzcgs(zzcim zzcim) {
        zzbq.checkNotNull(zzcim);
        this.zziwf = zzcim;
    }

    private final Handler getHandler() {
        Handler handler;
        if (zzdvp != null) {
            return zzdvp;
        }
        synchronized (zzcgs.class) {
            if (zzdvp == null) {
                zzdvp = new Handler(this.zziwf.getContext().getMainLooper());
            }
            handler = zzdvp;
        }
        return handler;
    }

    public final void cancel() {
        this.zzdvq = 0;
        getHandler().removeCallbacks(this.zzz);
    }

    public abstract void run();

    public final boolean zzdx() {
        return this.zzdvq != 0;
    }

    public final void zzs(long j) {
        cancel();
        if (j >= 0) {
            this.zzdvq = this.zziwf.zzws().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzz, j)) {
                this.zziwf.zzawy().zzazd().zzj("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }
}
