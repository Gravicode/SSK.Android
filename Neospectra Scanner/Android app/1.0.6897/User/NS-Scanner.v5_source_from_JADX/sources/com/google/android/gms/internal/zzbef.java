package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class zzbef extends zzbfm {
    public static final Creator<zzbef> CREATOR = new zzbeg();
    private boolean zzfju;
    private long zzfjv;
    private long zzfjw;

    public zzbef(boolean z, long j, long j2) {
        this.zzfju = z;
        this.zzfjv = j;
        this.zzfjw = j2;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzbef) {
            zzbef zzbef = (zzbef) obj;
            return this.zzfju == zzbef.zzfju && this.zzfjv == zzbef.zzfjv && this.zzfjw == zzbef.zzfjw;
        }
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.zzfju), Long.valueOf(this.zzfjv), Long.valueOf(this.zzfjw)});
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("CollectForDebugParcelable[skipPersistentStorage: ");
        sb.append(this.zzfju);
        sb.append(",collectForDebugStartTimeMillis: ");
        sb.append(this.zzfjv);
        sb.append(",collectForDebugExpiryTimeMillis: ");
        sb.append(this.zzfjw);
        sb.append("]");
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 1, this.zzfju);
        zzbfp.zza(parcel, 2, this.zzfjw);
        zzbfp.zza(parcel, 3, this.zzfjv);
        zzbfp.zzai(parcel, zze);
    }
}
