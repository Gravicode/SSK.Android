package com.google.android.gms.internal;

public abstract class zzbgq extends zzbgn implements zzbfq {
    public final int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!getClass().isInstance(obj)) {
            return false;
        }
        zzbgn zzbgn = (zzbgn) obj;
        for (zzbgo zzbgo : zzaav().values()) {
            if (zza(zzbgo)) {
                if (!zzbgn.zza(zzbgo) || !zzb(zzbgo).equals(zzbgn.zzb(zzbgo))) {
                    return false;
                }
            } else if (zzbgn.zza(zzbgo)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        for (zzbgo zzbgo : zzaav().values()) {
            if (zza(zzbgo)) {
                i = (i * 31) + zzb(zzbgo).hashCode();
            }
        }
        return i;
    }

    public Object zzgo(String str) {
        return null;
    }

    public boolean zzgp(String str) {
        return false;
    }
}
