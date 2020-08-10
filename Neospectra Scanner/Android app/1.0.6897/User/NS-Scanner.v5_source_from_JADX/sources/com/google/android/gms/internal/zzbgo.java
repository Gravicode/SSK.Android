package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbi;
import com.google.android.gms.common.internal.zzbq;
import java.util.ArrayList;
import java.util.Map;

public final class zzbgo<I, O> extends zzbfm {
    public static final zzbgr CREATOR = new zzbgr();
    private final int zzeck;
    protected final int zzgce;
    protected final boolean zzgcf;
    protected final int zzgcg;
    protected final boolean zzgch;
    protected final String zzgci;
    protected final int zzgcj;
    protected final Class<? extends zzbgn> zzgck;
    private String zzgcl;
    private zzbgt zzgcm;
    /* access modifiers changed from: private */
    public zzbgp<I, O> zzgcn;

    zzbgo(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, zzbgh zzbgh) {
        this.zzeck = i;
        this.zzgce = i2;
        this.zzgcf = z;
        this.zzgcg = i3;
        this.zzgch = z2;
        this.zzgci = str;
        this.zzgcj = i4;
        if (str2 == null) {
            this.zzgck = null;
            this.zzgcl = null;
        } else {
            this.zzgck = zzbgy.class;
            this.zzgcl = str2;
        }
        if (zzbgh == null) {
            this.zzgcn = null;
        } else {
            this.zzgcn = zzbgh.zzalt();
        }
    }

    private zzbgo(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends zzbgn> cls, zzbgp<I, O> zzbgp) {
        this.zzeck = 1;
        this.zzgce = i;
        this.zzgcf = z;
        this.zzgcg = i2;
        this.zzgch = z2;
        this.zzgci = str;
        this.zzgcj = i3;
        this.zzgck = cls;
        this.zzgcl = cls == null ? null : cls.getCanonicalName();
        this.zzgcn = zzbgp;
    }

    public static zzbgo zza(String str, int i, zzbgp<?, ?> zzbgp, boolean z) {
        zzbgo zzbgo = new zzbgo(7, false, 0, false, str, i, null, zzbgp);
        return zzbgo;
    }

    public static <T extends zzbgn> zzbgo<T, T> zza(String str, int i, Class<T> cls) {
        zzbgo zzbgo = new zzbgo(11, false, 11, false, str, i, cls, null);
        return zzbgo;
    }

    private String zzalv() {
        if (this.zzgcl == null) {
            return null;
        }
        return this.zzgcl;
    }

    public static <T extends zzbgn> zzbgo<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
        zzbgo zzbgo = new zzbgo(11, true, 11, true, str, i, cls, null);
        return zzbgo;
    }

    public static zzbgo<Integer, Integer> zzj(String str, int i) {
        zzbgo zzbgo = new zzbgo(0, false, 0, false, str, i, null, null);
        return zzbgo;
    }

    public static zzbgo<Boolean, Boolean> zzk(String str, int i) {
        zzbgo zzbgo = new zzbgo(6, false, 6, false, str, i, null, null);
        return zzbgo;
    }

    public static zzbgo<String, String> zzl(String str, int i) {
        zzbgo zzbgo = new zzbgo(7, false, 7, false, str, i, null, null);
        return zzbgo;
    }

    public static zzbgo<ArrayList<String>, ArrayList<String>> zzm(String str, int i) {
        zzbgo zzbgo = new zzbgo(7, true, 7, true, str, i, null, null);
        return zzbgo;
    }

    public static zzbgo<byte[], byte[]> zzn(String str, int i) {
        zzbgo zzbgo = new zzbgo(8, false, 8, false, str, 4, null, null);
        return zzbgo;
    }

    public final I convertBack(O o) {
        return this.zzgcn.convertBack(o);
    }

    public final String toString() {
        zzbi zzg = zzbg.zzx(this).zzg("versionCode", Integer.valueOf(this.zzeck)).zzg("typeIn", Integer.valueOf(this.zzgce)).zzg("typeInArray", Boolean.valueOf(this.zzgcf)).zzg("typeOut", Integer.valueOf(this.zzgcg)).zzg("typeOutArray", Boolean.valueOf(this.zzgch)).zzg("outputFieldName", this.zzgci).zzg("safeParcelFieldId", Integer.valueOf(this.zzgcj)).zzg("concreteTypeName", zzalv());
        Class<? extends zzbgn> cls = this.zzgck;
        if (cls != null) {
            zzg.zzg("concreteType.class", cls.getCanonicalName());
        }
        if (this.zzgcn != null) {
            zzg.zzg("converterName", this.zzgcn.getClass().getCanonicalName());
        }
        return zzg.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.zzeck);
        zzbfp.zzc(parcel, 2, this.zzgce);
        zzbfp.zza(parcel, 3, this.zzgcf);
        zzbfp.zzc(parcel, 4, this.zzgcg);
        zzbfp.zza(parcel, 5, this.zzgch);
        zzbfp.zza(parcel, 6, this.zzgci, false);
        zzbfp.zzc(parcel, 7, this.zzgcj);
        zzbfp.zza(parcel, 8, zzalv(), false);
        zzbfp.zza(parcel, 9, (Parcelable) this.zzgcn == null ? null : zzbgh.zza(this.zzgcn), i, false);
        zzbfp.zzai(parcel, zze);
    }

    public final void zza(zzbgt zzbgt) {
        this.zzgcm = zzbgt;
    }

    public final int zzalu() {
        return this.zzgcj;
    }

    public final boolean zzalw() {
        return this.zzgcn != null;
    }

    public final Map<String, zzbgo<?, ?>> zzalx() {
        zzbq.checkNotNull(this.zzgcl);
        zzbq.checkNotNull(this.zzgcm);
        return this.zzgcm.zzgq(this.zzgcl);
    }
}
