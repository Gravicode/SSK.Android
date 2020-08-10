package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzb<T> extends zza {
    protected final TaskCompletionSource<T> zzedx;

    public zzb(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zzedx = taskCompletionSource;
    }

    public void zza(@NonNull zzae zzae, boolean z) {
    }

    public final void zza(zzbo<?> zzbo) throws DeadObjectException {
        try {
            zzb(zzbo);
        } catch (DeadObjectException e) {
            zzs(zza.zza((RemoteException) e));
            throw e;
        } catch (RemoteException e2) {
            zzs(zza.zza(e2));
        }
    }

    /* access modifiers changed from: protected */
    public abstract void zzb(zzbo<?> zzbo) throws RemoteException;

    public void zzs(@NonNull Status status) {
        this.zzedx.trySetException(new ApiException(status));
    }
}
