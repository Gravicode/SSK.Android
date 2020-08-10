package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.zzn;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzeu;
import com.google.android.gms.internal.zzew;

public final class zzbc extends zzeu implements zzba {
    zzbc(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
    }

    public final boolean zza(zzn zzn, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzn);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        Parcel zza = zza(5, zzbe);
        boolean zza2 = zzew.zza(zza);
        zza.recycle();
        return zza2;
    }
}
