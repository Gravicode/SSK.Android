package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzr implements Creator<PhoneAuthCredential> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        boolean z = false;
        boolean z2 = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 2:
                    str2 = zzbfn.zzq(parcel, readInt);
                    break;
                case 3:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 4:
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                case 5:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 6:
                    str4 = zzbfn.zzq(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        PhoneAuthCredential phoneAuthCredential = new PhoneAuthCredential(str, str2, z, str3, z2, str4);
        return phoneAuthCredential;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new PhoneAuthCredential[i];
    }
}
