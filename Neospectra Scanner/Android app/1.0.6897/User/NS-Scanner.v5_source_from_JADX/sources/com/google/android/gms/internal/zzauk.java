package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.RemoteException;
import com.google.android.gms.auth.account.zza;
import com.google.android.gms.auth.account.zzc;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

final class zzauk extends zzm<Result, zzauq> {
    private /* synthetic */ Account zzecd;

    zzauk(zzaug zzaug, Api api, GoogleApiClient googleApiClient, Account account) {
        this.zzecd = account;
        super(api, googleApiClient);
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((Result) obj);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        ((zzc) ((zzauq) zzb).zzakn()).zza((zza) new zzaul(this), this.zzecd);
    }

    /* access modifiers changed from: protected */
    public final Result zzb(Status status) {
        return new zzaup(status);
    }
}
