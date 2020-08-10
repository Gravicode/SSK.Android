package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbq;

public final class zzcha extends zzbfm {
    public static final Creator<zzcha> CREATOR = new zzchb();
    public final String name;
    public final String zziyf;
    public final zzcgx zzizt;
    public final long zzizu;

    zzcha(zzcha zzcha, long j) {
        zzbq.checkNotNull(zzcha);
        this.name = zzcha.name;
        this.zzizt = zzcha.zzizt;
        this.zziyf = zzcha.zziyf;
        this.zzizu = j;
    }

    public zzcha(String str, zzcgx zzcgx, String str2, long j) {
        this.name = str;
        this.zzizt = zzcgx;
        this.zziyf = str2;
        this.zzizu = j;
    }

    public final String toString() {
        String str = this.zziyf;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzizt);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 21 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("origin=");
        sb.append(str);
        sb.append(",name=");
        sb.append(str2);
        sb.append(",params=");
        sb.append(valueOf);
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.name, false);
        zzbfp.zza(parcel, 3, (Parcelable) this.zzizt, i, false);
        zzbfp.zza(parcel, 4, this.zziyf, false);
        zzbfp.zza(parcel, 5, this.zzizu);
        zzbfp.zzai(parcel, zze);
    }
}
