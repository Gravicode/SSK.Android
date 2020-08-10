package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;

public final class zzawg implements Creator<zzawf> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        Credential credential = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            if ((65535 & readInt) != 1) {
                zzbfn.zzb(parcel, readInt);
            } else {
                credential = (Credential) zzbfn.zza(parcel, readInt, Credential.CREATOR);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzawf(credential);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzawf[i];
    }
}
