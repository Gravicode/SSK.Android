package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.internal.zzbfn;

public final class zzx implements Creator<SignInConfiguration> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        GoogleSignInOptions googleSignInOptions = null;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            int i = 65535 & readInt;
            if (i == 2) {
                str = zzbfn.zzq(parcel, readInt);
            } else if (i != 5) {
                zzbfn.zzb(parcel, readInt);
            } else {
                googleSignInOptions = (GoogleSignInOptions) zzbfn.zza(parcel, readInt, GoogleSignInOptions.CREATOR);
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new SignInConfiguration(str, googleSignInOptions);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new SignInConfiguration[i];
    }
}
