package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class zzdyw extends zzbfm {
    public static final Creator<zzdyw> CREATOR = new zzdyx();
    private int zzeck;
    private List<String> zzmgw;

    public zzdyw() {
        this(null);
    }

    zzdyw(int i, List<String> list) {
        List<String> emptyList;
        this.zzeck = i;
        if (list == null || list.isEmpty()) {
            emptyList = Collections.emptyList();
        } else {
            for (int i2 = 0; i2 < list.size(); i2++) {
                String str = (String) list.get(i2);
                if (TextUtils.isEmpty(str)) {
                    str = null;
                }
                list.set(i2, str);
            }
            emptyList = Collections.unmodifiableList(list);
        }
        this.zzmgw = emptyList;
    }

    private zzdyw(@Nullable List<String> list) {
        this.zzeck = 1;
        this.zzmgw = new ArrayList();
        if (list != null && !list.isEmpty()) {
            this.zzmgw.addAll(list);
        }
    }

    public static zzdyw zza(zzdyw zzdyw) {
        return new zzdyw(zzdyw != null ? zzdyw.zzmgw : null);
    }

    public static zzdyw zzbsb() {
        return new zzdyw(null);
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.zzeck);
        zzbfp.zzb(parcel, 2, this.zzmgw, false);
        zzbfp.zzai(parcel, zze);
    }

    public final List<String> zzbsa() {
        return this.zzmgw;
    }
}
