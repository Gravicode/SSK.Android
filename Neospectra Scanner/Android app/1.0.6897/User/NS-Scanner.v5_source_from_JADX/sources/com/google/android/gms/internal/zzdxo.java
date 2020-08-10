package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.PhoneAuthCredential;

public abstract class zzdxo extends zzev implements zzdxn {
    public zzdxo() {
        attachInterface(this, "com.google.firebase.auth.api.internal.IFirebaseAuthCallbacks");
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                zzb((zzdym) zzew.zza(parcel, zzdym.CREATOR));
                return true;
            case 2:
                zza((zzdym) zzew.zza(parcel, zzdym.CREATOR), (zzdyk) zzew.zza(parcel, zzdyk.CREATOR));
                return true;
            case 3:
                zza((zzdyi) zzew.zza(parcel, zzdyi.CREATOR));
                return true;
            case 4:
                zza((zzdys) zzew.zza(parcel, zzdys.CREATOR));
                return true;
            case 5:
                onFailure((Status) zzew.zza(parcel, Status.CREATOR));
                return true;
            case 6:
                zzbrn();
                return true;
            case 7:
                zzbro();
                return true;
            case 8:
                zzow(parcel.readString());
                return true;
            case 9:
                zzox(parcel.readString());
                return true;
            case 10:
                onVerificationCompleted((PhoneAuthCredential) zzew.zza(parcel, PhoneAuthCredential.CREATOR));
                return true;
            case 11:
                zzoy(parcel.readString());
                return true;
            case 12:
                zza((Status) zzew.zza(parcel, Status.CREATOR), (PhoneAuthCredential) zzew.zza(parcel, PhoneAuthCredential.CREATOR));
                return true;
            case 13:
                zzbrp();
                return true;
            default:
                return false;
        }
    }
}
