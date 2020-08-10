package com.google.firebase.auth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzbfp;
import com.google.android.gms.internal.zzdyy;

public class zzd extends AuthCredential {
    public static final Creator<zzd> CREATOR = new zze();
    private final String zzefs;
    private final String zzmcu;
    private final String zzmcv;
    private final zzdyy zzmcw;

    zzd(@NonNull String str, @Nullable String str2, @Nullable String str3, @Nullable zzdyy zzdyy) {
        this.zzmcu = str;
        this.zzefs = str2;
        this.zzmcv = str3;
        this.zzmcw = zzdyy;
    }

    public static zzdyy zza(@NonNull zzd zzd) {
        zzbq.checkNotNull(zzd);
        if (zzd.zzmcw != null) {
            return zzd.zzmcw;
        }
        zzdyy zzdyy = new zzdyy(zzd.zzefs, zzd.zzmcv, zzd.getProvider(), null, null);
        return zzdyy;
    }

    public static zzd zza(@NonNull zzdyy zzdyy) {
        return new zzd(null, null, null, (zzdyy) zzbq.checkNotNull(zzdyy, "Must specify a non-null webSignInCredential"));
    }

    public static zzd zzn(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
            return new zzd(zzbq.zzh(str, "Must specify a non-empty providerId"), str2, str3, null);
        }
        throw new IllegalArgumentException("Must specify an idToken or an accessToken.");
    }

    public String getProvider() {
        return this.zzmcu;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 1, getProvider(), false);
        zzbfp.zza(parcel, 2, this.zzefs, false);
        zzbfp.zza(parcel, 3, this.zzmcv, false);
        zzbfp.zza(parcel, 4, (Parcelable) this.zzmcw, i, false);
        zzbfp.zzai(parcel, zze);
    }
}
