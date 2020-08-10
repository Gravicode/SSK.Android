package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzdyl implements Creator<zzdyk> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzd = zzbfn.zzd(parcel);
        long j = 0;
        long j2 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        zzdyq zzdyq = null;
        String str5 = null;
        String str6 = null;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel2, readInt);
                    break;
                case 3:
                    str2 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 4:
                    z = zzbfn.zzc(parcel2, readInt);
                    break;
                case 5:
                    str3 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 6:
                    str4 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 7:
                    zzdyq = (zzdyq) zzbfn.zza(parcel2, readInt, zzdyq.CREATOR);
                    break;
                case 8:
                    str5 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 9:
                    str6 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 10:
                    j = zzbfn.zzi(parcel2, readInt);
                    break;
                case 11:
                    j2 = zzbfn.zzi(parcel2, readInt);
                    break;
                case 12:
                    z2 = zzbfn.zzc(parcel2, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel2, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel2, zzd);
        zzdyk zzdyk = new zzdyk(str, str2, z, str3, str4, zzdyq, str5, str6, j, j2, z2);
        return zzdyk;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdyk[i];
    }
}
