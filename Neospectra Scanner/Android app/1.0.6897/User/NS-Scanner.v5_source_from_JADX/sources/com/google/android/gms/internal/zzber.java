package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;

public abstract class zzber extends zzev implements zzbeq {
    public zzber() {
        attachInterface(this, "com.google.android.gms.clearcut.internal.IClearcutLoggerCallbacks");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                zzo((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 2:
                zzp((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 3:
                zza((Status) zzew.zza(parcel, Status.CREATOR), parcel.readLong());
                return true;
            case 4:
                zzq((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 5:
                zzb((Status) zzew.zza(parcel, Status.CREATOR), parcel.readLong());
                return true;
            case 6:
                zza((Status) zzew.zza(parcel, Status.CREATOR), (zzbeh[]) parcel.createTypedArray(zzbeh.CREATOR));
                return true;
            case 7:
                zza((DataHolder) zzew.zza(parcel, DataHolder.CREATOR));
                return true;
            case 8:
                zza((Status) zzew.zza(parcel, Status.CREATOR), (zzbef) zzew.zza(parcel, zzbef.CREATOR));
                return true;
            case 9:
                zzb((Status) zzew.zza(parcel, Status.CREATOR), (zzbef) zzew.zza(parcel, zzbef.CREATOR));
                return true;
            default:
                return false;
        }
    }
}
