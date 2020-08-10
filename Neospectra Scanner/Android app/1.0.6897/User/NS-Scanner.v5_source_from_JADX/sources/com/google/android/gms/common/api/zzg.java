package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzg implements Creator<Status> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        int i = 0;
        String str = null;
        PendingIntent pendingIntent = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i3 = 65535 & readInt;
            if (i3 != 1000) {
                switch (i3) {
                    case 1:
                        i2 = zzbfn.zzg(parcel, readInt);
                        break;
                    case 2:
                        str = zzbfn.zzq(parcel, readInt);
                        break;
                    case 3:
                        pendingIntent = (PendingIntent) zzbfn.zza(parcel, readInt, PendingIntent.CREATOR);
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
        return new Status(i, i2, str, pendingIntent);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new Status[i];
    }
}
