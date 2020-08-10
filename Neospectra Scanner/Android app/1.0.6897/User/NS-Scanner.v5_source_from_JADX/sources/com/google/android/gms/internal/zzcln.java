package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbq;

public final class zzcln extends zzbfm {
    public static final Creator<zzcln> CREATOR = new zzclo();
    public final String name;
    private int versionCode;
    private String zzgcc;
    public final String zziyf;
    public final long zzjji;
    private Long zzjjj;
    private Float zzjjk;
    private Double zzjjl;

    zzcln(int i, String str, long j, Long l, Float f, String str2, String str3, Double d) {
        this.versionCode = i;
        this.name = str;
        this.zzjji = j;
        this.zzjjj = l;
        Double d2 = null;
        this.zzjjk = null;
        if (i == 1) {
            if (f != null) {
                d2 = Double.valueOf(f.doubleValue());
            }
            this.zzjjl = d2;
        } else {
            this.zzjjl = d;
        }
        this.zzgcc = str2;
        this.zziyf = str3;
    }

    zzcln(zzclp zzclp) {
        this(zzclp.mName, zzclp.zzjjm, zzclp.mValue, zzclp.mOrigin);
    }

    zzcln(String str, long j, Object obj, String str2) {
        zzbq.zzgm(str);
        this.versionCode = 2;
        this.name = str;
        this.zzjji = j;
        this.zziyf = str2;
        if (obj == null) {
            this.zzjjj = null;
            this.zzjjk = null;
            this.zzjjl = null;
            this.zzgcc = null;
        } else if (obj instanceof Long) {
            this.zzjjj = (Long) obj;
            this.zzjjk = null;
            this.zzjjl = null;
            this.zzgcc = null;
        } else if (obj instanceof String) {
            this.zzjjj = null;
            this.zzjjk = null;
            this.zzjjl = null;
            this.zzgcc = (String) obj;
        } else if (obj instanceof Double) {
            this.zzjjj = null;
            this.zzjjk = null;
            this.zzjjl = (Double) obj;
            this.zzgcc = null;
        } else {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
    }

    public final Object getValue() {
        if (this.zzjjj != null) {
            return this.zzjjj;
        }
        if (this.zzjjl != null) {
            return this.zzjjl;
        }
        if (this.zzgcc != null) {
            return this.zzgcc;
        }
        return null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.versionCode);
        zzbfp.zza(parcel, 2, this.name, false);
        zzbfp.zza(parcel, 3, this.zzjji);
        zzbfp.zza(parcel, 4, this.zzjjj, false);
        zzbfp.zza(parcel, 5, (Float) null, false);
        zzbfp.zza(parcel, 6, this.zzgcc, false);
        zzbfp.zza(parcel, 7, this.zziyf, false);
        zzbfp.zza(parcel, 8, this.zzjjl, false);
        zzbfp.zzai(parcel, zze);
    }
}
