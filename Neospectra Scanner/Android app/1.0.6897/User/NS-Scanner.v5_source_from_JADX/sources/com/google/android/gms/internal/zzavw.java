package com.google.android.gms.internal;

import android.content.Context;
import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.zzm;

abstract class zzavw<R extends Result> extends zzm<R, zzavy> {
    zzavw(GoogleApiClient googleApiClient) {
        super(Auth.CREDENTIALS_API, googleApiClient);
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((Result) obj);
    }

    /* access modifiers changed from: protected */
    public abstract void zza(Context context, zzawd zzawd) throws DeadObjectException, RemoteException;

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        zzavy zzavy = (zzavy) zzb;
        zza(zzavy.getContext(), (zzawd) zzavy.zzakn());
    }
}
