package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzavk implements Creator<zzavj> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        int i = 0;
        byte[] bArr = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    bArr = zzbfn.zzt(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzavj(i, str, bArr);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzavj[i];
    }
}
