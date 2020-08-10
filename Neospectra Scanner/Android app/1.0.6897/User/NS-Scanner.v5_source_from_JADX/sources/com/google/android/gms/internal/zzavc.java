package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.accounttransfer.DeviceMetaData;
import com.google.android.gms.auth.api.accounttransfer.zzm;
import com.google.android.gms.auth.api.accounttransfer.zzu;
import com.google.android.gms.common.api.Status;

public abstract class zzavc extends zzev implements zzavb {
    public zzavc() {
        attachInterface(this, "com.google.android.gms.auth.api.accounttransfer.internal.IAccountTransferCallbacks");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                zze((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 2:
                zza((Status) zzew.zza(parcel, Status.CREATOR), (zzu) zzew.zza(parcel, zzu.CREATOR));
                return true;
            case 3:
                zza((Status) zzew.zza(parcel, Status.CREATOR), (zzm) zzew.zza(parcel, zzm.CREATOR));
                return true;
            case 4:
                zzaau();
                return true;
            case 5:
                onFailure((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 6:
                zzh(parcel.createByteArray());
                return true;
            case 7:
                zza((DeviceMetaData) zzew.zza(parcel, DeviceMetaData.CREATOR));
                return true;
            default:
                return false;
        }
    }
}
