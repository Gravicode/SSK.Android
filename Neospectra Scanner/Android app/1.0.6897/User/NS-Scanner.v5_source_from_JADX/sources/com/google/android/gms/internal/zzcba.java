package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public final class zzcba extends zzeu implements zzcay {
    zzcba(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.flags.IFlagProvider");
    }

    public final boolean getBooleanFlagValue(String str, boolean z, int i) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzew.zza(zzbe, z);
        zzbe.writeInt(i);
        Parcel zza = zza(2, zzbe);
        boolean zza2 = zzew.zza(zza);
        zza.recycle();
        return zza2;
    }

    public final int getIntFlagValue(String str, int i, int i2) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeInt(i);
        zzbe.writeInt(i2);
        Parcel zza = zza(3, zzbe);
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }

    public final long getLongFlagValue(String str, long j, int i) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeLong(j);
        zzbe.writeInt(i);
        Parcel zza = zza(4, zzbe);
        long readLong = zza.readLong();
        zza.recycle();
        return readLong;
    }

    public final String getStringFlagValue(String str, String str2, int i) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzbe.writeInt(i);
        Parcel zza = zza(5, zzbe);
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }

    public final void init(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzb(1, zzbe);
    }
}
