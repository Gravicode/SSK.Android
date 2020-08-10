package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.auth.account.zzc;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

final class zzauh extends zzm<Result, zzauq> {
    private /* synthetic */ boolean zzecs;

    zzauh(zzaug zzaug, Api api, GoogleApiClient googleApiClient, boolean z) {
        this.zzecs = z;
        super(api, googleApiClient);
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((Result) obj);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        ((zzc) ((zzauq) zzb).zzakn()).zzaq(this.zzecs);
        setResult(new zzauo(Status.zzfni));
    }

    /* access modifiers changed from: protected */
    public final Result zzb(Status status) {
        return new zzauo(status);
    }
}
