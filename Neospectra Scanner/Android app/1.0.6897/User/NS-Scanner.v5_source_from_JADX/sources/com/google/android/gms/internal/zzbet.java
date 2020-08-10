package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzbet extends zzeu implements zzbes {
    zzbet(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.clearcut.internal.IClearcutLoggerService");
    }

    public final void zza(zzbeq zzbeq, zzbeh zzbeh) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzbeq);
        zzew.zza(zzbe, (Parcelable) zzbeh);
        zzc(1, zzbe);
    }
}
