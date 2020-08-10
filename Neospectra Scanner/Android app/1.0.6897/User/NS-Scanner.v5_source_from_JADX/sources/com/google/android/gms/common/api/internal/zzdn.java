package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class zzdn<A extends zzb, L> {
    private final zzck<L> zzful;

    protected zzdn(zzck<L> zzck) {
        this.zzful = zzck;
    }

    public final zzck<L> zzajo() {
        return this.zzful;
    }

    /* access modifiers changed from: protected */
    public abstract void zzc(A a, TaskCompletionSource<Boolean> taskCompletionSource) throws RemoteException;
}
