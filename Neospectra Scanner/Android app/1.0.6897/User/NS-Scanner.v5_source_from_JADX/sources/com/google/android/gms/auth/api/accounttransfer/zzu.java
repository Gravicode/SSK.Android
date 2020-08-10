package com.google.android.gms.auth.api.accounttransfer;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p001v4.app.NotificationCompat;
import android.support.p001v4.util.ArraySet;
import com.google.android.gms.internal.zzaxc;
import com.google.android.gms.internal.zzbfp;
import com.google.android.gms.internal.zzbgo;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class zzu extends zzaxc {
    public static final Creator<zzu> CREATOR = new zzv();
    private static final HashMap<String, zzbgo<?, ?>> zzeea;
    private int zzcbc;
    private int zzeck;
    private Set<Integer> zzeeb;
    private String zzeem;
    private byte[] zzeen;
    private PendingIntent zzeeo;
    private DeviceMetaData zzeep;

    static {
        HashMap<String, zzbgo<?, ?>> hashMap = new HashMap<>();
        zzeea = hashMap;
        hashMap.put("accountType", zzbgo.zzl("accountType", 2));
        zzeea.put(NotificationCompat.CATEGORY_STATUS, zzbgo.zzj(NotificationCompat.CATEGORY_STATUS, 3));
        zzeea.put("transferBytes", zzbgo.zzn("transferBytes", 4));
    }

    public zzu() {
        this.zzeeb = new ArraySet(3);
        this.zzeck = 1;
    }

    zzu(Set<Integer> set, int i, String str, int i2, byte[] bArr, PendingIntent pendingIntent, DeviceMetaData deviceMetaData) {
        this.zzeeb = set;
        this.zzeck = i;
        this.zzeem = str;
        this.zzcbc = i2;
        this.zzeen = bArr;
        this.zzeeo = pendingIntent;
        this.zzeep = deviceMetaData;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        Set<Integer> set = this.zzeeb;
        if (set.contains(Integer.valueOf(1))) {
            zzbfp.zzc(parcel, 1, this.zzeck);
        }
        if (set.contains(Integer.valueOf(2))) {
            zzbfp.zza(parcel, 2, this.zzeem, true);
        }
        if (set.contains(Integer.valueOf(3))) {
            zzbfp.zzc(parcel, 3, this.zzcbc);
        }
        if (set.contains(Integer.valueOf(4))) {
            zzbfp.zza(parcel, 4, this.zzeen, true);
        }
        if (set.contains(Integer.valueOf(5))) {
            zzbfp.zza(parcel, 5, (Parcelable) this.zzeeo, i, true);
        }
        if (set.contains(Integer.valueOf(6))) {
            zzbfp.zza(parcel, 6, (Parcelable) this.zzeep, i, true);
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
        int i;
        switch (zzbgo.zzalu()) {
            case 1:
                i = this.zzeck;
                break;
            case 2:
                return this.zzeem;
            case 3:
                i = this.zzcbc;
                break;
            case 4:
                return this.zzeen;
            default:
                int zzalu = zzbgo.zzalu();
                StringBuilder sb = new StringBuilder(37);
                sb.append("Unknown SafeParcelable id=");
                sb.append(zzalu);
                throw new IllegalStateException(sb.toString());
        }
        return Integer.valueOf(i);
    }
}
