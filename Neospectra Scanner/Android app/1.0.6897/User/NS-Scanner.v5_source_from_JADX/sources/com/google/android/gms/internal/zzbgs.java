package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbgs implements Creator<zzbgv> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        int i = 0;
        zzbgo zzbgo = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    zzbgo = (zzbgo) zzbfn.zza(parcel, readInt, (Creator<T>) zzbgo.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzbgv(i, str, zzbgo);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgv[i];
    }
}
