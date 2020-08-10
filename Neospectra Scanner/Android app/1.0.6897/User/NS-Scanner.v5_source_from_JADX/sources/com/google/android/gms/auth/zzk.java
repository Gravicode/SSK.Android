package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;
import java.util.List;

public final class zzk implements Creator<TokenData> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        Long l = null;
        List list = null;
        int i = 0;
        boolean z = false;
        boolean z2 = false;
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
                    l = zzbfn.zzj(parcel, readInt);
                    break;
                case 4:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 6:
                    list = zzbfn.zzac(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        TokenData tokenData = new TokenData(i, str, l, z, z2, list);
        return tokenData;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new TokenData[i];
    }
}
