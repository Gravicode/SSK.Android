package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzan;

public final class zzcxm extends zzeu implements zzcxl {
    zzcxm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.signin.internal.ISignInService");
    }

    public final void zza(zzan zzan, int i, boolean z) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzan);
        zzbe.writeInt(i);
        zzew.zza(zzbe, z);
        zzb(9, zzbe);
    }

    public final void zza(zzcxo zzcxo, zzcxj zzcxj) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcxo);
        zzew.zza(zzbe, (IInterface) zzcxj);
        zzb(12, zzbe);
    }

    public final void zzeh(int i) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeInt(i);
        zzb(7, zzbe);
    }
}
