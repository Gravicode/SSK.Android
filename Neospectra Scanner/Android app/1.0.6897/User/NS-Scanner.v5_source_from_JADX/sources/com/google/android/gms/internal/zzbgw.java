package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public final class zzbgw implements Creator<zzbgt> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        ArrayList arrayList = null;
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    arrayList = zzbfn.zzc(parcel, readInt, zzbgu.CREATOR);
                    break;
                case 3:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzbgt(i, arrayList, str);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgt[i];
    }
}
