package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzb implements Creator<ActionCodeSettings> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        boolean z = false;
        boolean z2 = false;
        int i = 0;
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
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                case 4:
                    str4 = zzbfn.zzq(parcel, readInt);
                    break;
                case 5:
                    z = zzbfn.zzc(parcel, readInt);
                    break;
                case 6:
                    str5 = zzbfn.zzq(parcel, readInt);
                    break;
                case 7:
                    z2 = zzbfn.zzc(parcel, readInt);
                    break;
                case 8:
                    str6 = zzbfn.zzq(parcel, readInt);
                    break;
                case 9:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        ActionCodeSettings actionCodeSettings = new ActionCodeSettings(str, str2, str3, str4, z, str5, z2, str6, i);
        return actionCodeSettings;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new ActionCodeSettings[i];
    }
}
