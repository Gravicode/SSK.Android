package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbgz implements Creator<zzbgy> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        Parcel parcel2 = null;
        int i = 0;
        zzbgt zzbgt = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    parcel2 = zzbfn.zzad(parcel, readInt);
                    break;
                case 3:
                    zzbgt = (zzbgt) zzbfn.zza(parcel, readInt, zzbgt.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzbgy(i, parcel2, zzbgt);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgy[i];
    }
}
