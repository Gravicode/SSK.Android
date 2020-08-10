package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public final class zzbgx implements Creator<zzbgu> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        int i = 0;
        ArrayList arrayList = null;
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
                    arrayList = zzbfn.zzc(parcel, readInt, zzbgv.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzbgu(i, str, arrayList);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbgu[i];
    }
}
