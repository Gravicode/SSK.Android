package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.zzbg;
import java.util.Arrays;

public final class zzbeh extends zzbfm {
    public static final Creator<zzbeh> CREATOR = new zzbei();
    public final zzbec zzfjk;
    private boolean zzfjq;
    public final zzfkq zzfjr;
    public zzbew zzfjx;
    public byte[] zzfjy;
    private int[] zzfjz;
    private String[] zzfka;
    private int[] zzfkb;
    private byte[][] zzfkc;
    private zzctx[] zzfkd;
    public final zzbec zzfke;

    public zzbeh(zzbew zzbew, zzfkq zzfkq, zzbec zzbec, zzbec zzbec2, int[] iArr, String[] strArr, int[] iArr2, byte[][] bArr, zzctx[] zzctxArr, boolean z) {
        this.zzfjx = zzbew;
        this.zzfjr = zzfkq;
        this.zzfjk = zzbec;
        this.zzfke = null;
        this.zzfjz = iArr;
        this.zzfka = null;
        this.zzfkb = iArr2;
        this.zzfkc = null;
        this.zzfkd = null;
        this.zzfjq = z;
    }

    zzbeh(zzbew zzbew, byte[] bArr, int[] iArr, String[] strArr, int[] iArr2, byte[][] bArr2, boolean z, zzctx[] zzctxArr) {
        this.zzfjx = zzbew;
        this.zzfjy = bArr;
        this.zzfjz = iArr;
        this.zzfka = strArr;
        this.zzfjr = null;
        this.zzfjk = null;
        this.zzfke = null;
        this.zzfkb = iArr2;
        this.zzfkc = bArr2;
        this.zzfkd = zzctxArr;
        this.zzfjq = z;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzbeh) {
            zzbeh zzbeh = (zzbeh) obj;
            return zzbg.equal(this.zzfjx, zzbeh.zzfjx) && Arrays.equals(this.zzfjy, zzbeh.zzfjy) && Arrays.equals(this.zzfjz, zzbeh.zzfjz) && Arrays.equals(this.zzfka, zzbeh.zzfka) && zzbg.equal(this.zzfjr, zzbeh.zzfjr) && zzbg.equal(this.zzfjk, zzbeh.zzfjk) && zzbg.equal(this.zzfke, zzbeh.zzfke) && Arrays.equals(this.zzfkb, zzbeh.zzfkb) && Arrays.deepEquals(this.zzfkc, zzbeh.zzfkc) && Arrays.equals(this.zzfkd, zzbeh.zzfkd) && this.zzfjq == zzbeh.zzfjq;
        }
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzfjx, this.zzfjy, this.zzfjz, this.zzfka, this.zzfjr, this.zzfjk, this.zzfke, this.zzfkb, this.zzfkc, this.zzfkd, Boolean.valueOf(this.zzfjq)});
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("LogEventParcelable[");
        sb.append(this.zzfjx);
        sb.append(", LogEventBytes: ");
        sb.append(this.zzfjy == null ? null : new String(this.zzfjy));
        sb.append(", TestCodes: ");
        sb.append(Arrays.toString(this.zzfjz));
        sb.append(", MendelPackages: ");
        sb.append(Arrays.toString(this.zzfka));
        sb.append(", LogEvent: ");
        sb.append(this.zzfjr);
        sb.append(", ExtensionProducer: ");
        sb.append(this.zzfjk);
        sb.append(", VeProducer: ");
        sb.append(this.zzfke);
        sb.append(", ExperimentIDs: ");
        sb.append(Arrays.toString(this.zzfkb));
        sb.append(", ExperimentTokens: ");
        sb.append(Arrays.toString(this.zzfkc));
        sb.append(", ExperimentTokensParcelables: ");
        sb.append(Arrays.toString(this.zzfkd));
        sb.append(", AddPhenotypeExperimentTokens: ");
        sb.append(this.zzfjq);
        sb.append("]");
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, (Parcelable) this.zzfjx, i, false);
        zzbfp.zza(parcel, 3, this.zzfjy, false);
        zzbfp.zza(parcel, 4, this.zzfjz, false);
        zzbfp.zza(parcel, 5, this.zzfka, false);
        zzbfp.zza(parcel, 6, this.zzfkb, false);
        zzbfp.zza(parcel, 7, this.zzfkc, false);
        zzbfp.zza(parcel, 8, this.zzfjq);
        zzbfp.zza(parcel, 9, (T[]) this.zzfkd, i, false);
        zzbfp.zzai(parcel, zze);
    }
}
