package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzcgm implements Creator<zzcgl> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzd = zzbfn.zzd(parcel);
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        String str = null;
        String str2 = null;
        zzcln zzcln = null;
        String str3 = null;
        zzcha zzcha = null;
        zzcha zzcha2 = null;
        zzcha zzcha3 = null;
        int i = 0;
        boolean z = false;
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
                    str2 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 4:
                    zzcln = (zzcln) zzbfn.zza(parcel2, readInt, zzcln.CREATOR);
                    break;
                case 5:
                    j = zzbfn.zzi(parcel2, readInt);
                    break;
                case 6:
                    z = zzbfn.zzc(parcel2, readInt);
                    break;
                case 7:
                    str3 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 8:
                    zzcha = (zzcha) zzbfn.zza(parcel2, readInt, zzcha.CREATOR);
                    break;
                case 9:
                    j2 = zzbfn.zzi(parcel2, readInt);
                    break;
                case 10:
                    zzcha2 = (zzcha) zzbfn.zza(parcel2, readInt, zzcha.CREATOR);
                    break;
                case 11:
                    j3 = zzbfn.zzi(parcel2, readInt);
                    break;
                case 12:
                    zzcha3 = (zzcha) zzbfn.zza(parcel2, readInt, zzcha.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel2, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel2, zzd);
        zzcgl zzcgl = new zzcgl(i, str, str2, zzcln, j, z, str3, zzcha, j2, zzcha2, j3, zzcha3);
        return zzcgl;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzcgl[i];
    }
}
