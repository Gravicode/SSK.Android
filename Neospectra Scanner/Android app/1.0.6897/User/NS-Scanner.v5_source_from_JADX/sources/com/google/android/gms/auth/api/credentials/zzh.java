package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzh implements Creator<HintRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        CredentialPickerConfig credentialPickerConfig = null;
        String[] strArr = null;
        String str = null;
        String str2 = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i2 = 65535 & readInt;
            if (i2 != 1000) {
                switch (i2) {
                    case 1:
                        credentialPickerConfig = (CredentialPickerConfig) zzbfn.zza(parcel, readInt, CredentialPickerConfig.CREATOR);
                        break;
                    case 2:
                        z = zzbfn.zzc(parcel, readInt);
                        break;
                    case 3:
                        z2 = zzbfn.zzc(parcel, readInt);
                        break;
                    case 4:
                        strArr = zzbfn.zzaa(parcel, readInt);
                        break;
                    case 5:
                        z3 = zzbfn.zzc(parcel, readInt);
                        break;
                    case 6:
                        str = zzbfn.zzq(parcel, readInt);
                        break;
                    case 7:
                        str2 = zzbfn.zzq(parcel, readInt);
                        break;
                    default:
                        zzbfn.zzb(parcel, readInt);
                        break;
                }
            } else {
                i = zzbfn.zzg(parcel, readInt);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        HintRequest hintRequest = new HintRequest(i, credentialPickerConfig, z, z2, strArr, z3, str, str2);
        return hintRequest;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new HintRequest[i];
    }
}
