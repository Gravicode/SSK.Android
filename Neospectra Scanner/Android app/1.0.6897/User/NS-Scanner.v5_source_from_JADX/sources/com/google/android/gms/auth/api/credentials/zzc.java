package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzc implements Creator<CredentialPickerConfig> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        int i = 0;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        int i2 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i3 = 65535 & readInt;
            if (i3 != 1000) {
                switch (i3) {
                    case 1:
                        z = zzbfn.zzc(parcel, readInt);
                        break;
                    case 2:
                        z2 = zzbfn.zzc(parcel, readInt);
                        break;
                    case 3:
                        z3 = zzbfn.zzc(parcel, readInt);
                        break;
                    case 4:
                        i2 = zzbfn.zzg(parcel, readInt);
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
        CredentialPickerConfig credentialPickerConfig = new CredentialPickerConfig(i, z, z2, z3, i2);
        return credentialPickerConfig;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new CredentialPickerConfig[i];
    }
}
