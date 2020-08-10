package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zzf extends zzb<Boolean> {
    private zzck<?> zzfnu;

    public zzf(zzck<?> zzck, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzfnu = zzck;
    }

    public final /* bridge */ /* synthetic */ void zza(@NonNull zzae zzae, boolean z) {
    }

    public final void zzb(zzbo<?> zzbo) throws RemoteException {
        zzcr zzcr = (zzcr) zzbo.zzaiy().remove(this.zzfnu);
        if (zzcr != null) {
            zzcr.zzfnr.zzc(zzbo.zzahp(), this.zzedx);
            zzcr.zzfnq.zzajp();
            return;
        }
        this.zzedx.trySetResult(Boolean.valueOf(false));
    }

    public final /* bridge */ /* synthetic */ void zzs(@NonNull Status status) {
        super.zzs(status);
    }
}
