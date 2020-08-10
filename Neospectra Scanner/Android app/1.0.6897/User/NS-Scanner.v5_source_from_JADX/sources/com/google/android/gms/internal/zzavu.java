package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzavu extends zzavw<Status> {
    zzavu(zzavp zzavp, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final void zza(Context context, zzawd zzawd) throws RemoteException {
        zzawd.zza(new zzavv(this));
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
