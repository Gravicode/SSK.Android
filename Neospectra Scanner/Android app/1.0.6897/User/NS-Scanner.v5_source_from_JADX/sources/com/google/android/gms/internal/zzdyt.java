package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzdyt implements Creator<zzdys> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 4:
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzdys(str, str2, str3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdys[i];
    }
}
