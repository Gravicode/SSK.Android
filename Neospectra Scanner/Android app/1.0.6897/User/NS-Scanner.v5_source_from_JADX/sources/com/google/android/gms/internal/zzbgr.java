package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbgr implements Creator<zzbgo> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        zzbgh zzbgh = null;
        int i = 0;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    i2 = zzbfn.zzg(parcel, readInt);
                    break;
                case 3:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 4:
                    i3 = zzbfn.zzg(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 6:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 7:
                    i4 = zzbfn.zzg(parcel, readInt);
                    break;
                case 8:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 9:
                    zzbgh = (zzbgh) zzbfn.zza(parcel, readInt, zzbgh.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzbgo zzbgo = new zzbgo(i, i2, z, i3, z2, str, i4, str2, zzbgh);
        return zzbgo;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgo[i];
    }
}
