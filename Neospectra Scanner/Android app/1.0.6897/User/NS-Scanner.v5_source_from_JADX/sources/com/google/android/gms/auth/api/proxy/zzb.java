package com.google.android.gms.auth.api.proxy;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzb implements Creator<ProxyResponse> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        PendingIntent pendingIntent = null;
        Bundle bundle = null;
        byte[] bArr = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i4 = 65535 & readInt;
            if (i4 != 1000) {
                switch (i4) {
                    case 1:
                        i2 = zzbfn.zzg(parcel, readInt);
                        break;
                    case 2:
                        pendingIntent = (PendingIntent) zzbfn.zza(parcel, readInt, PendingIntent.CREATOR);
                        break;
                    case 3:
                        i3 = zzbfn.zzg(parcel, readInt);
                        break;
                    case 4:
                        bundle = zzbfn.zzs(parcel, readInt);
                        break;
                    case 5:
                        bArr = zzbfn.zzt(parcel, readInt);
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
        ProxyResponse proxyResponse = new ProxyResponse(i, i2, pendingIntent, i3, bundle, bArr);
        return proxyResponse;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new ProxyResponse[i];
    }
}
