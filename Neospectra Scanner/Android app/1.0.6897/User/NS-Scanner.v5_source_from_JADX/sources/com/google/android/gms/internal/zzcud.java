package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public final class zzcud implements Creator<zzctx> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        byte[] bArr = null;
        byte[][] bArr2 = null;
        byte[][] bArr3 = null;
        byte[][] bArr4 = null;
        byte[][] bArr5 = null;
        int[] iArr = null;
        byte[][] bArr6 = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    bArr = zzbfn.zzt(parcel, readInt);
                    break;
                case 4:
                    bArr2 = zzbfn.zzu(parcel, readInt);
                    break;
                case 5:
                    bArr3 = zzbfn.zzu(parcel, readInt);
                    break;
                case 6:
                    bArr4 = zzbfn.zzu(parcel, readInt);
                    break;
                case 7:
                    bArr5 = zzbfn.zzu(parcel, readInt);
                    break;
                case 8:
                    iArr = zzbfn.zzw(parcel, readInt);
                    break;
                case 9:
                    bArr6 = zzbfn.zzu(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzctx zzctx = new zzctx(str, bArr, bArr2, bArr3, bArr4, bArr5, iArr, bArr6);
        return zzctx;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzctx[i];
    }
}
