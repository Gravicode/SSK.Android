package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbeg implements Creator<zzbef> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        long j = 0;
        long j2 = 0;
        boolean z = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 2:
                    j2 = zzbfn.zzi(parcel, readInt);
                    break;
                case 3:
                    j = zzbfn.zzi(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzbef zzbef = new zzbef(z, j, j2);
        return zzbef;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbef[i];
    }
}
