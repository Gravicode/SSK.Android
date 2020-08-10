package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.zza;

public final class zzexy extends zzeu implements zzexx {
    zzexy(IBinder iBinder) {
        super(iBinder, "com.google.firebase.storage.network.INetworkRequest");
    }

    public final int getResultCode() throws RemoteException {
        Parcel zza = zza(12, zzbe());
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }

    public final void reset() throws RemoteException {
        zzb(2, zzbe());
    }

    public final void zzbq(String str, String str2) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzb(5, zzbe);
    }

    public final void zzcmh() throws RemoteException {
        zzb(4, zzbe());
    }

    public final IObjectWrapper zzcmi() throws RemoteException {
        Parcel zza = zza(7, zzbe());
        IObjectWrapper zzaq = zza.zzaq(zza.readStrongBinder());
        zza.recycle();
        return zzaq;
    }

    public final IObjectWrapper zzcmj() throws RemoteException {
        Parcel zza = zza(8, zzbe());
        IObjectWrapper zzaq = zza.zzaq(zza.readStrongBinder());
        zza.recycle();
        return zzaq;
    }

    public final String zzcmk() throws RemoteException {
        Parcel zza = zza(9, zzbe());
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }

    public final IObjectWrapper zzcml() throws RemoteException {
        Parcel zza = zza(11, zzbe());
        IObjectWrapper zzaq = zza.zzaq(zza.readStrongBinder());
        zza.recycle();
        return zzaq;
    }

    public final boolean zzcmm() throws RemoteException {
        Parcel zza = zza(13, zzbe());
        boolean zza2 = zzew.zza(zza);
        zza.recycle();
        return zza2;
    }

    public final int zzcmn() throws RemoteException {
        Parcel zza = zza(14, zzbe());
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }

    public final void zzsl(String str) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzb(1, zzbe);
    }

    public final void zzsm(String str) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzb(3, zzbe);
    }

    public final String zzsn(String str) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        Parcel zza = zza(6, zzbe);
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }
}
