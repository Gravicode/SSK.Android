package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzavt extends zzavw<Status> {
    private /* synthetic */ Credential zzegf;

    zzavt(zzavp zzavp, GoogleApiClient googleApiClient, Credential credential) {
        this.zzegf = credential;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final void zza(Context context, zzawd zzawd) throws RemoteException {
        zzawd.zza((zzawb) new zzavv(this), new zzavz(this.zzegf));
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
