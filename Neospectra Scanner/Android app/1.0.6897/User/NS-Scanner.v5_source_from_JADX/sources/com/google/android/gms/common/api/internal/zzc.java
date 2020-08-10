package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

public final class zzc<A extends zzm<? extends Result, zzb>> extends zza {
    private A zzfnp;

    public zzc(int i, A a) {
        super(i);
        this.zzfnp = a;
    }

    public final void zza(@NonNull zzae zzae, boolean z) {
        zzae.zza((BasePendingResult<? extends Result>) this.zzfnp, z);
    }

    public final void zza(zzbo<?> zzbo) throws DeadObjectException {
        this.zzfnp.zzb(zzbo.zzahp());
    }

    public final void zzs(@NonNull Status status) {
        this.zzfnp.zzu(status);
    }
}
