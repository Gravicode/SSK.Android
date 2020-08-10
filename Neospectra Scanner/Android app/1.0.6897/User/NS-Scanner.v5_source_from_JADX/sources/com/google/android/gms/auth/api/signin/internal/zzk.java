package com.google.android.gms.auth.api.signin.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

final class zzk extends zza {
    private /* synthetic */ zzj zzeid;

    zzk(zzj zzj) {
        this.zzeid = zzj;
    }

    public final void zzj(Status status) throws RemoteException {
        this.zzeid.setResult(status);
    }
}
