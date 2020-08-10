package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public final class zzawo extends zzeu implements zzawn {
    zzawo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.auth.api.phone.internal.ISmsRetrieverApiService");
    }

    public final void zza(zzawp zzawp) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawp);
        zzb(1, zzbe);
    }
}
