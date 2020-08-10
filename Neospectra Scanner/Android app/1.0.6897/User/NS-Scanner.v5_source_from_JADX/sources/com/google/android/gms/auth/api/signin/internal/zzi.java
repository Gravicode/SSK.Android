package com.google.android.gms.auth.api.signin.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

final class zzi extends zza {
    private /* synthetic */ zzh zzeic;

    zzi(zzh zzh) {
        this.zzeic = zzh;
    }

    public final void zzi(Status status) throws RemoteException {
        this.zzeic.setResult(status);
    }
}
