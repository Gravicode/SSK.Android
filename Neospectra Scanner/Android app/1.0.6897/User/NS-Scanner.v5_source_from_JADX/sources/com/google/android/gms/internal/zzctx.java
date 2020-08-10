package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Base64;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class zzctx extends zzbfm {
    public static final Creator<zzctx> CREATOR = new zzcud();
    private static byte[][] zzfiz = new byte[0][];
    private static zzctx zzjwk;
    private static final zzcuc zzjwt = new zzcty();
    private static final zzcuc zzjwu = new zzctz();
    private static final zzcuc zzjwv = new zzcua();
    private static final zzcuc zzjww = new zzcub();
    private String zzjwl;
    private byte[] zzjwm;
    private byte[][] zzjwn;
    private byte[][] zzjwo;
    private byte[][] zzjwp;
    private byte[][] zzjwq;
    private int[] zzjwr;
    private byte[][] zzjws;

    /* JADX WARNING: type inference failed for: r0v4, types: [com.google.android.gms.internal.zzcuc, com.google.android.gms.internal.zzcty] */
    /* JADX WARNING: type inference failed for: r0v5, types: [com.google.android.gms.internal.zzcuc, com.google.android.gms.internal.zzctz] */
    /* JADX WARNING: type inference failed for: r0v6, types: [com.google.android.gms.internal.zzcuc, com.google.android.gms.internal.zzcua] */
    /* JADX WARNING: type inference failed for: r0v7, types: [com.google.android.gms.internal.zzcuc, com.google.android.gms.internal.zzcub] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v4, types: [com.google.android.gms.internal.zzcuc, com.google.android.gms.internal.zzcty]
      assigns: [com.google.android.gms.internal.zzcty]
      uses: [com.google.android.gms.internal.zzcuc]
      mth insns count: 20
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 4 */
    static {
        /*
            com.google.android.gms.internal.zzcud r0 = new com.google.android.gms.internal.zzcud
            r0.<init>()
            CREATOR = r0
            r0 = 0
            byte[][] r0 = new byte[r0][]
            zzfiz = r0
            com.google.android.gms.internal.zzctx r0 = new com.google.android.gms.internal.zzctx
            java.lang.String r2 = ""
            byte[][] r4 = zzfiz
            byte[][] r5 = zzfiz
            byte[][] r6 = zzfiz
            byte[][] r7 = zzfiz
            r3 = 0
            r8 = 0
            r9 = 0
            r1 = r0
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            zzjwk = r0
            com.google.android.gms.internal.zzcty r0 = new com.google.android.gms.internal.zzcty
            r0.<init>()
            zzjwt = r0
            com.google.android.gms.internal.zzctz r0 = new com.google.android.gms.internal.zzctz
            r0.<init>()
            zzjwu = r0
            com.google.android.gms.internal.zzcua r0 = new com.google.android.gms.internal.zzcua
            r0.<init>()
            zzjwv = r0
            com.google.android.gms.internal.zzcub r0 = new com.google.android.gms.internal.zzcub
            r0.<init>()
            zzjww = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzctx.<clinit>():void");
    }

    public zzctx(String str, byte[] bArr, byte[][] bArr2, byte[][] bArr3, byte[][] bArr4, byte[][] bArr5, int[] iArr, byte[][] bArr6) {
        this.zzjwl = str;
        this.zzjwm = bArr;
        this.zzjwn = bArr2;
        this.zzjwo = bArr3;
        this.zzjwp = bArr4;
        this.zzjwq = bArr5;
        this.zzjwr = iArr;
        this.zzjws = bArr6;
    }

    private static void zza(StringBuilder sb, String str, int[] iArr) {
        String str2;
        sb.append(str);
        sb.append("=");
        if (iArr == null) {
            str2 = "null";
        } else {
            sb.append("(");
            int length = iArr.length;
            int i = 0;
            boolean z = true;
            while (i < length) {
                int i2 = iArr[i];
                if (!z) {
                    sb.append(", ");
                }
                sb.append(i2);
                i++;
                z = false;
            }
            str2 = ")";
        }
        sb.append(str2);
    }

    private static void zza(StringBuilder sb, String str, byte[][] bArr) {
        String str2;
        sb.append(str);
        sb.append("=");
        if (bArr == null) {
            str2 = "null";
        } else {
            sb.append("(");
            int length = bArr.length;
            int i = 0;
            boolean z = true;
            while (i < length) {
                byte[] bArr2 = bArr[i];
                if (!z) {
                    sb.append(", ");
                }
                sb.append("'");
                sb.append(Base64.encodeToString(bArr2, 3));
                sb.append("'");
                i++;
                z = false;
            }
            str2 = ")";
        }
        sb.append(str2);
    }

    private static List<String> zzb(byte[][] bArr) {
        if (bArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(bArr.length);
        for (byte[] encodeToString : bArr) {
            arrayList.add(Base64.encodeToString(encodeToString, 3));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static List<Integer> zzd(int[] iArr) {
        if (iArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(iArr.length);
        for (int valueOf : iArr) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof zzctx) {
            zzctx zzctx = (zzctx) obj;
            if (zzcuh.equals(this.zzjwl, zzctx.zzjwl) && Arrays.equals(this.zzjwm, zzctx.zzjwm) && zzcuh.equals(zzb(this.zzjwn), zzb(zzctx.zzjwn)) && zzcuh.equals(zzb(this.zzjwo), zzb(zzctx.zzjwo)) && zzcuh.equals(zzb(this.zzjwp), zzb(zzctx.zzjwp)) && zzcuh.equals(zzb(this.zzjwq), zzb(zzctx.zzjwq)) && zzcuh.equals(zzd(this.zzjwr), zzd(zzctx.zzjwr)) && zzcuh.equals(zzb(this.zzjws), zzb(zzctx.zzjws))) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder("ExperimentTokens");
        sb.append("(");
        if (this.zzjwl == null) {
            str = "null";
        } else {
            String str3 = "'";
            String str4 = this.zzjwl;
            String str5 = "'";
            StringBuilder sb2 = new StringBuilder(String.valueOf(str3).length() + String.valueOf(str4).length() + String.valueOf(str5).length());
            sb2.append(str3);
            sb2.append(str4);
            sb2.append(str5);
            str = sb2.toString();
        }
        sb.append(str);
        sb.append(", ");
        byte[] bArr = this.zzjwm;
        sb.append("direct");
        sb.append("=");
        if (bArr == null) {
            str2 = "null";
        } else {
            sb.append("'");
            sb.append(Base64.encodeToString(bArr, 3));
            str2 = "'";
        }
        sb.append(str2);
        sb.append(", ");
        zza(sb, "GAIA", this.zzjwn);
        sb.append(", ");
        zza(sb, "PSEUDO", this.zzjwo);
        sb.append(", ");
        zza(sb, "ALWAYS", this.zzjwp);
        sb.append(", ");
        zza(sb, "OTHER", this.zzjwq);
        sb.append(", ");
        zza(sb, "weak", this.zzjwr);
        sb.append(", ");
        zza(sb, "directs", this.zzjws);
        sb.append(")");
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.zzjwl, false);
        zzbfp.zza(parcel, 3, this.zzjwm, false);
        zzbfp.zza(parcel, 4, this.zzjwn, false);
        zzbfp.zza(parcel, 5, this.zzjwo, false);
        zzbfp.zza(parcel, 6, this.zzjwp, false);
        zzbfp.zza(parcel, 7, this.zzjwq, false);
        zzbfp.zza(parcel, 8, this.zzjwr, false);
        zzbfp.zza(parcel, 9, this.zzjws, false);
        zzbfp.zzai(parcel, zze);
    }
}
