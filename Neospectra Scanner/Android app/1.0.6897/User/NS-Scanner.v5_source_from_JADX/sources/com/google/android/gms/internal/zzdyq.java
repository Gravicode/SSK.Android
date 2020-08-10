package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class zzdyq extends zzbfm {
    public static final Creator<zzdyq> CREATOR = new zzdyr();
    private List<zzdyo> zzmgq;

    public zzdyq() {
        this.zzmgq = new ArrayList();
    }

    zzdyq(List<zzdyo> list) {
        this.zzmgq = (list == null || list.isEmpty()) ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    public static zzdyq zza(zzdyq zzdyq) {
        List<zzdyo> list = zzdyq.zzmgq;
        zzdyq zzdyq2 = new zzdyq();
        if (list != null) {
            zzdyq2.zzmgq.addAll(list);
        }
        return zzdyq2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 2, this.zzmgq, false);
        zzbfp.zzai(parcel, zze);
    }

    public final List<zzdyo> zzbrt() {
        return this.zzmgq;
    }
}
