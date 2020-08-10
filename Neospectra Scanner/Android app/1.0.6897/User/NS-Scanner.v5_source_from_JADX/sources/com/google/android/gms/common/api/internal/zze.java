package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zze<TResult> extends zza {
    private final TaskCompletionSource<TResult> zzedx;
    private final zzdd<zzb, TResult> zzfns;
    private final zzcz zzfnt;

    public zze(int i, zzdd<zzb, TResult> zzdd, TaskCompletionSource<TResult> taskCompletionSource, zzcz zzcz) {
        super(i);
        this.zzedx = taskCompletionSource;
        this.zzfns = zzdd;
        this.zzfnt = zzcz;
    }

    public final void zza(@NonNull zzae zzae, boolean z) {
        zzae.zza(this.zzedx, z);
    }

    public final void zza(zzbo<?> zzbo) throws DeadObjectException {
        try {
            this.zzfns.zza(zzbo.zzahp(), this.zzedx);
        } catch (DeadObjectException e) {
            throw e;
        } catch (RemoteException e2) {
            zzs(zza.zza(e2));
        }
    }

    public final void zzs(@NonNull Status status) {
        this.zzedx.trySetException(this.zzfnt.zzt(status));
    }
}
