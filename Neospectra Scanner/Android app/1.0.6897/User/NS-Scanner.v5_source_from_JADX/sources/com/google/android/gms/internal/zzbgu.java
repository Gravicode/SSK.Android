package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class zzbgu extends zzbfm {
    public static final Creator<zzbgu> CREATOR = new zzbgx();
    final String className;
    private int versionCode;
    private ArrayList<zzbgv> zzgcr;

    zzbgu(int i, String str, ArrayList<zzbgv> arrayList) {
        this.versionCode = i;
        this.className = str;
        this.zzgcr = arrayList;
    }

    zzbgu(String str, Map<String, zzbgo<?, ?>> map) {
        ArrayList<zzbgv> arrayList;
        this.versionCode = 1;
        this.className = str;
        if (map == null) {
            arrayList = null;
        } else {
            arrayList = new ArrayList<>();
            for (String str2 : map.keySet()) {
                arrayList.add(new zzbgv(str2, (zzbgo) map.get(str2)));
            }
        }
        this.zzgcr = arrayList;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.versionCode);
        zzbfp.zza(parcel, 2, this.className, false);
        zzbfp.zzc(parcel, 3, this.zzgcr, false);
        zzbfp.zzai(parcel, zze);
    }

    /* access modifiers changed from: 0000 */
    public final HashMap<String, zzbgo<?, ?>> zzama() {
        HashMap<String, zzbgo<?, ?>> hashMap = new HashMap<>();
        int size = this.zzgcr.size();
        for (int i = 0; i < size; i++) {
            zzbgv zzbgv = (zzbgv) this.zzgcr.get(i);
            hashMap.put(zzbgv.key, zzbgv.zzgcs);
        }
        return hashMap;
    }
}
