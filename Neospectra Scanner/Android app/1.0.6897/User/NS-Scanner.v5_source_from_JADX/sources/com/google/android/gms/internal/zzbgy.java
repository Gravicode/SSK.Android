package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzb;
import com.google.android.gms.common.util.zzo;
import com.google.android.gms.common.util.zzp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class zzbgy extends zzbgq {
    public static final Creator<zzbgy> CREATOR = new zzbgz();
    private final String mClassName;
    private final int zzeck;
    private final zzbgt zzgcm;
    private final Parcel zzgct;
    private final int zzgcu = 2;
    private int zzgcv;
    private int zzgcw;

    zzbgy(int i, Parcel parcel, zzbgt zzbgt) {
        this.zzeck = i;
        this.zzgct = (Parcel) zzbq.checkNotNull(parcel);
        this.zzgcm = zzbgt;
        this.mClassName = this.zzgcm == null ? null : this.zzgcm.zzalz();
        this.zzgcv = 2;
    }

    private static void zza(StringBuilder sb, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"");
                sb.append(zzo.zzgr(obj.toString()));
                sb.append("\"");
                return;
            case 8:
                sb.append("\"");
                sb.append(zzb.zzk((byte[]) obj));
                sb.append("\"");
                return;
            case 9:
                sb.append("\"");
                sb.append(zzb.zzl((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                zzp.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                StringBuilder sb2 = new StringBuilder(26);
                sb2.append("Unknown type = ");
                sb2.append(i);
                throw new IllegalArgumentException(sb2.toString());
        }
    }

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.math.BigInteger[]] */
    /* JADX WARNING: type inference failed for: r2v3, types: [double[]] */
    /* JADX WARNING: type inference failed for: r2v4, types: [double[]] */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], double[], java.math.BigInteger[]]
      uses: [java.lang.Object[], double[], ?[OBJECT, ARRAY][]]
      mth insns count: 159
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
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zza(java.lang.StringBuilder r6, com.google.android.gms.internal.zzbgo<?, ?> r7, android.os.Parcel r8, int r9) {
        /*
            r5 = this;
            boolean r0 = r7.zzgch
            r1 = 0
            if (r0 == 0) goto L_0x00cb
            java.lang.String r0 = "["
            r6.append(r0)
            int r0 = r7.zzgcg
            r2 = 0
            switch(r0) {
                case 0: goto L_0x00ab;
                case 1: goto L_0x0082;
                case 2: goto L_0x007a;
                case 3: goto L_0x0072;
                case 4: goto L_0x005b;
                case 5: goto L_0x0052;
                case 6: goto L_0x0049;
                case 7: goto L_0x0040;
                case 8: goto L_0x0038;
                case 9: goto L_0x0038;
                case 10: goto L_0x0038;
                case 11: goto L_0x0018;
                default: goto L_0x0010;
            }
        L_0x0010:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "Unknown field type out."
            r6.<init>(r7)
            throw r6
        L_0x0018:
            android.os.Parcel[] r8 = com.google.android.gms.internal.zzbfn.zzae(r8, r9)
            int r9 = r8.length
            r0 = 0
        L_0x001e:
            if (r0 >= r9) goto L_0x00c5
            if (r0 <= 0) goto L_0x0027
            java.lang.String r2 = ","
            r6.append(r2)
        L_0x0027:
            r2 = r8[r0]
            r2.setDataPosition(r1)
            java.util.Map r2 = r7.zzalx()
            r3 = r8[r0]
            r5.zza(r6, r2, r3)
            int r0 = r0 + 1
            goto L_0x001e
        L_0x0038:
            java.lang.UnsupportedOperationException r6 = new java.lang.UnsupportedOperationException
            java.lang.String r7 = "List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported"
            r6.<init>(r7)
            throw r6
        L_0x0040:
            java.lang.String[] r7 = com.google.android.gms.internal.zzbfn.zzaa(r8, r9)
            com.google.android.gms.common.util.zza.zza(r6, r7)
            goto L_0x00c5
        L_0x0049:
            boolean[] r7 = com.google.android.gms.internal.zzbfn.zzv(r8, r9)
            com.google.android.gms.common.util.zza.zza(r6, r7)
            goto L_0x00c5
        L_0x0052:
            java.math.BigDecimal[] r7 = com.google.android.gms.internal.zzbfn.zzz(r8, r9)
            com.google.android.gms.common.util.zza.zza(r6, (T[]) r7)
            goto L_0x00c5
        L_0x005b:
            int r7 = com.google.android.gms.internal.zzbfn.zza(r8, r9)
            int r9 = r8.dataPosition()
            if (r7 != 0) goto L_0x0066
            goto L_0x006e
        L_0x0066:
            double[] r2 = r8.createDoubleArray()
            int r9 = r9 + r7
            r8.setDataPosition(r9)
        L_0x006e:
            com.google.android.gms.common.util.zza.zza(r6, r2)
            goto L_0x00c5
        L_0x0072:
            float[] r7 = com.google.android.gms.internal.zzbfn.zzy(r8, r9)
            com.google.android.gms.common.util.zza.zza(r6, r7)
            goto L_0x00c5
        L_0x007a:
            long[] r7 = com.google.android.gms.internal.zzbfn.zzx(r8, r9)
            com.google.android.gms.common.util.zza.zza(r6, r7)
            goto L_0x00c5
        L_0x0082:
            int r7 = com.google.android.gms.internal.zzbfn.zza(r8, r9)
            int r9 = r8.dataPosition()
            if (r7 != 0) goto L_0x008d
            goto L_0x00a7
        L_0x008d:
            int r0 = r8.readInt()
            java.math.BigInteger[] r2 = new java.math.BigInteger[r0]
        L_0x0093:
            if (r1 >= r0) goto L_0x00a3
            java.math.BigInteger r3 = new java.math.BigInteger
            byte[] r4 = r8.createByteArray()
            r3.<init>(r4)
            r2[r1] = r3
            int r1 = r1 + 1
            goto L_0x0093
        L_0x00a3:
            int r9 = r9 + r7
            r8.setDataPosition(r9)
        L_0x00a7:
            com.google.android.gms.common.util.zza.zza(r6, (T[]) r2)
            goto L_0x00c5
        L_0x00ab:
            int[] r7 = com.google.android.gms.internal.zzbfn.zzw(r8, r9)
            int r8 = r7.length
        L_0x00b0:
            if (r1 >= r8) goto L_0x00c5
            if (r1 == 0) goto L_0x00b9
            java.lang.String r9 = ","
            r6.append(r9)
        L_0x00b9:
            r9 = r7[r1]
            java.lang.String r9 = java.lang.Integer.toString(r9)
            r6.append(r9)
            int r1 = r1 + 1
            goto L_0x00b0
        L_0x00c5:
            java.lang.String r7 = "]"
            r6.append(r7)
            return
        L_0x00cb:
            int r0 = r7.zzgcg
            switch(r0) {
                case 0: goto L_0x01b2;
                case 1: goto L_0x01aa;
                case 2: goto L_0x01a2;
                case 3: goto L_0x019a;
                case 4: goto L_0x0192;
                case 5: goto L_0x018a;
                case 6: goto L_0x0182;
                case 7: goto L_0x016c;
                case 8: goto L_0x0156;
                case 9: goto L_0x0140;
                case 10: goto L_0x00e7;
                case 11: goto L_0x00d8;
                default: goto L_0x00d0;
            }
        L_0x00d0:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "Unknown field type out"
            r6.<init>(r7)
            throw r6
        L_0x00d8:
            android.os.Parcel r8 = com.google.android.gms.internal.zzbfn.zzad(r8, r9)
            r8.setDataPosition(r1)
            java.util.Map r7 = r7.zzalx()
            r5.zza(r6, r7, r8)
            return
        L_0x00e7:
            android.os.Bundle r7 = com.google.android.gms.internal.zzbfn.zzs(r8, r9)
            java.util.Set r8 = r7.keySet()
            r8.size()
            java.lang.String r9 = "{"
            r6.append(r9)
            java.util.Iterator r8 = r8.iterator()
            r9 = 1
        L_0x00fd:
            boolean r0 = r8.hasNext()
            if (r0 == 0) goto L_0x0139
            java.lang.Object r0 = r8.next()
            java.lang.String r0 = (java.lang.String) r0
            if (r9 != 0) goto L_0x0110
            java.lang.String r9 = ","
            r6.append(r9)
        L_0x0110:
            java.lang.String r9 = "\""
            r6.append(r9)
            r6.append(r0)
            java.lang.String r9 = "\""
            r6.append(r9)
            java.lang.String r9 = ":"
            r6.append(r9)
            java.lang.String r9 = "\""
            r6.append(r9)
            java.lang.String r9 = r7.getString(r0)
            java.lang.String r9 = com.google.android.gms.common.util.zzo.zzgr(r9)
            r6.append(r9)
            java.lang.String r9 = "\""
            r6.append(r9)
            r9 = 0
            goto L_0x00fd
        L_0x0139:
            java.lang.String r7 = "}"
            r6.append(r7)
            return
        L_0x0140:
            byte[] r7 = com.google.android.gms.internal.zzbfn.zzt(r8, r9)
            java.lang.String r8 = "\""
            r6.append(r8)
            java.lang.String r7 = com.google.android.gms.common.util.zzb.zzl(r7)
            r6.append(r7)
            java.lang.String r7 = "\""
            r6.append(r7)
            return
        L_0x0156:
            byte[] r7 = com.google.android.gms.internal.zzbfn.zzt(r8, r9)
            java.lang.String r8 = "\""
            r6.append(r8)
            java.lang.String r7 = com.google.android.gms.common.util.zzb.zzk(r7)
            r6.append(r7)
            java.lang.String r7 = "\""
            r6.append(r7)
            return
        L_0x016c:
            java.lang.String r7 = com.google.android.gms.internal.zzbfn.zzq(r8, r9)
            java.lang.String r8 = "\""
            r6.append(r8)
            java.lang.String r7 = com.google.android.gms.common.util.zzo.zzgr(r7)
            r6.append(r7)
            java.lang.String r7 = "\""
            r6.append(r7)
            return
        L_0x0182:
            boolean r7 = com.google.android.gms.internal.zzbfn.zzc(r8, r9)
            r6.append(r7)
            return
        L_0x018a:
            java.math.BigDecimal r7 = com.google.android.gms.internal.zzbfn.zzp(r8, r9)
            r6.append(r7)
            return
        L_0x0192:
            double r7 = com.google.android.gms.internal.zzbfn.zzn(r8, r9)
            r6.append(r7)
            return
        L_0x019a:
            float r7 = com.google.android.gms.internal.zzbfn.zzl(r8, r9)
            r6.append(r7)
            return
        L_0x01a2:
            long r7 = com.google.android.gms.internal.zzbfn.zzi(r8, r9)
            r6.append(r7)
            return
        L_0x01aa:
            java.math.BigInteger r7 = com.google.android.gms.internal.zzbfn.zzk(r8, r9)
            r6.append(r7)
            return
        L_0x01b2:
            int r7 = com.google.android.gms.internal.zzbfn.zzg(r8, r9)
            r6.append(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbgy.zza(java.lang.StringBuilder, com.google.android.gms.internal.zzbgo, android.os.Parcel, int):void");
    }

    private final void zza(StringBuilder sb, Map<String, zzbgo<?, ?>> map, Parcel parcel) {
        Object obj;
        SparseArray sparseArray = new SparseArray();
        for (Entry entry : map.entrySet()) {
            sparseArray.put(((zzbgo) entry.getValue()).zzgcj, entry);
        }
        sb.append('{');
        int zzd = zzbfn.zzd(parcel);
        boolean z = false;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            Entry entry2 = (Entry) sparseArray.get(65535 & readInt);
            if (entry2 != null) {
                if (z) {
                    sb.append(",");
                }
                String str = (String) entry2.getKey();
                zzbgo zzbgo = (zzbgo) entry2.getValue();
                sb.append("\"");
                sb.append(str);
                sb.append("\":");
                if (zzbgo.zzalw()) {
                    switch (zzbgo.zzgcg) {
                        case 0:
                            obj = Integer.valueOf(zzbfn.zzg(parcel, readInt));
                            break;
                        case 1:
                            obj = zzbfn.zzk(parcel, readInt);
                            break;
                        case 2:
                            obj = Long.valueOf(zzbfn.zzi(parcel, readInt));
                            break;
                        case 3:
                            obj = Float.valueOf(zzbfn.zzl(parcel, readInt));
                            break;
                        case 4:
                            obj = Double.valueOf(zzbfn.zzn(parcel, readInt));
                            break;
                        case 5:
                            obj = zzbfn.zzp(parcel, readInt);
                            break;
                        case 6:
                            obj = Boolean.valueOf(zzbfn.zzc(parcel, readInt));
                            break;
                        case 7:
                            obj = zzbfn.zzq(parcel, readInt);
                            break;
                        case 8:
                        case 9:
                            obj = zzbfn.zzt(parcel, readInt);
                            break;
                        case 10:
                            obj = zzl(zzbfn.zzs(parcel, readInt));
                            break;
                        case 11:
                            throw new IllegalArgumentException("Method does not accept concrete type.");
                        default:
                            int i = zzbgo.zzgcg;
                            StringBuilder sb2 = new StringBuilder(36);
                            sb2.append("Unknown field out type = ");
                            sb2.append(i);
                            throw new IllegalArgumentException(sb2.toString());
                    }
                    zzb(sb, zzbgo, zza(zzbgo, obj));
                } else {
                    zza(sb, zzbgo, parcel, readInt);
                }
                z = true;
            }
        }
        if (parcel.dataPosition() != zzd) {
            StringBuilder sb3 = new StringBuilder(37);
            sb3.append("Overread allowed size end=");
            sb3.append(zzd);
            throw new zzbfo(sb3.toString(), parcel);
        }
        sb.append('}');
    }

    private Parcel zzamb() {
        switch (this.zzgcv) {
            case 0:
                this.zzgcw = zzbfp.zze(this.zzgct);
                break;
            case 1:
                break;
        }
        zzbfp.zzai(this.zzgct, this.zzgcw);
        this.zzgcv = 2;
        return this.zzgct;
    }

    private final void zzb(StringBuilder sb, zzbgo<?, ?> zzbgo, Object obj) {
        if (zzbgo.zzgcf) {
            ArrayList arrayList = (ArrayList) obj;
            sb.append("[");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                zza(sb, zzbgo.zzgce, arrayList.get(i));
            }
            sb.append("]");
            return;
        }
        zza(sb, zzbgo.zzgce, obj);
    }

    private static HashMap<String, String> zzl(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    public String toString() {
        zzbq.checkNotNull(this.zzgcm, "Cannot convert to JSON on client side.");
        Parcel zzamb = zzamb();
        zzamb.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzgcm.zzgq(this.mClassName), zzamb);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzbgt zzbgt;
        int zze = zzbfp.zze(parcel);
        zzbfp.zzc(parcel, 1, this.zzeck);
        zzbfp.zza(parcel, 2, zzamb(), false);
        switch (this.zzgcu) {
            case 0:
                zzbgt = null;
                break;
            case 1:
            case 2:
                zzbgt = this.zzgcm;
                break;
            default:
                int i2 = this.zzgcu;
                StringBuilder sb = new StringBuilder(34);
                sb.append("Invalid creation type: ");
                sb.append(i2);
                throw new IllegalStateException(sb.toString());
        }
        zzbfp.zza(parcel, 3, (Parcelable) zzbgt, i, false);
        zzbfp.zzai(parcel, zze);
    }

    public final Map<String, zzbgo<?, ?>> zzaav() {
        if (this.zzgcm == null) {
            return null;
        }
        return this.zzgcm.zzgq(this.mClassName);
    }

    public final Object zzgo(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public final boolean zzgp(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }
}
