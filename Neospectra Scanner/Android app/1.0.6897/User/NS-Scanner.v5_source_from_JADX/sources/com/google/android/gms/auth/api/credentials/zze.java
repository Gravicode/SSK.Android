package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zze implements Creator<CredentialRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String[] strArr = null;
        CredentialPickerConfig credentialPickerConfig = null;
        CredentialPickerConfig credentialPickerConfig2 = null;
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
                        z = zzbfn.zzc(parcel, readInt);
                        break;
                    case 2:
                        strArr = zzbfn.zzaa(parcel, readInt);
                        break;
                    case 3:
                        credentialPickerConfig = (CredentialPickerConfig) zzbfn.zza(parcel, readInt, CredentialPickerConfig.CREATOR);
                        break;
                    case 4:
                        credentialPickerConfig2 = (CredentialPickerConfig) zzbfn.zza(parcel, readInt, CredentialPickerConfig.CREATOR);
                        break;
                    case 5:
                        z2 = zzbfn.zzc(parcel, readInt);
                        break;
                    case 6:
                        str = zzbfn.zzq(parcel, readInt);
                        break;
                    case 7:
                        str2 = zzbfn.zzq(parcel, readInt);
                        break;
                    case 8:
                        z3 = zzbfn.zzc(parcel, readInt);
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
        CredentialRequest credentialRequest = new CredentialRequest(i, z, strArr, credentialPickerConfig, credentialPickerConfig2, z2, str, str2, z3);
        return credentialRequest;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new CredentialRequest[i];
    }
}
