package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzg implements Creator<FacebookAuthCredential> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            if ((65535 & readInt) != 1) {
                zzbfn.zzb(parcel, readInt);
            } else {
                str = zzbfn.zzq(parcel, readInt);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new FacebookAuthCredential(str);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new FacebookAuthCredential[i];
    }
}
