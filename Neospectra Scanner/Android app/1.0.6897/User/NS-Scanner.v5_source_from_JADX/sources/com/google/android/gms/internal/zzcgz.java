package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzcgz implements Creator<zzcgx> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        Bundle bundle = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            if ((65535 & readInt) != 2) {
                zzbfn.zzb(parcel, readInt);
            } else {
                bundle = zzbfn.zzs(parcel, readInt);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzcgx(bundle);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzcgx[i];
    }
}
