package com.google.android.gms.auth.api.accounttransfer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;
import com.google.android.gms.internal.zzbfo;
import java.util.HashSet;

public final class zzt implements Creator<zzs> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int i;
        int zzd = zzbfn.zzd(parcel);
        HashSet hashSet = new HashSet();
        int i2 = 0;
        zzu zzu = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i2 = zzbfn.zzg(parcel, readInt);
                    i = 1;
                    break;
                case 2:
                    zzu = (zzu) zzbfn.zza(parcel, readInt, zzu.CREATOR);
                    i = 2;
                    break;
                case 3:
                    str = zzbfn.zzq(parcel, readInt);
                    i = 3;
                    break;
                case 4:
                    str2 = zzbfn.zzq(parcel, readInt);
                    i = 4;
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    continue;
            }
            hashSet.add(Integer.valueOf(i));
        }
        if (parcel.dataPosition() != zzd) {
            StringBuilder sb = new StringBuilder(37);
            sb.append("Overread allowed size end=");
            sb.append(zzd);
            throw new zzbfo(sb.toString(), parcel);
        }
        zzs zzs = new zzs(hashSet, i2, zzu, str, str2);
        return zzs;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzs[i];
    }
}
