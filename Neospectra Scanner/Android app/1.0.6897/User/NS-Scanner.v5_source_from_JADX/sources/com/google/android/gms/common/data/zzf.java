package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzf implements Creator<DataHolder> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String[] strArr = null;
        CursorWindow[] cursorWindowArr = null;
        Bundle bundle = null;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i3 = 65535 & readInt;
            if (i3 != 1000) {
                switch (i3) {
                    case 1:
                        strArr = zzbfn.zzaa(parcel, readInt);
                        break;
                    case 2:
                        cursorWindowArr = (CursorWindow[]) zzbfn.zzb(parcel, readInt, CursorWindow.CREATOR);
                        break;
                    case 3:
                        i2 = zzbfn.zzg(parcel, readInt);
                        break;
                    case 4:
                        bundle = zzbfn.zzs(parcel, readInt);
                        break;
                    default:
                        zzbfn.zzb(parcel, readInt);
                        break;
                }
            } else {
                i = zzbfn.zzg(parcel, readInt);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        DataHolder dataHolder = new DataHolder(i, strArr, cursorWindowArr, i2, bundle);
        dataHolder.zzajz();
        return dataHolder;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new DataHolder[i];
    }
}
