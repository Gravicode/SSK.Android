package com.google.android.gms.auth.api.accounttransfer;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;
import com.google.android.gms.internal.zzbfo;
import java.util.HashSet;

public final class zzv implements Creator<zzu> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int i;
        int zzd = zzbfn.zzd(parcel);
        HashSet hashSet = new HashSet();
        String str = null;
        byte[] bArr = null;
        PendingIntent pendingIntent = null;
        DeviceMetaData deviceMetaData = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i2 = zzbfn.zzg(parcel, readInt);
                    i = 1;
                    break;
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    i = 2;
                    break;
                case 3:
                    i3 = zzbfn.zzg(parcel, readInt);
                    i = 3;
                    break;
                case 4:
                    bArr = zzbfn.zzt(parcel, readInt);
                    i = 4;
                    break;
                case 5:
                    pendingIntent = (PendingIntent) zzbfn.zza(parcel, readInt, PendingIntent.CREATOR);
                    i = 5;
                    break;
                case 6:
                    deviceMetaData = (DeviceMetaData) zzbfn.zza(parcel, readInt, DeviceMetaData.CREATOR);
                    i = 6;
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
        zzu zzu = new zzu(hashSet, i2, str, i3, bArr, pendingIntent, deviceMetaData);
        return zzu;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzu[i];
    }
}
