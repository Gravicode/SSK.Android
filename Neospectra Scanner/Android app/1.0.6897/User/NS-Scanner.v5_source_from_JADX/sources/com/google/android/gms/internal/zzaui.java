package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.auth.account.WorkAccountApi.AddAccountResult;
import com.google.android.gms.auth.account.zza;
import com.google.android.gms.auth.account.zzc;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

final class zzaui extends zzm<AddAccountResult, zzauq> {
    private /* synthetic */ String zzecf;

    zzaui(zzaug zzaug, Api api, GoogleApiClient googleApiClient, String str) {
        this.zzecf = str;
        super(api, googleApiClient);
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((AddAccountResult) obj);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        ((zzc) ((zzauq) zzb).zzakn()).zza((zza) new zzauj(this), this.zzecf);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return new zzaun(status, null);
    }
}
