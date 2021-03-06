package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbr;

public final class zzcxo extends zzbfm {
    public static final Creator<zzcxo> CREATOR = new zzcxp();
    private int zzeck;
    private zzbr zzkcb;

    zzcxo(int i, zzbr zzbr) {
        this.zzeck = i;
        this.zzkcb = zzbr;
    }

    public zzcxo(zzbr zzbr) {
        this(1, zzbr);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.zzeck);
        zzbfp.zza(parcel, 2, (Parcelable) this.zzkcb, i, false);
        zzbfp.zzai(parcel, zze);
    }
}
