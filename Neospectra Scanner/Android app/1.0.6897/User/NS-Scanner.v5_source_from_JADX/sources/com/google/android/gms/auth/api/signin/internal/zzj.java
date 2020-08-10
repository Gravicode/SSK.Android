package com.google.android.gms.auth.api.signin.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzj extends zzl<Status> {
    zzj(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        zzd zzd = (zzd) zzb;
        ((zzu) zzd.zzakn()).zzc(new zzk(this), zzd.zzabk());
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
