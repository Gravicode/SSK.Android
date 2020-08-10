package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzdd;
import com.google.android.gms.common.api.internal.zzde;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzawv extends zzdd<zzawr, Void> {
    private TaskCompletionSource<Void> zzedx;

    private zzawv() {
    }

    /* synthetic */ zzawv(zzawt zzawt) {
        this();
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzawr zzawr = (zzawr) zzb;
        this.zzedx = taskCompletionSource;
        zza((zzawn) zzawr.zzakn());
    }

    /* access modifiers changed from: protected */
    public abstract void zza(zzawn zzawn) throws RemoteException;

    /* access modifiers changed from: protected */
    public final void zzh(Status status) {
        zzde.zza(status, null, this.zzedx);
    }
}
