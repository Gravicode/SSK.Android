package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zzd extends zzb<Void> {
    private zzcq<zzb, ?> zzfnq;
    private zzdn<zzb, ?> zzfnr;

    public zzd(zzcr zzcr, TaskCompletionSource<Void> taskCompletionSource) {
        super(3, taskCompletionSource);
        this.zzfnq = zzcr.zzfnq;
        this.zzfnr = zzcr.zzfnr;
    }

    public final /* bridge */ /* synthetic */ void zza(@NonNull zzae zzae, boolean z) {
    }

    public final void zzb(zzbo<?> zzbo) throws RemoteException {
        this.zzfnq.zzb(zzbo.zzahp(), this.zzedx);
        if (this.zzfnq.zzajo() != null) {
            zzbo.zzaiy().put(this.zzfnq.zzajo(), new zzcr(this.zzfnq, this.zzfnr));
        }
    }

    public final /* bridge */ /* synthetic */ void zzs(@NonNull Status status) {
        super.zzs(status);
    }
}
