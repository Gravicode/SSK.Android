package com.google.android.gms.auth.api.accounttransfer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;
import com.google.android.gms.internal.zzbfo;
import java.util.ArrayList;
import java.util.HashSet;

public final class zzn implements Creator<zzm> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int i;
        int zzd = zzbfn.zzd(parcel);
        HashSet hashSet = new HashSet();
        int i2 = 0;
        ArrayList arrayList = null;
        zzp zzp = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i2 = zzbfn.zzg(parcel, readInt);
                    i = 1;
                    break;
                case 2:
                    arrayList = zzbfn.zzc(parcel, readInt, zzs.CREATOR);
                    i = 2;
                    break;
                case 3:
                    i3 = zzbfn.zzg(parcel, readInt);
                    i = 3;
                    break;
                case 4:
                    zzp = (zzp) zzbfn.zza(parcel, readInt, zzp.CREATOR);
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
        zzm zzm = new zzm(hashSet, i2, arrayList, i3, zzp);
        return zzm;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzm[i];
    }
}
