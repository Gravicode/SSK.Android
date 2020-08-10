package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbex implements Creator<zzbew> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        int i = 0;
        int i2 = 0;
        boolean z = true;
        boolean z2 = false;
        int i3 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 4:
                    i2 = zzbfn.zzg(parcel, readInt);
                    break;
                case 5:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 6:
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                case 7:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 8:
                    str4 = zzbfn.zzq(parcel, readInt);
                    break;
                case 9:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 10:
                    i3 = zzbfn.zzg(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzbew zzbew = new zzbew(str, i, i2, str2, str3, z, str4, z2, i3);
        return zzbew;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbew[i];
    }
}
