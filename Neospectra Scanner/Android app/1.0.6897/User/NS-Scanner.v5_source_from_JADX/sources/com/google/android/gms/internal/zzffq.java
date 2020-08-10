package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzffq<FieldDescriptorType extends zzffs<FieldDescriptorType>> {
    private static final zzffq zzpgm = new zzffq(true);
    private boolean zzktj;
    private final zzfhy<FieldDescriptorType, Object> zzpgk = zzfhy.zzma(16);
    private boolean zzpgl = false;

    private zzffq() {
    }

    private zzffq(boolean z) {
        if (!this.zzktj) {
            this.zzpgk.zzbiy();
            this.zzktj = true;
        }
    }

    static int zza(zzfiy zzfiy, int i, Object obj) {
        int zzlg = zzffg.zzlg(i);
        if (zzfiy == zzfiy.GROUP) {
            zzffz.zzh((zzfhe) obj);
            zzlg <<= 1;
        }
        return zzlg + zzb(zzfiy, obj);
    }

    public static Object zza(zzffb zzffb, zzfiy zzfiy, boolean z) throws IOException {
        zzfje zzfje = zzfje.STRICT;
        switch (zzfix.zzpgo[zzfiy.ordinal()]) {
            case 1:
                return Double.valueOf(zzffb.readDouble());
            case 2:
                return Float.valueOf(zzffb.readFloat());
            case 3:
                return Long.valueOf(zzffb.zzcvv());
            case 4:
                return Long.valueOf(zzffb.zzcvu());
            case 5:
                return Integer.valueOf(zzffb.zzcvw());
            case 6:
                return Long.valueOf(zzffb.zzcvx());
            case 7:
                return Integer.valueOf(zzffb.zzcvy());
            case 8:
                return Boolean.valueOf(zzffb.zzcvz());
            case 9:
                return zzffb.zzcwb();
            case 10:
                return Integer.valueOf(zzffb.zzcwc());
            case 11:
                return Integer.valueOf(zzffb.zzcwe());
            case 12:
                return Long.valueOf(zzffb.zzcwf());
            case 13:
                return Integer.valueOf(zzffb.zzcwg());
            case 14:
                return Long.valueOf(zzffb.zzcwh());
            case 15:
                return zzfje.zza(zzffb);
            case 16:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle nested groups.");
            case 17:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle embedded messages.");
            case 18:
                throw new IllegalArgumentException("readPrimitiveField() cannot handle enums.");
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    static void zza(zzffg zzffg, zzfiy zzfiy, int i, Object obj) throws IOException {
        if (zzfiy == zzfiy.GROUP) {
            zzfhe zzfhe = (zzfhe) obj;
            zzffz.zzh(zzfhe);
            zzffg.zze(i, zzfhe);
            return;
        }
        zzffg.zzz(i, zzfiy.zzdae());
        switch (zzffr.zzpgo[zzfiy.ordinal()]) {
            case 1:
                zzffg.zzn(((Double) obj).doubleValue());
                return;
            case 2:
                zzffg.zzf(((Float) obj).floatValue());
                return;
            case 3:
                zzffg.zzct(((Long) obj).longValue());
                return;
            case 4:
                zzffg.zzct(((Long) obj).longValue());
                return;
            case 5:
                zzffg.zzlc(((Integer) obj).intValue());
                return;
            case 6:
                zzffg.zzcv(((Long) obj).longValue());
                return;
            case 7:
                zzffg.zzlf(((Integer) obj).intValue());
                return;
            case 8:
                zzffg.zzdd(((Boolean) obj).booleanValue());
                return;
            case 9:
                ((zzfhe) obj).zza(zzffg);
                return;
            case 10:
                zzffg.zze((zzfhe) obj);
                return;
            case 11:
                if (obj instanceof zzfes) {
                    zzffg.zzay((zzfes) obj);
                    return;
                } else {
                    zzffg.zzts((String) obj);
                    return;
                }
            case 12:
                if (obj instanceof zzfes) {
                    zzffg.zzay((zzfes) obj);
                    return;
                }
                byte[] bArr = (byte[]) obj;
                zzffg.zzi(bArr, 0, bArr.length);
                return;
            case 13:
                zzffg.zzld(((Integer) obj).intValue());
                return;
            case 14:
                zzffg.zzlf(((Integer) obj).intValue());
                return;
            case 15:
                zzffg.zzcv(((Long) obj).longValue());
                return;
            case 16:
                zzffg.zzle(((Integer) obj).intValue());
                return;
            case 17:
                zzffg.zzcu(((Long) obj).longValue());
                return;
            case 18:
                if (obj instanceof zzfga) {
                    zzffg.zzlc(((zzfga) obj).zzhq());
                    return;
                } else {
                    zzffg.zzlc(((Integer) obj).intValue());
                    return;
                }
            default:
                return;
        }
    }

    private void zza(FieldDescriptorType fielddescriptortype, Object obj) {
        if (!fielddescriptortype.zzcxj()) {
            zza(fielddescriptortype.zzcxh(), obj);
            r7 = obj;
        } else if (!(obj instanceof List)) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList2.get(i);
                i++;
                zza(fielddescriptortype.zzcxh(), obj2);
            }
            r7 = arrayList;
        }
        if (r7 instanceof zzfgg) {
            this.zzpgl = true;
        }
        this.zzpgk.put(fielddescriptortype, r7);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0026, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        if ((r3 instanceof byte[]) == false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x001b, code lost:
        if ((r3 instanceof com.google.android.gms.internal.zzfgg) == false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        if ((r3 instanceof com.google.android.gms.internal.zzfga) == false) goto L_0x0043;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zza(com.google.android.gms.internal.zzfiy r2, java.lang.Object r3) {
        /*
            com.google.android.gms.internal.zzffz.checkNotNull(r3)
            int[] r0 = com.google.android.gms.internal.zzffr.zzpgn
            com.google.android.gms.internal.zzfjd r2 = r2.zzdad()
            int r2 = r2.ordinal()
            r2 = r0[r2]
            r0 = 1
            r1 = 0
            switch(r2) {
                case 1: goto L_0x0040;
                case 2: goto L_0x003d;
                case 3: goto L_0x003a;
                case 4: goto L_0x0037;
                case 5: goto L_0x0034;
                case 6: goto L_0x0031;
                case 7: goto L_0x0028;
                case 8: goto L_0x001e;
                case 9: goto L_0x0015;
                default: goto L_0x0014;
            }
        L_0x0014:
            goto L_0x0043
        L_0x0015:
            boolean r2 = r3 instanceof com.google.android.gms.internal.zzfhe
            if (r2 != 0) goto L_0x0026
            boolean r2 = r3 instanceof com.google.android.gms.internal.zzfgg
            if (r2 == 0) goto L_0x0043
            goto L_0x0026
        L_0x001e:
            boolean r2 = r3 instanceof java.lang.Integer
            if (r2 != 0) goto L_0x0026
            boolean r2 = r3 instanceof com.google.android.gms.internal.zzfga
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            r1 = 1
            goto L_0x0043
        L_0x0028:
            boolean r2 = r3 instanceof com.google.android.gms.internal.zzfes
            if (r2 != 0) goto L_0x0026
            boolean r2 = r3 instanceof byte[]
            if (r2 == 0) goto L_0x0043
            goto L_0x0026
        L_0x0031:
            boolean r0 = r3 instanceof java.lang.String
            goto L_0x0042
        L_0x0034:
            boolean r0 = r3 instanceof java.lang.Boolean
            goto L_0x0042
        L_0x0037:
            boolean r0 = r3 instanceof java.lang.Double
            goto L_0x0042
        L_0x003a:
            boolean r0 = r3 instanceof java.lang.Float
            goto L_0x0042
        L_0x003d:
            boolean r0 = r3 instanceof java.lang.Long
            goto L_0x0042
        L_0x0040:
            boolean r0 = r3 instanceof java.lang.Integer
        L_0x0042:
            r1 = r0
        L_0x0043:
            if (r1 != 0) goto L_0x004d
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Wrong object type used with protocol message reflection."
            r2.<init>(r3)
            throw r2
        L_0x004d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzffq.zza(com.google.android.gms.internal.zzfiy, java.lang.Object):void");
    }

    private static int zzb(zzffs<?> zzffs, Object obj) {
        zzfiy zzcxh = zzffs.zzcxh();
        int zzhq = zzffs.zzhq();
        if (!zzffs.zzcxj()) {
            return zza(zzcxh, zzhq, obj);
        }
        int i = 0;
        if (zzffs.zzcxk()) {
            for (Object zzb : (List) obj) {
                i += zzb(zzcxh, zzb);
            }
            return zzffg.zzlg(zzhq) + i + zzffg.zzlp(i);
        }
        for (Object zza : (List) obj) {
            i += zza(zzcxh, zzhq, zza);
        }
        return i;
    }

    private static int zzb(zzfiy zzfiy, Object obj) {
        switch (zzffr.zzpgo[zzfiy.ordinal()]) {
            case 1:
                return zzffg.zzo(((Double) obj).doubleValue());
            case 2:
                return zzffg.zzg(((Float) obj).floatValue());
            case 3:
                return zzffg.zzcw(((Long) obj).longValue());
            case 4:
                return zzffg.zzcx(((Long) obj).longValue());
            case 5:
                return zzffg.zzlh(((Integer) obj).intValue());
            case 6:
                return zzffg.zzcz(((Long) obj).longValue());
            case 7:
                return zzffg.zzlk(((Integer) obj).intValue());
            case 8:
                return zzffg.zzde(((Boolean) obj).booleanValue());
            case 9:
                return zzffg.zzg((zzfhe) obj);
            case 10:
                return obj instanceof zzfgg ? zzffg.zza((zzfgg) obj) : zzffg.zzf((zzfhe) obj);
            case 11:
                return obj instanceof zzfes ? zzffg.zzaz((zzfes) obj) : zzffg.zztt((String) obj);
            case 12:
                return obj instanceof zzfes ? zzffg.zzaz((zzfes) obj) : zzffg.zzbd((byte[]) obj);
            case 13:
                return zzffg.zzli(((Integer) obj).intValue());
            case 14:
                return zzffg.zzll(((Integer) obj).intValue());
            case 15:
                return zzffg.zzda(((Long) obj).longValue());
            case 16:
                return zzffg.zzlj(((Integer) obj).intValue());
            case 17:
                return zzffg.zzcy(((Long) obj).longValue());
            case 18:
                return obj instanceof zzfga ? zzffg.zzlm(((zzfga) obj).zzhq()) : zzffg.zzlm(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static int zzb(Entry<FieldDescriptorType, Object> entry) {
        zzffs zzffs = (zzffs) entry.getKey();
        Object value = entry.getValue();
        return (zzffs.zzcxi() != zzfjd.MESSAGE || zzffs.zzcxj() || zzffs.zzcxk()) ? zzb(zzffs, value) : value instanceof zzfgg ? zzffg.zzb(((zzffs) entry.getKey()).zzhq(), (zzfgk) (zzfgg) value) : zzffg.zzd(((zzffs) entry.getKey()).zzhq(), (zzfhe) value);
    }

    public static <T extends zzffs<T>> zzffq<T> zzcxf() {
        return new zzffq<>();
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzffq zzffq = new zzffq();
        for (int i = 0; i < this.zzpgk.zzczj(); i++) {
            Entry zzmb = this.zzpgk.zzmb(i);
            zzffq.zza((FieldDescriptorType) (zzffs) zzmb.getKey(), zzmb.getValue());
        }
        for (Entry entry : this.zzpgk.zzczk()) {
            zzffq.zza((FieldDescriptorType) (zzffs) entry.getKey(), entry.getValue());
        }
        zzffq.zzpgl = this.zzpgl;
        return zzffq;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzffq)) {
            return false;
        }
        return this.zzpgk.equals(((zzffq) obj).zzpgk);
    }

    public final int hashCode() {
        return this.zzpgk.hashCode();
    }

    public final Iterator<Entry<FieldDescriptorType, Object>> iterator() {
        return this.zzpgl ? new zzfgj(this.zzpgk.entrySet().iterator()) : this.zzpgk.entrySet().iterator();
    }

    public final int zzcxg() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzpgk.zzczj(); i2++) {
            i += zzb(this.zzpgk.zzmb(i2));
        }
        for (Entry zzb : this.zzpgk.zzczk()) {
            i += zzb(zzb);
        }
        return i;
    }
}
