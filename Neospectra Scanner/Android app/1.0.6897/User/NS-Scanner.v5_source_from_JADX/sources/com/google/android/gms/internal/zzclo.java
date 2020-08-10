package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzclo implements Creator<zzcln> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        Long l = null;
        Float f = null;
        String str2 = null;
        String str3 = null;
        Double d = null;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel2, readInt);
                    break;
                case 2:
                    str = zzbfn.zzq(parcel2, readInt);
                    break;
                case 3:
                    j = zzbfn.zzi(parcel2, readInt);
                    break;
                case 4:
                    l = zzbfn.zzj(parcel2, readInt);
                    break;
                case 5:
                    f = zzbfn.zzm(parcel2, readInt);
                    break;
                case 6:
                    str2 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 7:
                    str3 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 8:
                    d = zzbfn.zzo(parcel2, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel2, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel2, zzd);
        zzcln zzcln = new zzcln(i, str, j, l, f, str2, str3, d);
        return zzcln;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzcln[i];
    }
}
