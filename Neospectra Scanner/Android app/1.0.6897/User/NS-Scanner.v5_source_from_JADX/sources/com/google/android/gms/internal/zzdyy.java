package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;

public final class zzdyy extends zzbfm {
    public static final Creator<zzdyy> CREATOR = new zzdyz();
    private String mSessionId;
    private String zzbxx;
    private String zzefs;
    @Nullable
    private String zzegs;
    private String zzmcu;
    private String zzmcv;
    private boolean zzmdt;
    private String zzmgx;
    private String zzmgy;
    private String zzmgz;
    private boolean zzmha;
    private String zzmhb;
    private String zzmhc;

    public zzdyy() {
        this.zzmha = true;
        this.zzmdt = true;
    }

    public zzdyy(@Nullable String str, @Nullable String str2, String str3, @Nullable String str4, @Nullable String str5) {
        this(str, str2, str3, null, str5, null);
    }

    public zzdyy(@Nullable String str, @Nullable String str2, String str3, @Nullable String str4, @Nullable String str5, @Nullable String str6) {
        this.zzmgx = "http://localhost";
        this.zzefs = str;
        this.zzmcv = str2;
        this.zzmgz = str5;
        this.zzmhb = str6;
        this.zzmha = true;
        if (!TextUtils.isEmpty(this.zzefs) || !TextUtils.isEmpty(this.zzmcv) || !TextUtils.isEmpty(this.zzmhb)) {
            this.zzmcu = zzbq.zzgm(str3);
            this.zzegs = null;
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(this.zzefs)) {
                sb.append("id_token=");
                sb.append(this.zzefs);
                sb.append("&");
            }
            if (!TextUtils.isEmpty(this.zzmcv)) {
                sb.append("access_token=");
                sb.append(this.zzmcv);
                sb.append("&");
            }
            if (!TextUtils.isEmpty(this.zzegs)) {
                sb.append("identifier=");
                sb.append(this.zzegs);
                sb.append("&");
            }
            if (!TextUtils.isEmpty(this.zzmgz)) {
                sb.append("oauth_token_secret=");
                sb.append(this.zzmgz);
                sb.append("&");
            }
            if (!TextUtils.isEmpty(this.zzmhb)) {
                sb.append("code=");
                sb.append(this.zzmhb);
                sb.append("&");
            }
            sb.append("providerId=");
            sb.append(this.zzmcu);
            this.zzbxx = sb.toString();
            this.zzmdt = true;
            return;
        }
        throw new IllegalArgumentException("idToken, accessToken and authCode cannot all be null");
    }

    zzdyy(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, boolean z2, String str9, String str10, String str11) {
        this.zzmgx = str;
        this.zzmgy = str2;
        this.zzefs = str3;
        this.zzmcv = str4;
        this.zzmcu = str5;
        this.zzegs = str6;
        this.zzbxx = str7;
        this.zzmgz = str8;
        this.zzmha = z;
        this.zzmdt = z2;
        this.zzmhb = str9;
        this.mSessionId = str10;
        this.zzmhc = str11;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.zzmgx, false);
        zzbfp.zza(parcel, 3, this.zzmgy, false);
        zzbfp.zza(parcel, 4, this.zzefs, false);
        zzbfp.zza(parcel, 5, this.zzmcv, false);
        zzbfp.zza(parcel, 6, this.zzmcu, false);
        zzbfp.zza(parcel, 7, this.zzegs, false);
        zzbfp.zza(parcel, 8, this.zzbxx, false);
        zzbfp.zza(parcel, 9, this.zzmgz, false);
        zzbfp.zza(parcel, 10, this.zzmha);
        zzbfp.zza(parcel, 11, this.zzmdt);
        zzbfp.zza(parcel, 12, this.zzmhb, false);
        zzbfp.zza(parcel, 13, this.mSessionId, false);
        zzbfp.zza(parcel, 14, this.zzmhc, false);
        zzbfp.zzai(parcel, zze);
    }

    public final zzdyy zzch(boolean z) {
        this.zzmdt = false;
        return this;
    }
}
