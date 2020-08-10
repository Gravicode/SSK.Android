package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public final class zzbgf extends zzeu implements zzbge {
    zzbgf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.service.ICommonService");
    }

    public final void zza(zzbgc zzbgc) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzbgc);
        zzc(1, zzbe);
    }
}
