package com.google.android.gms.auth.api.accounttransfer;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p001v4.app.NotificationCompat;
import com.google.android.gms.internal.zzaxc;
import com.google.android.gms.internal.zzbfp;
import com.google.android.gms.internal.zzbgo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class zzm extends zzaxc {
    public static final Creator<zzm> CREATOR = new zzn();
    private static final HashMap<String, zzbgo<?, ?>> zzeea;
    private int zzeck;
    private Set<Integer> zzeeb;
    private ArrayList<zzs> zzeec;
    private int zzeed;
    private zzp zzeee;

    static {
        HashMap<String, zzbgo<?, ?>> hashMap = new HashMap<>();
        zzeea = hashMap;
        hashMap.put("authenticatorData", zzbgo.zzb("authenticatorData", 2, zzs.class));
        zzeea.put(NotificationCompat.CATEGORY_PROGRESS, zzbgo.zza(NotificationCompat.CATEGORY_PROGRESS, 4, zzp.class));
    }

    public zzm() {
        this.zzeeb = new HashSet(1);
        this.zzeck = 1;
    }

    zzm(Set<Integer> set, int i, ArrayList<zzs> arrayList, int i2, zzp zzp) {
        this.zzeeb = set;
        this.zzeck = i;
        this.zzeec = arrayList;
        this.zzeed = i2;
        this.zzeee = zzp;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        Set<Integer> set = this.zzeeb;
        if (set.contains(Integer.valueOf(1))) {
            zzbfp.zzc(parcel, 1, this.zzeck);
        }
        if (set.contains(Integer.valueOf(2))) {
            zzbfp.zzc(parcel, 2, this.zzeec, true);
        }
        if (set.contains(Integer.valueOf(3))) {
            zzbfp.zzc(parcel, 3, this.zzeed);
        }
        if (set.contains(Integer.valueOf(4))) {
            zzbfp.zza(parcel, 4, (Parcelable) this.zzeee, i, true);
        }
        zzbfp.zzai(parcel, zze);
    }

    /* access modifiers changed from: protected */
    public final boolean zza(zzbgo zzbgo) {
        return this.zzeeb.contains(Integer.valueOf(zzbgo.zzalu()));
    }

    public final /* synthetic */ Map zzaav() {
        return zzeea;
    }

    /* access modifiers changed from: protected */
    public final Object zzb(zzbgo zzbgo) {
        int zzalu = zzbgo.zzalu();
        if (zzalu == 4) {
            return this.zzeee;
        }
        switch (zzalu) {
            case 1:
                return Integer.valueOf(this.zzeck);
            case 2:
                return this.zzeec;
            default:
                int zzalu2 = zzbgo.zzalu();
                StringBuilder sb = new StringBuilder(37);
                sb.append("Unknown SafeParcelable id=");
                sb.append(zzalu2);
                throw new IllegalStateException(sb.toString());
        }
    }
}
