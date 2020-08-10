package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.IObjectWrapper.zza;
import com.google.android.gms.internal.zzeu;
import com.google.android.gms.internal.zzew;

public final class zzl extends zzeu implements zzk {
    zzl(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoader");
    }

    public final int zza(IObjectWrapper iObjectWrapper, String str, boolean z) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeString(str);
        zzew.zza(zzbe, z);
        Parcel zza = zza(3, zzbe);
        int readInt = zza.readInt();
        zza.recycle();
        return readInt;
    }

    public final IObjectWrapper zza(IObjectWrapper iObjectWrapper, String str, int i) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeString(str);
        zzbe.writeInt(i);
        Parcel zza = zza(2, zzbe);
        IObjectWrapper zzaq = zza.zzaq(zza.readStrongBinder());
        zza.recycle();
        return zzaq;
    }
}
