package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbq;
import java.util.Arrays;

public final class zzbew extends zzbfm {
    public static final Creator<zzbew> CREATOR = new zzbex();
    private String packageName;
    private int zzfja;
    public final String zzfjb;
    public final int zzfjc;
    private String zzfjd;
    private String zzfje;
    private boolean zzfjf;
    private int zzfjg;
    private boolean zzfko;

    public zzbew(String str, int i, int i2, String str2, String str3, String str4, boolean z, int i3) {
        this.packageName = (String) zzbq.checkNotNull(str);
        this.zzfja = i;
        this.zzfjc = i2;
        this.zzfjb = str2;
        this.zzfjd = str3;
        this.zzfje = str4;
        this.zzfko = !z;
        this.zzfjf = z;
        this.zzfjg = i3;
    }

    public zzbew(String str, int i, int i2, String str2, String str3, boolean z, String str4, boolean z2, int i3) {
        this.packageName = str;
        this.zzfja = i;
        this.zzfjc = i2;
        this.zzfjd = str2;
        this.zzfje = str3;
        this.zzfko = z;
        this.zzfjb = str4;
        this.zzfjf = z2;
        this.zzfjg = i3;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzbew) {
            zzbew zzbew = (zzbew) obj;
            return zzbg.equal(this.packageName, zzbew.packageName) && this.zzfja == zzbew.zzfja && this.zzfjc == zzbew.zzfjc && zzbg.equal(this.zzfjb, zzbew.zzfjb) && zzbg.equal(this.zzfjd, zzbew.zzfjd) && zzbg.equal(this.zzfje, zzbew.zzfje) && this.zzfko == zzbew.zzfko && this.zzfjf == zzbew.zzfjf && this.zzfjg == zzbew.zzfjg;
        }
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.packageName, Integer.valueOf(this.zzfja), Integer.valueOf(this.zzfjc), this.zzfjb, this.zzfjd, this.zzfje, Boolean.valueOf(this.zzfko), Boolean.valueOf(this.zzfjf), Integer.valueOf(this.zzfjg)});
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayLoggerContext[");
        sb.append("package=");
        sb.append(this.packageName);
        sb.append(',');
        sb.append("packageVersionCode=");
        sb.append(this.zzfja);
        sb.append(',');
        sb.append("logSource=");
        sb.append(this.zzfjc);
        sb.append(',');
        sb.append("logSourceName=");
        sb.append(this.zzfjb);
        sb.append(',');
        sb.append("uploadAccount=");
        sb.append(this.zzfjd);
        sb.append(',');
        sb.append("loggingId=");
        sb.append(this.zzfje);
        sb.append(',');
        sb.append("logAndroidId=");
        sb.append(this.zzfko);
        sb.append(',');
        sb.append("isAnonymous=");
        sb.append(this.zzfjf);
        sb.append(',');
        sb.append("qosTier=");
        sb.append(this.zzfjg);
        sb.append("]");
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.packageName, false);
        zzbfp.zzc(parcel, 3, this.zzfja);
        zzbfp.zzc(parcel, 4, this.zzfjc);
        zzbfp.zza(parcel, 5, this.zzfjd, false);
        zzbfp.zza(parcel, 6, this.zzfje, false);
        zzbfp.zza(parcel, 7, this.zzfko);
        zzbfp.zza(parcel, 8, this.zzfjb, false);
        zzbfp.zza(parcel, 9, this.zzfjf);
        zzbfp.zzc(parcel, 10, this.zzfjg);
        zzbfp.zzai(parcel, zze);
    }
}
