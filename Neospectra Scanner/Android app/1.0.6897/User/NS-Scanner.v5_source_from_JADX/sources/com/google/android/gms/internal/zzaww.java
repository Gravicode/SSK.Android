package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.zzd;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

abstract class zzaww extends zzm<ProxyResult, zzawi> {
    public zzaww(GoogleApiClient googleApiClient) {
        super(zzd.API, googleApiClient);
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((ProxyResult) obj);
    }

    /* access modifiers changed from: protected */
    public abstract void zza(Context context, zzawl zzawl) throws RemoteException;

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        zzawi zzawi = (zzawi) zzb;
        zza(zzawi.getContext(), (zzawl) zzawi.zzakn());
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return new zzaxa(status);
    }
}
