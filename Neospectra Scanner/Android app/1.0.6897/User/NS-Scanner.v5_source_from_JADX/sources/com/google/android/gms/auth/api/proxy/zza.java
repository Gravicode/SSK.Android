package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zza implements Creator<ProxyRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        byte[] bArr = null;
        Bundle bundle = null;
        long j = 0;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i3 = 65535 & readInt;
            if (i3 != 1000) {
                switch (i3) {
                    case 1:
                        str = zzbfn.zzq(parcel, readInt);
                        break;
                    case 2:
                        i2 = zzbfn.zzg(parcel, readInt);
                        break;
                    case 3:
                        j = zzbfn.zzi(parcel, readInt);
                        break;
                    case 4:
                        bArr = zzbfn.zzt(parcel, readInt);
                        break;
                    case 5:
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
        ProxyRequest proxyRequest = new ProxyRequest(i, str, i2, j, bArr, bundle);
        return proxyRequest;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new ProxyRequest[i];
    }
}
