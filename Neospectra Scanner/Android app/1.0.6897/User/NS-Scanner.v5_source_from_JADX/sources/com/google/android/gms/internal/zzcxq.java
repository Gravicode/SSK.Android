package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbt;

public final class zzcxq extends zzbfm {
    public static final Creator<zzcxq> CREATOR = new zzcxr();
    private int zzeck;
    private final ConnectionResult zzfoo;
    private final zzbt zzkcc;

    public zzcxq(int i) {
        this(new ConnectionResult(8, null), null);
    }

    zzcxq(int i, ConnectionResult connectionResult, zzbt zzbt) {
        this.zzeck = i;
        this.zzfoo = connectionResult;
        this.zzkcc = zzbt;
    }

    private zzcxq(ConnectionResult connectionResult, zzbt zzbt) {
        this(1, connectionResult, null);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.zzeck);
        zzbfp.zza(parcel, 2, (Parcelable) this.zzfoo, i, false);
        zzbfp.zza(parcel, 3, (Parcelable) this.zzkcc, i, false);
        zzbfp.zzai(parcel, zze);
    }

    public final ConnectionResult zzahf() {
        return this.zzfoo;
    }

    public final zzbt zzbdi() {
        return this.zzkcc;
    }
}
