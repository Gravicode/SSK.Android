package com.google.android.gms.auth.api.signin;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzf implements Creator<SignInAccount> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = "";
        String str2 = "";
        GoogleSignInAccount googleSignInAccount = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i = 65535 & readInt;
            if (i != 4) {
                switch (i) {
                    case 7:
                        googleSignInAccount = (GoogleSignInAccount) zzbfn.zza(parcel, readInt, GoogleSignInAccount.CREATOR);
                        break;
                    case 8:
                        str2 = zzbfn.zzq(parcel, readInt);
                        break;
                    default:
                        zzbfn.zzb(parcel, readInt);
                        break;
                }
            } else {
                str = zzbfn.zzq(parcel, readInt);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new SignInAccount(str, googleSignInAccount, str2);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new SignInAccount[i];
    }
}
