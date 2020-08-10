package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzdyp implements Creator<zzdyo> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
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
                case 5:
                    str4 = zzbfn.zzq(parcel, readInt);
                    break;
                case 6:
                    str5 = zzbfn.zzq(parcel, readInt);
                    break;
                case 7:
                    str6 = zzbfn.zzq(parcel, readInt);
                    break;
                case 8:
                    str7 = zzbfn.zzq(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzdyo zzdyo = new zzdyo(str, str2, str3, str4, str5, str6, str7);
        return zzdyo;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdyo[i];
    }
}
