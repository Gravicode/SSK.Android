package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class zzcq<A extends zzb, L> {
    private final zzci<L> zzfus;

    protected zzcq(zzci<L> zzci) {
        this.zzfus = zzci;
    }

    public final zzck<L> zzajo() {
        return this.zzfus.zzajo();
    }

    public final void zzajp() {
        this.zzfus.clear();
    }

    /* access modifiers changed from: protected */
    public abstract void zzb(A a, TaskCompletionSource<Void> taskCompletionSource) throws RemoteException;
}
