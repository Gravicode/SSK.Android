package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzdyj implements Creator<zzdyi> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        zzdyw zzdyw = null;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 4:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 6:
                    zzdyw = (zzdyw) zzbfn.zza(parcel, readInt, zzdyw.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzdyi zzdyi = new zzdyi(str, z, str2, z2, zzdyw);
        return zzdyi;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdyi[i];
    }
}
