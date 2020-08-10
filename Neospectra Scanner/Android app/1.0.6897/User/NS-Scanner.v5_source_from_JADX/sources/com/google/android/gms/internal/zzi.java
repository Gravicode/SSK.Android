package com.google.android.gms.internal;

import android.os.Handler;
import java.util.concurrent.Executor;

public final class zzi implements zzz {
    private final Executor zzv;

    public zzi(Handler handler) {
        this.zzv = new zzj(this, handler);
    }

    public final void zza(zzr<?> zzr, zzad zzad) {
        zzr.zzb("post-error");
        this.zzv.execute(new zzk(this, zzr, zzw.zzc(zzad), null));
    }

    public final void zza(zzr<?> zzr, zzw<?> zzw, Runnable runnable) {
        zzr.zzj();
        zzr.zzb("post-response");
        this.zzv.execute(new zzk(this, zzr, zzw, runnable));
    }

    public final void zzb(zzr<?> zzr, zzw<?> zzw) {
        zza(zzr, zzw, null);
    }
}
