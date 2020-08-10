package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzdyn implements Creator<zzdym> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        Long l = null;
        String str3 = null;
        Long l2 = null;
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
                    l = zzbfn.zzj(parcel, readInt);
                    break;
                case 5:
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                case 6:
                    l2 = zzbfn.zzj(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzdym zzdym = new zzdym(str, str2, l, str3, l2);
        return zzdym;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdym[i];
    }
}
