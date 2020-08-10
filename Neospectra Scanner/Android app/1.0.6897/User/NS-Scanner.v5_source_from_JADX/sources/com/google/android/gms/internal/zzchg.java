package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public final class zzchg extends zzeu implements zzche {
    zzchg(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    public final List<zzcln> zza(zzcgi zzcgi, boolean z) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzew.zza(zzbe, z);
        Parcel zza = zza(7, zzbe);
        ArrayList createTypedArrayList = zza.createTypedArrayList(zzcln.CREATOR);
        zza.recycle();
        return createTypedArrayList;
    }

    public final List<zzcgl> zza(String str, String str2, zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzew.zza(zzbe, (Parcelable) zzcgi);
        Parcel zza = zza(16, zzbe);
        ArrayList createTypedArrayList = zza.createTypedArrayList(zzcgl.CREATOR);
        zza.recycle();
        return createTypedArrayList;
    }

    public final List<zzcln> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzbe.writeString(str3);
        zzew.zza(zzbe, z);
        Parcel zza = zza(15, zzbe);
        ArrayList createTypedArrayList = zza.createTypedArrayList(zzcln.CREATOR);
        zza.recycle();
        return createTypedArrayList;
    }

    public final List<zzcln> zza(String str, String str2, boolean z, zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzew.zza(zzbe, z);
        zzew.zza(zzbe, (Parcelable) zzcgi);
        Parcel zza = zza(14, zzbe);
        ArrayList createTypedArrayList = zza.createTypedArrayList(zzcln.CREATOR);
        zza.recycle();
        return createTypedArrayList;
    }

    public final void zza(long j, String str, String str2, String str3) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeLong(j);
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzbe.writeString(str3);
        zzb(10, zzbe);
    }

    public final void zza(zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(4, zzbe);
    }

    public final void zza(zzcgl zzcgl, zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgl);
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(12, zzbe);
    }

    public final void zza(zzcha zzcha, zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcha);
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(1, zzbe);
    }

    public final void zza(zzcha zzcha, String str, String str2) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcha);
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzb(5, zzbe);
    }

    public final void zza(zzcln zzcln, zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcln);
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(2, zzbe);
    }

    public final byte[] zza(zzcha zzcha, String str) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcha);
        zzbe.writeString(str);
        Parcel zza = zza(9, zzbe);
        byte[] createByteArray = zza.createByteArray();
        zza.recycle();
        return createByteArray;
    }

    public final void zzb(zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(6, zzbe);
    }

    public final void zzb(zzcgl zzcgl) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgl);
        zzb(13, zzbe);
    }

    public final String zzc(zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgi);
        Parcel zza = zza(11, zzbe);
        String readString = zza.readString();
        zza.recycle();
        return readString;
    }

    public final void zzd(zzcgi zzcgi) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (Parcelable) zzcgi);
        zzb(18, zzbe);
    }

    public final List<zzcgl> zzj(String str, String str2, String str3) throws RemoteException {
        Parcel zzbe = zzbe();
        zzbe.writeString(str);
        zzbe.writeString(str2);
        zzbe.writeString(str3);
        Parcel zza = zza(17, zzbe);
        ArrayList createTypedArrayList = zza.createTypedArrayList(zzcgl.CREATOR);
        zza.recycle();
        return createTypedArrayList;
    }
}
