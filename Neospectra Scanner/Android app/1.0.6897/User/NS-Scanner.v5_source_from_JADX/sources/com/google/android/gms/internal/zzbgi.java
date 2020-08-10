package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbgi implements Creator<zzbgh> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        int i = 0;
        zzbgj zzbgj = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    zzbgj = (zzbgj) zzbfn.zza(parcel, readInt, zzbgj.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzbgh(i, zzbgj);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgh[i];
    }
}
