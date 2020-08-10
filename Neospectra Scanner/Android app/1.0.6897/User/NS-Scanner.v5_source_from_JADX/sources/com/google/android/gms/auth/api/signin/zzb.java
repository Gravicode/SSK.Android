package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbfn;
import java.util.List;

public final class zzb implements Creator<GoogleSignInAccount> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Uri uri = null;
        String str5 = null;
        String str6 = null;
        List list = null;
        String str7 = null;
        String str8 = null;
        long j = 0;
        int i = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel2, readInt);
                    break;
                case 2:
                    str = zzbfn.zzq(parcel2, readInt);
                    break;
                case 3:
                    str2 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 4:
                    str3 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 5:
                    str4 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 6:
                    uri = (Uri) zzbfn.zza(parcel2, readInt, Uri.CREATOR);
                    break;
                case 7:
                    str5 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 8:
                    j = zzbfn.zzi(parcel2, readInt);
                    break;
                case 9:
                    str6 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 10:
                    list = zzbfn.zzc(parcel2, readInt, Scope.CREATOR);
                    break;
                case 11:
                    str7 = zzbfn.zzq(parcel2, readInt);
                    break;
                case 12:
                    str8 = zzbfn.zzq(parcel2, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel2, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel2, zzd);
        GoogleSignInAccount googleSignInAccount = new GoogleSignInAccount(i, str, str2, str3, str4, uri, str5, j, str6, list, str7, str8);
        return googleSignInAccount;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new GoogleSignInAccount[i];
    }
}
