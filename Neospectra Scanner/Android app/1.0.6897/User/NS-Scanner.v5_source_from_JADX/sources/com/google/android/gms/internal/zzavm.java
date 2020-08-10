package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzavm implements Creator<zzavl> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        int i = 0;
        PendingIntent pendingIntent = null;
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
                    pendingIntent = (PendingIntent) zzbfn.zza(parcel, readInt, PendingIntent.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new zzavl(i, str, pendingIntent);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzavl[i];
    }
}
