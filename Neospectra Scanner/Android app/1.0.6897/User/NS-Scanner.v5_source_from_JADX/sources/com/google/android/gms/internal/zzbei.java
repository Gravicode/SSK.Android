package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzbei implements Creator<zzbeh> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        zzbew zzbew = null;
        byte[] bArr = null;
        int[] iArr = null;
        String[] strArr = null;
        int[] iArr2 = null;
        byte[][] bArr2 = null;
        zzctx[] zzctxArr = null;
        boolean z = true;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    zzbew = (zzbew) zzbfn.zza(parcel, readInt, zzbew.CREATOR);
                    break;
                case 3:
                    bArr = zzbfn.zzt(parcel, readInt);
                    break;
                case 4:
                    iArr = zzbfn.zzw(parcel, readInt);
                    break;
                case 5:
                    strArr = zzbfn.zzaa(parcel, readInt);
                    break;
                case 6:
                    iArr2 = zzbfn.zzw(parcel, readInt);
                    break;
                case 7:
                    bArr2 = zzbfn.zzu(parcel, readInt);
                    break;
                case 8:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 9:
                    zzctxArr = (zzctx[]) zzbfn.zzb(parcel, readInt, zzctx.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzbeh zzbeh = new zzbeh(zzbew, bArr, iArr, strArr, iArr2, bArr2, z, zzctxArr);
        return zzbeh;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbeh[i];
    }
}
