package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzave extends zzeu implements zzavd {
    zzave(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.auth.api.accounttransfer.internal.IAccountTransferService");
    }

    public final void zza(zzavb zzavb, zzauz zzauz) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzavb);
        zzew.zza(zzbe, (Parcelable) zzauz);
        zzb(7, zzbe);
    }

    public final void zza(zzavb zzavb, zzavf zzavf) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzavb);
        zzew.zza(zzbe, (Parcelable) zzavf);
        zzb(9, zzbe);
    }

    public final void zza(zzavb zzavb, zzavh zzavh) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzavb);
        zzew.zza(zzbe, (Parcelable) zzavh);
        zzb(6, zzbe);
    }

    public final void zza(zzavb zzavb, zzavj zzavj) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzavb);
        zzew.zza(zzbe, (Parcelable) zzavj);
        zzb(5, zzbe);
    }

    public final void zza(zzavb zzavb, zzavl zzavl) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzavb);
        zzew.zza(zzbe, (Parcelable) zzavl);
        zzb(8, zzbe);
    }
}
