package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzchb implements Creator<zzcha> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        zzcgx zzcgx = null;
        String str2 = null;
        long j = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    zzcgx = (zzcgx) zzbfn.zza(parcel, readInt, zzcgx.CREATOR);
                    break;
                case 4:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 5:
                    j = zzbfn.zzi(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzcha zzcha = new zzcha(str, zzcgx, str2, j);
        return zzcha;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzcha[i];
    }
}
