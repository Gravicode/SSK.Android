package com.google.android.gms.auth.api.accounttransfer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzw implements Creator<DeviceMetaData> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        long j = 0;
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
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 3:
                    j = zzbfn.zzi(parcel, readInt);
                    break;
                case 4:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        DeviceMetaData deviceMetaData = new DeviceMetaData(i, z, j, z2);
        return deviceMetaData;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new DeviceMetaData[i];
    }
}
