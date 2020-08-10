package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbq;

public final class zzcgl extends zzbfm {
    public static final Creator<zzcgl> CREATOR = new zzcgm();
    public String packageName;
    private int versionCode;
    public String zziyf;
    public zzcln zziyg;
    public long zziyh;
    public boolean zziyi;
    public String zziyj;
    public zzcha zziyk;
    public long zziyl;
    public zzcha zziym;
    public long zziyn;
    public zzcha zziyo;

    zzcgl(int i, String str, String str2, zzcln zzcln, long j, boolean z, String str3, zzcha zzcha, long j2, zzcha zzcha2, long j3, zzcha zzcha3) {
        this.versionCode = i;
        this.packageName = str;
        this.zziyf = str2;
        this.zziyg = zzcln;
        this.zziyh = j;
        this.zziyi = z;
        this.zziyj = str3;
        this.zziyk = zzcha;
        this.zziyl = j2;
        this.zziym = zzcha2;
        this.zziyn = j3;
        this.zziyo = zzcha3;
    }

    zzcgl(zzcgl zzcgl) {
        this.versionCode = 1;
        zzbq.checkNotNull(zzcgl);
        this.packageName = zzcgl.packageName;
        this.zziyf = zzcgl.zziyf;
        this.zziyg = zzcgl.zziyg;
        this.zziyh = zzcgl.zziyh;
        this.zziyi = zzcgl.zziyi;
        this.zziyj = zzcgl.zziyj;
        this.zziyk = zzcgl.zziyk;
        this.zziyl = zzcgl.zziyl;
        this.zziym = zzcgl.zziym;
        this.zziyn = zzcgl.zziyn;
        this.zziyo = zzcgl.zziyo;
    }

    zzcgl(String str, String str2, zzcln zzcln, long j, boolean z, String str3, zzcha zzcha, long j2, zzcha zzcha2, long j3, zzcha zzcha3) {
        this.versionCode = 1;
        this.packageName = str;
        this.zziyf = str2;
        this.zziyg = zzcln;
        this.zziyh = j;
        this.zziyi = z;
        this.zziyj = str3;
        this.zziyk = zzcha;
        this.zziyl = j2;
        this.zziym = zzcha2;
        this.zziyn = j3;
        this.zziyo = zzcha3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.versionCode);
        zzbfp.zza(parcel, 2, this.packageName, false);
        zzbfp.zza(parcel, 3, this.zziyf, false);
        zzbfp.zza(parcel, 4, (Parcelable) this.zziyg, i, false);
        zzbfp.zza(parcel, 5, this.zziyh);
        zzbfp.zza(parcel, 6, this.zziyi);
        zzbfp.zza(parcel, 7, this.zziyj, false);
        zzbfp.zza(parcel, 8, (Parcelable) this.zziyk, i, false);
        zzbfp.zza(parcel, 9, this.zziyl);
        zzbfp.zza(parcel, 10, (Parcelable) this.zziym, i, false);
        zzbfp.zza(parcel, 11, this.zziyn);
        zzbfp.zza(parcel, 12, (Parcelable) this.zziyo, i, false);
        zzbfp.zzai(parcel, zze);
    }
}
