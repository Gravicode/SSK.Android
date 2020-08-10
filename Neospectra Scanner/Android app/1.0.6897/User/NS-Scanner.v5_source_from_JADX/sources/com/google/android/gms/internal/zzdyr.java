package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public final class zzdyr implements Creator<zzdyq> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        ArrayList arrayList = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            if ((65535 & readInt) != 2) {
                zzbfn.zzb(parcel, readInt);
            } else {
                arrayList = zzbfn.zzc(parcel, readInt, zzdyo.CREATOR);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzdyq(arrayList);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdyq[i];
    }
}
