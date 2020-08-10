package com.google.android.gms.internal;

import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public final class zzeya extends zzeu implements zzexz {
    zzeya(IBinder iBinder) {
        super(iBinder, "com.google.firebase.storage.network.INetworkRequestFactory");
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        Parcel zza = zza(1, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeLong(j);
        Parcel zza = zza(3, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzew.zza(zzbe, (IInterface) iObjectWrapper2);
        Parcel zza = zza(9, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, String str) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzew.zza(zzbe, (IInterface) iObjectWrapper2);
        zzbe.writeString(str);
        Parcel zza = zza(8, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeString(str);
        Parcel zza = zza(6, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zza(Uri uri, IObjectWrapper iObjectWrapper, String str, IObjectWrapper iObjectWrapper2, long j, int i, boolean z) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeString(str);
        zzew.zza(zzbe, (IInterface) iObjectWrapper2);
        zzbe.writeLong(j);
        zzbe.writeInt(i);
        zzew.zza(zzbe, z);
        Parcel zza = zza(5, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zzb(Uri uri, IObjectWrapper iObjectWrapper) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        Parcel zza = zza(2, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final zzexx zzb(Uri uri, IObjectWrapper iObjectWrapper, String str) throws RemoteException {
        zzexx zzexx;
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        zzew.zza(zzbe, (IInterface) iObjectWrapper);
        zzbe.writeString(str);
        Parcel zza = zza(7, zzbe);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzexx = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.firebase.storage.network.INetworkRequest");
            zzexx = queryLocalInterface instanceof zzexx ? (zzexx) queryLocalInterface : new zzexy(readStrongBinder);
        }
        zza.recycle();
        return zzexx;
    }

    public final String zzcmo() throws RemoteException {
        Parcel zza = zza(10, zzbe());
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }

    public final String zzu(Uri uri) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) uri);
        Parcel zza = zza(11, zzbe);
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }
}
