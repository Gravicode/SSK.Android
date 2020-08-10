package com.google.android.gms.auth.api.credentials;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;
import java.util.List;

public final class zza implements Creator<Credential> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        String str2 = null;
        Uri uri = null;
        List list = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
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
                    uri = (Uri) zzbfn.zza(parcel, readInt, Uri.CREATOR);
                    break;
                case 4:
                    list = zzbfn.zzc(parcel, readInt, IdToken.CREATOR);
                    break;
                case 5:
                    str3 = zzbfn.zzq(parcel, readInt);
                    break;
                case 6:
                    str4 = zzbfn.zzq(parcel, readInt);
                    break;
                case 7:
                    str5 = zzbfn.zzq(parcel, readInt);
                    break;
                case 8:
                    str6 = zzbfn.zzq(parcel, readInt);
                    break;
                case 9:
                    str7 = zzbfn.zzq(parcel, readInt);
                    break;
                case 10:
                    str8 = zzbfn.zzq(parcel, readInt);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        Credential credential = new Credential(str, str2, uri, list, str3, str4, str5, str6, str7, str8);
        return credential;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new Credential[i];
    }
}
