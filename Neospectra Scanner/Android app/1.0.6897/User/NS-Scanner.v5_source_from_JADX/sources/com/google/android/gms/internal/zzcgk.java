package com.google.android.gms.internal;

import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class zzcgk extends zzcjl {
    zzcgk(zzcim zzcim) {
        super(zzcim);
    }

    private final Boolean zza(double d, zzclu zzclu) {
        try {
            return zza(new BigDecimal(d), zzclu, Math.ulp(d));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(long j, zzclu zzclu) {
        try {
            return zza(new BigDecimal(j), zzclu, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(zzcls zzcls, zzcmb zzcmb, long j) {
        zzclt[] zzcltArr;
        zzcmc[] zzcmcArr;
        zzclt[] zzcltArr2;
        Boolean bool;
        String str;
        Object obj;
        if (zzcls.zzjka != null) {
            Boolean zza = zza(j, zzcls.zzjka);
            if (zza == null) {
                return null;
            }
            if (!zza.booleanValue()) {
                return Boolean.valueOf(false);
            }
        }
        HashSet hashSet = new HashSet();
        for (zzclt zzclt : zzcls.zzjjy) {
            if (TextUtils.isEmpty(zzclt.zzjkf)) {
                zzawy().zzazf().zzj("null or empty param name in filter. event", zzawt().zzjh(zzcmb.name));
                return null;
            }
            hashSet.add(zzclt.zzjkf);
        }
        ArrayMap arrayMap = new ArrayMap();
        for (zzcmc zzcmc : zzcmb.zzjlh) {
            if (hashSet.contains(zzcmc.name)) {
                if (zzcmc.zzjll != null) {
                    str = zzcmc.name;
                    obj = zzcmc.zzjll;
                } else if (zzcmc.zzjjl != null) {
                    str = zzcmc.name;
                    obj = zzcmc.zzjjl;
                } else if (zzcmc.zzgcc != null) {
                    str = zzcmc.name;
                    obj = zzcmc.zzgcc;
                } else {
                    zzawy().zzazf().zze("Unknown value for param. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(zzcmc.name));
                    return null;
                }
                arrayMap.put(str, obj);
            }
        }
        for (zzclt zzclt2 : zzcls.zzjjy) {
            boolean equals = Boolean.TRUE.equals(zzclt2.zzjke);
            String str2 = zzclt2.zzjkf;
            if (TextUtils.isEmpty(str2)) {
                zzawy().zzazf().zzj("Event has empty param name. event", zzawt().zzjh(zzcmb.name));
                return null;
            }
            Object obj2 = arrayMap.get(str2);
            if (obj2 instanceof Long) {
                if (zzclt2.zzjkd == null) {
                    zzawy().zzazf().zze("No number filter for long param. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                    return null;
                }
                Boolean zza2 = zza(((Long) obj2).longValue(), zzclt2.zzjkd);
                if (zza2 == null) {
                    return null;
                }
                if ((true ^ zza2.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj2 instanceof Double) {
                if (zzclt2.zzjkd == null) {
                    zzawy().zzazf().zze("No number filter for double param. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                    return null;
                }
                Boolean zza3 = zza(((Double) obj2).doubleValue(), zzclt2.zzjkd);
                if (zza3 == null) {
                    return null;
                }
                if ((true ^ zza3.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj2 instanceof String) {
                if (zzclt2.zzjkc != null) {
                    bool = zza((String) obj2, zzclt2.zzjkc);
                } else if (zzclt2.zzjkd != null) {
                    String str3 = (String) obj2;
                    if (zzclq.zzkk(str3)) {
                        bool = zza(str3, zzclt2.zzjkd);
                    } else {
                        zzawy().zzazf().zze("Invalid param value for number filter. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                        return null;
                    }
                } else {
                    zzawy().zzazf().zze("No filter for String param. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                    return null;
                }
                if (bool == null) {
                    return null;
                }
                if ((true ^ bool.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj2 == null) {
                zzawy().zzazj().zze("Missing param for filter. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                return Boolean.valueOf(false);
            } else {
                zzawy().zzazf().zze("Unknown param type. event, param", zzawt().zzjh(zzcmb.name), zzawt().zzji(str2));
                return null;
            }
        }
        return Boolean.valueOf(true);
    }

    private static Boolean zza(Boolean bool, boolean z) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, int i, boolean z, String str2, List<String> list, String str3) {
        boolean startsWith;
        if (str == null) {
            return null;
        }
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z && i != 1) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (i) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException e) {
                    zzawy().zzazf().zzj("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                startsWith = str.startsWith(str2);
                break;
            case 3:
                startsWith = str.endsWith(str2);
                break;
            case 4:
                startsWith = str.contains(str2);
                break;
            case 5:
                startsWith = str.equals(str2);
                break;
            case 6:
                startsWith = list.contains(str);
                break;
            default:
                return null;
        }
        return Boolean.valueOf(startsWith);
    }

    private final Boolean zza(String str, zzclu zzclu) {
        if (!zzclq.zzkk(str)) {
            return null;
        }
        try {
            return zza(new BigDecimal(str), zzclu, 0.0d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final Boolean zza(String str, zzclw zzclw) {
        List list;
        zzbq.checkNotNull(zzclw);
        if (str == null || zzclw.zzjko == null || zzclw.zzjko.intValue() == 0) {
            return null;
        }
        if (zzclw.zzjko.intValue() == 6) {
            if (zzclw.zzjkr == null || zzclw.zzjkr.length == 0) {
                return null;
            }
        } else if (zzclw.zzjkp == null) {
            return null;
        }
        int intValue = zzclw.zzjko.intValue();
        boolean z = zzclw.zzjkq != null && zzclw.zzjkq.booleanValue();
        String upperCase = (z || intValue == 1 || intValue == 6) ? zzclw.zzjkp : zzclw.zzjkp.toUpperCase(Locale.ENGLISH);
        if (zzclw.zzjkr == null) {
            list = null;
        } else {
            String[] strArr = zzclw.zzjkr;
            if (z) {
                list = Arrays.asList(strArr);
            } else {
                ArrayList arrayList = new ArrayList();
                for (String upperCase2 : strArr) {
                    arrayList.add(upperCase2.toUpperCase(Locale.ENGLISH));
                }
                list = arrayList;
            }
        }
        return zza(str, intValue, z, upperCase, list, intValue == 1 ? upperCase : null);
    }

    private static Boolean zza(BigDecimal bigDecimal, zzclu zzclu, double d) {
        BigDecimal bigDecimal2;
        BigDecimal bigDecimal3;
        BigDecimal bigDecimal4;
        zzbq.checkNotNull(zzclu);
        if (zzclu.zzjkg == null || zzclu.zzjkg.intValue() == 0) {
            return null;
        }
        if (zzclu.zzjkg.intValue() == 4) {
            if (zzclu.zzjkj == null || zzclu.zzjkk == null) {
                return null;
            }
        } else if (zzclu.zzjki == null) {
            return null;
        }
        int intValue = zzclu.zzjkg.intValue();
        if (zzclu.zzjkg.intValue() == 4) {
            if (!zzclq.zzkk(zzclu.zzjkj) || !zzclq.zzkk(zzclu.zzjkk)) {
                return null;
            }
            try {
                BigDecimal bigDecimal5 = new BigDecimal(zzclu.zzjkj);
                bigDecimal3 = new BigDecimal(zzclu.zzjkk);
                bigDecimal2 = bigDecimal5;
                bigDecimal4 = null;
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (!zzclq.zzkk(zzclu.zzjki)) {
            return null;
        } else {
            try {
                bigDecimal4 = new BigDecimal(zzclu.zzjki);
                bigDecimal2 = null;
                bigDecimal3 = null;
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        if (intValue == 4) {
            if (bigDecimal2 == null) {
                return null;
            }
        } else if (bigDecimal4 == null) {
            return null;
        }
        boolean z = false;
        switch (intValue) {
            case 1:
                if (bigDecimal.compareTo(bigDecimal4) == -1) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 2:
                if (bigDecimal.compareTo(bigDecimal4) == 1) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 3:
                if (d != 0.0d) {
                    if (bigDecimal.compareTo(bigDecimal4.subtract(new BigDecimal(d).multiply(new BigDecimal(2)))) == 1 && bigDecimal.compareTo(bigDecimal4.add(new BigDecimal(d).multiply(new BigDecimal(2)))) == -1) {
                        z = true;
                    }
                    return Boolean.valueOf(z);
                }
                if (bigDecimal.compareTo(bigDecimal4) == 0) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 4:
                if (!(bigDecimal.compareTo(bigDecimal2) == -1 || bigDecimal.compareTo(bigDecimal3) == 1)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            default:
                return null;
        }
    }

    /* JADX WARNING: type inference failed for: r14v17 */
    /* JADX WARNING: type inference failed for: r14v18, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v42 */
    /* JADX WARNING: type inference failed for: r12v54 */
    /* JADX WARNING: type inference failed for: r12v55, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r14v39 */
    /* JADX WARNING: type inference failed for: r12v59 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x05f8, code lost:
        zzawy().zzazf().zze("Invalid property filter ID. appId, id", com.google.android.gms.internal.zzchm.zzjk(r54), java.lang.String.valueOf(r5.zzjjw));
        r11.add(java.lang.Integer.valueOf(r13));
        r4 = r44;
        r5 = r45;
        r9 = r46;
        r12 = r47;
        r40 = r49;
        r42 = r50;
        r41 = r51;
        r3 = r56;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x05ca  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x05cd  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x05d3  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x05dc  */
    /* JADX WARNING: Unknown variable types count: 3 */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.zzcma[] zza(java.lang.String r54, com.google.android.gms.internal.zzcmb[] r55, com.google.android.gms.internal.zzcmg[] r56) {
        /*
            r53 = this;
            r1 = r53
            r15 = r54
            r14 = r55
            r13 = r56
            com.google.android.gms.common.internal.zzbq.zzgm(r54)
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            android.support.v4.util.ArrayMap r12 = new android.support.v4.util.ArrayMap
            r12.<init>()
            android.support.v4.util.ArrayMap r9 = new android.support.v4.util.ArrayMap
            r9.<init>()
            android.support.v4.util.ArrayMap r10 = new android.support.v4.util.ArrayMap
            r10.<init>()
            com.google.android.gms.internal.zzcgo r2 = r53.zzaws()
            java.util.Map r2 = r2.zzje(r15)
            if (r2 == 0) goto L_0x00fc
            java.util.Set r3 = r2.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x0031:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x00fc
            java.lang.Object r4 = r3.next()
            java.lang.Integer r4 = (java.lang.Integer) r4
            int r4 = r4.intValue()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)
            java.lang.Object r5 = r2.get(r5)
            com.google.android.gms.internal.zzcmf r5 = (com.google.android.gms.internal.zzcmf) r5
            java.lang.Integer r6 = java.lang.Integer.valueOf(r4)
            java.lang.Object r6 = r9.get(r6)
            java.util.BitSet r6 = (java.util.BitSet) r6
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)
            java.lang.Object r8 = r10.get(r8)
            java.util.BitSet r8 = (java.util.BitSet) r8
            if (r6 != 0) goto L_0x0079
            java.util.BitSet r6 = new java.util.BitSet
            r6.<init>()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)
            r9.put(r8, r6)
            java.util.BitSet r8 = new java.util.BitSet
            r8.<init>()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)
            r10.put(r7, r8)
        L_0x0079:
            r17 = r2
            r7 = 0
        L_0x007c:
            long[] r2 = r5.zzjmp
            int r2 = r2.length
            int r2 = r2 << 6
            if (r7 >= r2) goto L_0x00c4
            long[] r2 = r5.zzjmp
            boolean r2 = com.google.android.gms.internal.zzclq.zza(r2, r7)
            if (r2 == 0) goto L_0x00b5
            com.google.android.gms.internal.zzchm r2 = r53.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazj()
            r18 = r3
            java.lang.String r3 = "Filter already evaluated. audience ID, filter ID"
            r19 = r9
            java.lang.Integer r9 = java.lang.Integer.valueOf(r4)
            r20 = r10
            java.lang.Integer r10 = java.lang.Integer.valueOf(r7)
            r2.zze(r3, r9, r10)
            r8.set(r7)
            long[] r2 = r5.zzjmq
            boolean r2 = com.google.android.gms.internal.zzclq.zza(r2, r7)
            if (r2 == 0) goto L_0x00bb
            r6.set(r7)
            goto L_0x00bb
        L_0x00b5:
            r18 = r3
            r19 = r9
            r20 = r10
        L_0x00bb:
            int r7 = r7 + 1
            r3 = r18
            r9 = r19
            r10 = r20
            goto L_0x007c
        L_0x00c4:
            r18 = r3
            r19 = r9
            r20 = r10
            com.google.android.gms.internal.zzcma r2 = new com.google.android.gms.internal.zzcma
            r2.<init>()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r4)
            r12.put(r3, r2)
            r7 = 0
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r7)
            r2.zzjlf = r3
            r2.zzjle = r5
            com.google.android.gms.internal.zzcmf r3 = new com.google.android.gms.internal.zzcmf
            r3.<init>()
            r2.zzjld = r3
            com.google.android.gms.internal.zzcmf r3 = r2.zzjld
            long[] r4 = com.google.android.gms.internal.zzclq.zza(r6)
            r3.zzjmq = r4
            com.google.android.gms.internal.zzcmf r2 = r2.zzjld
            long[] r3 = com.google.android.gms.internal.zzclq.zza(r8)
            r2.zzjmp = r3
            r2 = r17
            r3 = r18
            goto L_0x0031
        L_0x00fc:
            r19 = r9
            r20 = r10
            r7 = 0
            if (r14 == 0) goto L_0x0385
            android.support.v4.util.ArrayMap r5 = new android.support.v4.util.ArrayMap
            r5.<init>()
            int r6 = r14.length
            r4 = 0
        L_0x010a:
            if (r4 >= r6) goto L_0x0385
            r3 = r14[r4]
            com.google.android.gms.internal.zzcgo r2 = r53.zzaws()
            java.lang.String r7 = r3.name
            com.google.android.gms.internal.zzcgw r2 = r2.zzae(r15, r7)
            if (r2 != 0) goto L_0x0173
            com.google.android.gms.internal.zzchm r2 = r53.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()
            java.lang.String r7 = "Event aggregate wasn't created during raw event logging. appId, event"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r54)
            com.google.android.gms.internal.zzchk r9 = r53.zzawt()
            java.lang.String r10 = r3.name
            java.lang.String r9 = r9.zzjh(r10)
            r2.zze(r7, r8, r9)
            com.google.android.gms.internal.zzcgw r16 = new com.google.android.gms.internal.zzcgw
            java.lang.String r7 = r3.name
            r8 = 1
            r17 = 1
            java.lang.Long r2 = r3.zzjli
            long r21 = r2.longValue()
            r23 = 0
            r25 = 0
            r26 = 0
            r27 = 0
            r2 = r16
            r10 = r3
            r3 = r15
            r28 = r4
            r4 = r7
            r7 = r5
            r29 = r6
            r5 = r8
            r9 = r7
            r7 = r17
            r32 = r9
            r33 = r10
            r30 = r19
            r31 = r20
            r9 = r21
            r34 = r11
            r35 = r12
            r11 = r23
            r13 = r25
            r14 = r26
            r15 = r27
            r2.<init>(r3, r4, r5, r7, r9, r11, r13, r14, r15)
            goto L_0x0189
        L_0x0173:
            r33 = r3
            r28 = r4
            r32 = r5
            r29 = r6
            r34 = r11
            r35 = r12
            r30 = r19
            r31 = r20
            com.google.android.gms.internal.zzcgw r16 = r2.zzayw()
            r2 = r16
        L_0x0189:
            com.google.android.gms.internal.zzcgo r3 = r53.zzaws()
            r3.zza(r2)
            long r2 = r2.zzizk
            r4 = r33
            java.lang.String r5 = r4.name
            r6 = r32
            java.lang.Object r5 = r6.get(r5)
            java.util.Map r5 = (java.util.Map) r5
            if (r5 != 0) goto L_0x01b9
            com.google.android.gms.internal.zzcgo r5 = r53.zzaws()
            java.lang.String r7 = r4.name
            r8 = r54
            java.util.Map r5 = r5.zzaj(r8, r7)
            if (r5 != 0) goto L_0x01b3
            android.support.v4.util.ArrayMap r5 = new android.support.v4.util.ArrayMap
            r5.<init>()
        L_0x01b3:
            java.lang.String r7 = r4.name
            r6.put(r7, r5)
            goto L_0x01bb
        L_0x01b9:
            r8 = r54
        L_0x01bb:
            java.util.Set r7 = r5.keySet()
            java.util.Iterator r7 = r7.iterator()
        L_0x01c3:
            boolean r9 = r7.hasNext()
            if (r9 == 0) goto L_0x0364
            java.lang.Object r9 = r7.next()
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r9 = r9.intValue()
            java.lang.Integer r10 = java.lang.Integer.valueOf(r9)
            r11 = r34
            boolean r10 = r11.contains(r10)
            if (r10 == 0) goto L_0x01f3
            com.google.android.gms.internal.zzchm r10 = r53.zzawy()
            com.google.android.gms.internal.zzcho r10 = r10.zzazj()
            java.lang.String r12 = "Skipping failed audience ID"
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r10.zzj(r12, r9)
            r34 = r11
            goto L_0x01c3
        L_0x01f3:
            java.lang.Integer r10 = java.lang.Integer.valueOf(r9)
            r12 = r35
            java.lang.Object r10 = r12.get(r10)
            com.google.android.gms.internal.zzcma r10 = (com.google.android.gms.internal.zzcma) r10
            java.lang.Integer r13 = java.lang.Integer.valueOf(r9)
            r14 = r30
            java.lang.Object r13 = r14.get(r13)
            java.util.BitSet r13 = (java.util.BitSet) r13
            java.lang.Integer r15 = java.lang.Integer.valueOf(r9)
            r36 = r6
            r6 = r31
            java.lang.Object r15 = r6.get(r15)
            java.util.BitSet r15 = (java.util.BitSet) r15
            if (r10 != 0) goto L_0x0247
            com.google.android.gms.internal.zzcma r10 = new com.google.android.gms.internal.zzcma
            r10.<init>()
            java.lang.Integer r13 = java.lang.Integer.valueOf(r9)
            r12.put(r13, r10)
            r13 = 1
            java.lang.Boolean r15 = java.lang.Boolean.valueOf(r13)
            r10.zzjlf = r15
            java.util.BitSet r10 = new java.util.BitSet
            r10.<init>()
            java.lang.Integer r15 = java.lang.Integer.valueOf(r9)
            r14.put(r15, r10)
            java.util.BitSet r15 = new java.util.BitSet
            r15.<init>()
            java.lang.Integer r13 = java.lang.Integer.valueOf(r9)
            r6.put(r13, r15)
            r13 = r10
        L_0x0247:
            java.lang.Integer r10 = java.lang.Integer.valueOf(r9)
            java.lang.Object r10 = r5.get(r10)
            java.util.List r10 = (java.util.List) r10
            java.util.Iterator r10 = r10.iterator()
        L_0x0255:
            boolean r16 = r10.hasNext()
            if (r16 == 0) goto L_0x0356
            java.lang.Object r16 = r10.next()
            r37 = r5
            r5 = r16
            com.google.android.gms.internal.zzcls r5 = (com.google.android.gms.internal.zzcls) r5
            r38 = r7
            com.google.android.gms.internal.zzchm r7 = r53.zzawy()
            r39 = r10
            r10 = 2
            boolean r7 = r7.zzae(r10)
            if (r7 == 0) goto L_0x02ad
            com.google.android.gms.internal.zzchm r7 = r53.zzawy()
            com.google.android.gms.internal.zzcho r7 = r7.zzazj()
            java.lang.String r10 = "Evaluating filter. audience, filter, event"
            r40 = r6
            java.lang.Integer r6 = java.lang.Integer.valueOf(r9)
            r41 = r14
            java.lang.Integer r14 = r5.zzjjw
            r42 = r12
            com.google.android.gms.internal.zzchk r12 = r53.zzawt()
            java.lang.String r8 = r5.zzjjx
            java.lang.String r8 = r12.zzjh(r8)
            r7.zzd(r10, r6, r14, r8)
            com.google.android.gms.internal.zzchm r6 = r53.zzawy()
            com.google.android.gms.internal.zzcho r6 = r6.zzazj()
            java.lang.String r7 = "Filter definition"
            com.google.android.gms.internal.zzchk r8 = r53.zzawt()
            java.lang.String r8 = r8.zza(r5)
            r6.zzj(r7, r8)
            goto L_0x02b3
        L_0x02ad:
            r40 = r6
            r42 = r12
            r41 = r14
        L_0x02b3:
            java.lang.Integer r6 = r5.zzjjw
            if (r6 == 0) goto L_0x032c
            java.lang.Integer r6 = r5.zzjjw
            int r6 = r6.intValue()
            r7 = 256(0x100, float:3.59E-43)
            if (r6 <= r7) goto L_0x02c3
            goto L_0x032e
        L_0x02c3:
            java.lang.Integer r6 = r5.zzjjw
            int r6 = r6.intValue()
            boolean r6 = r13.get(r6)
            if (r6 == 0) goto L_0x02f2
            com.google.android.gms.internal.zzchm r6 = r53.zzawy()
            com.google.android.gms.internal.zzcho r6 = r6.zzazj()
            java.lang.String r8 = "Event filter already evaluated true. audience ID, filter ID"
            java.lang.Integer r10 = java.lang.Integer.valueOf(r9)
            java.lang.Integer r5 = r5.zzjjw
            r6.zze(r8, r10, r5)
        L_0x02e2:
            r5 = r37
            r7 = r38
            r10 = r39
            r6 = r40
            r14 = r41
            r12 = r42
            r8 = r54
            goto L_0x0255
        L_0x02f2:
            java.lang.Boolean r6 = r1.zza(r5, r4, r2)
            com.google.android.gms.internal.zzchm r8 = r53.zzawy()
            com.google.android.gms.internal.zzcho r8 = r8.zzazj()
            java.lang.String r10 = "Event filter result"
            if (r6 != 0) goto L_0x0305
            java.lang.String r12 = "null"
            goto L_0x0306
        L_0x0305:
            r12 = r6
        L_0x0306:
            r8.zzj(r10, r12)
            if (r6 != 0) goto L_0x0313
            java.lang.Integer r5 = java.lang.Integer.valueOf(r9)
            r11.add(r5)
            goto L_0x02e2
        L_0x0313:
            java.lang.Integer r8 = r5.zzjjw
            int r8 = r8.intValue()
            r15.set(r8)
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x02e2
            java.lang.Integer r5 = r5.zzjjw
            int r5 = r5.intValue()
            r13.set(r5)
            goto L_0x02e2
        L_0x032c:
            r7 = 256(0x100, float:3.59E-43)
        L_0x032e:
            com.google.android.gms.internal.zzchm r6 = r53.zzawy()
            com.google.android.gms.internal.zzcho r6 = r6.zzazf()
            java.lang.String r8 = "Invalid event filter ID. appId, id"
            r10 = r54
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r54)
            java.lang.Integer r5 = r5.zzjjw
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r6.zze(r8, r12, r5)
            r8 = r10
            r5 = r37
            r7 = r38
            r10 = r39
            r6 = r40
            r14 = r41
            r12 = r42
            goto L_0x0255
        L_0x0356:
            r38 = r7
            r31 = r6
            r34 = r11
            r35 = r12
            r30 = r14
            r6 = r36
            goto L_0x01c3
        L_0x0364:
            r36 = r6
            r10 = r8
            r41 = r30
            r40 = r31
            r11 = r34
            r42 = r35
            r7 = 256(0x100, float:3.59E-43)
            int r4 = r28 + 1
            r14 = r55
            r15 = r10
            r6 = r29
            r5 = r36
            r20 = r40
            r19 = r41
            r12 = r42
            r7 = 0
            r13 = r56
            goto L_0x010a
        L_0x0385:
            r42 = r12
            r10 = r15
            r41 = r19
            r40 = r20
            r7 = 256(0x100, float:3.59E-43)
            r3 = r56
            if (r3 == 0) goto L_0x0648
            android.support.v4.util.ArrayMap r4 = new android.support.v4.util.ArrayMap
            r4.<init>()
            int r5 = r3.length
            r6 = 0
        L_0x0399:
            if (r6 >= r5) goto L_0x0648
            r8 = r3[r6]
            java.lang.String r9 = r8.name
            java.lang.Object r9 = r4.get(r9)
            java.util.Map r9 = (java.util.Map) r9
            if (r9 != 0) goto L_0x03bd
            com.google.android.gms.internal.zzcgo r9 = r53.zzaws()
            java.lang.String r12 = r8.name
            java.util.Map r9 = r9.zzak(r10, r12)
            if (r9 != 0) goto L_0x03b8
            android.support.v4.util.ArrayMap r9 = new android.support.v4.util.ArrayMap
            r9.<init>()
        L_0x03b8:
            java.lang.String r12 = r8.name
            r4.put(r12, r9)
        L_0x03bd:
            java.util.Set r12 = r9.keySet()
            java.util.Iterator r12 = r12.iterator()
        L_0x03c5:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x0638
            java.lang.Object r13 = r12.next()
            java.lang.Integer r13 = (java.lang.Integer) r13
            int r13 = r13.intValue()
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            boolean r14 = r11.contains(r14)
            if (r14 == 0) goto L_0x03f1
            com.google.android.gms.internal.zzchm r14 = r53.zzawy()
            com.google.android.gms.internal.zzcho r14 = r14.zzazj()
            java.lang.String r15 = "Skipping failed audience ID"
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            r14.zzj(r15, r13)
            goto L_0x03c5
        L_0x03f1:
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            r15 = r42
            java.lang.Object r14 = r15.get(r14)
            com.google.android.gms.internal.zzcma r14 = (com.google.android.gms.internal.zzcma) r14
            java.lang.Integer r2 = java.lang.Integer.valueOf(r13)
            r7 = r41
            java.lang.Object r2 = r7.get(r2)
            java.util.BitSet r2 = (java.util.BitSet) r2
            r43 = r2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r13)
            r3 = r40
            java.lang.Object r2 = r3.get(r2)
            java.util.BitSet r2 = (java.util.BitSet) r2
            if (r14 != 0) goto L_0x0447
            com.google.android.gms.internal.zzcma r2 = new com.google.android.gms.internal.zzcma
            r2.<init>()
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            r15.put(r14, r2)
            r44 = r4
            r14 = 1
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r14)
            r2.zzjlf = r4
            java.util.BitSet r2 = new java.util.BitSet
            r2.<init>()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r13)
            r7.put(r4, r2)
            java.util.BitSet r4 = new java.util.BitSet
            r4.<init>()
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            r3.put(r14, r4)
            goto L_0x044c
        L_0x0447:
            r44 = r4
            r4 = r2
            r2 = r43
        L_0x044c:
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            java.lang.Object r14 = r9.get(r14)
            java.util.List r14 = (java.util.List) r14
            java.util.Iterator r14 = r14.iterator()
        L_0x045a:
            boolean r16 = r14.hasNext()
            if (r16 == 0) goto L_0x0628
            java.lang.Object r16 = r14.next()
            r45 = r5
            r5 = r16
            com.google.android.gms.internal.zzclv r5 = (com.google.android.gms.internal.zzclv) r5
            r46 = r9
            com.google.android.gms.internal.zzchm r9 = r53.zzawy()
            r47 = r12
            r12 = 2
            boolean r9 = r9.zzae(r12)
            if (r9 == 0) goto L_0x04b4
            com.google.android.gms.internal.zzchm r9 = r53.zzawy()
            com.google.android.gms.internal.zzcho r9 = r9.zzazj()
            java.lang.String r12 = "Evaluating filter. audience, filter, property"
            r48 = r14
            java.lang.Integer r14 = java.lang.Integer.valueOf(r13)
            r49 = r3
            java.lang.Integer r3 = r5.zzjjw
            r50 = r15
            com.google.android.gms.internal.zzchk r15 = r53.zzawt()
            r51 = r7
            java.lang.String r7 = r5.zzjkm
            java.lang.String r7 = r15.zzjj(r7)
            r9.zzd(r12, r14, r3, r7)
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazj()
            java.lang.String r7 = "Filter definition"
            com.google.android.gms.internal.zzchk r9 = r53.zzawt()
            java.lang.String r9 = r9.zza(r5)
            r3.zzj(r7, r9)
            goto L_0x04bc
        L_0x04b4:
            r49 = r3
            r51 = r7
            r48 = r14
            r50 = r15
        L_0x04bc:
            java.lang.Integer r3 = r5.zzjjw
            if (r3 == 0) goto L_0x05f6
            java.lang.Integer r3 = r5.zzjjw
            int r3 = r3.intValue()
            r7 = 256(0x100, float:3.59E-43)
            if (r3 <= r7) goto L_0x04cc
            goto L_0x05f8
        L_0x04cc:
            java.lang.Integer r3 = r5.zzjjw
            int r3 = r3.intValue()
            boolean r3 = r2.get(r3)
            if (r3 == 0) goto L_0x04fb
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazj()
            java.lang.String r9 = "Property filter already evaluated true. audience ID, filter ID"
            java.lang.Integer r12 = java.lang.Integer.valueOf(r13)
            java.lang.Integer r5 = r5.zzjjw
            r3.zze(r9, r12, r5)
        L_0x04eb:
            r5 = r45
            r9 = r46
            r12 = r47
            r14 = r48
            r3 = r49
            r15 = r50
            r7 = r51
            goto L_0x045a
        L_0x04fb:
            com.google.android.gms.internal.zzclt r3 = r5.zzjkn
            if (r3 != 0) goto L_0x0519
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "Missing property filter. property"
        L_0x0509:
            com.google.android.gms.internal.zzchk r12 = r53.zzawt()
            java.lang.String r14 = r8.name
            java.lang.String r12 = r12.zzjj(r14)
            r3.zzj(r9, r12)
        L_0x0516:
            r3 = 0
            goto L_0x05be
        L_0x0519:
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            java.lang.Boolean r12 = r3.zzjke
            boolean r9 = r9.equals(r12)
            java.lang.Long r12 = r8.zzjll
            if (r12 == 0) goto L_0x0546
            com.google.android.gms.internal.zzclu r12 = r3.zzjkd
            if (r12 != 0) goto L_0x0534
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "No number filter for long property. property"
            goto L_0x0509
        L_0x0534:
            java.lang.Long r12 = r8.zzjll
            long r14 = r12.longValue()
            com.google.android.gms.internal.zzclu r3 = r3.zzjkd
            java.lang.Boolean r3 = r1.zza(r14, r3)
        L_0x0540:
            java.lang.Boolean r3 = zza(r3, r9)
            goto L_0x05be
        L_0x0546:
            java.lang.Double r12 = r8.zzjjl
            if (r12 == 0) goto L_0x0566
            com.google.android.gms.internal.zzclu r12 = r3.zzjkd
            if (r12 != 0) goto L_0x0559
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "No number filter for double property. property"
            goto L_0x0509
        L_0x0559:
            java.lang.Double r12 = r8.zzjjl
            double r14 = r12.doubleValue()
            com.google.android.gms.internal.zzclu r3 = r3.zzjkd
            java.lang.Boolean r3 = r1.zza(r14, r3)
            goto L_0x0540
        L_0x0566:
            java.lang.String r12 = r8.zzgcc
            if (r12 == 0) goto L_0x05b2
            com.google.android.gms.internal.zzclw r12 = r3.zzjkc
            if (r12 != 0) goto L_0x05a9
            com.google.android.gms.internal.zzclu r12 = r3.zzjkd
            if (r12 != 0) goto L_0x057d
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "No string or number filter defined. property"
            goto L_0x0509
        L_0x057d:
            java.lang.String r12 = r8.zzgcc
            boolean r12 = com.google.android.gms.internal.zzclq.zzkk(r12)
            if (r12 == 0) goto L_0x058e
            java.lang.String r12 = r8.zzgcc
            com.google.android.gms.internal.zzclu r3 = r3.zzjkd
            java.lang.Boolean r3 = r1.zza(r12, r3)
            goto L_0x0540
        L_0x058e:
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "Invalid user property value for Numeric number filter. property, value"
            com.google.android.gms.internal.zzchk r12 = r53.zzawt()
            java.lang.String r14 = r8.name
            java.lang.String r12 = r12.zzjj(r14)
            java.lang.String r14 = r8.zzgcc
            r3.zze(r9, r12, r14)
            goto L_0x0516
        L_0x05a9:
            java.lang.String r12 = r8.zzgcc
            com.google.android.gms.internal.zzclw r3 = r3.zzjkc
            java.lang.Boolean r3 = r1.zza(r12, r3)
            goto L_0x0540
        L_0x05b2:
            com.google.android.gms.internal.zzchm r3 = r53.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r9 = "User property has no value, property"
            goto L_0x0509
        L_0x05be:
            com.google.android.gms.internal.zzchm r9 = r53.zzawy()
            com.google.android.gms.internal.zzcho r9 = r9.zzazj()
            java.lang.String r12 = "Property filter result"
            if (r3 != 0) goto L_0x05cd
            java.lang.String r14 = "null"
            goto L_0x05ce
        L_0x05cd:
            r14 = r3
        L_0x05ce:
            r9.zzj(r12, r14)
            if (r3 != 0) goto L_0x05dc
            java.lang.Integer r3 = java.lang.Integer.valueOf(r13)
            r11.add(r3)
            goto L_0x04eb
        L_0x05dc:
            java.lang.Integer r9 = r5.zzjjw
            int r9 = r9.intValue()
            r4.set(r9)
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x04eb
            java.lang.Integer r3 = r5.zzjjw
            int r3 = r3.intValue()
            r2.set(r3)
            goto L_0x04eb
        L_0x05f6:
            r7 = 256(0x100, float:3.59E-43)
        L_0x05f8:
            com.google.android.gms.internal.zzchm r2 = r53.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazf()
            java.lang.String r3 = "Invalid property filter ID. appId, id"
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r54)
            java.lang.Integer r5 = r5.zzjjw
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r2.zze(r3, r4, r5)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r13)
            r11.add(r2)
            r4 = r44
            r5 = r45
            r9 = r46
            r12 = r47
            r40 = r49
            r42 = r50
            r41 = r51
            r3 = r56
            goto L_0x03c5
        L_0x0628:
            r51 = r7
            r40 = r3
            r42 = r15
            r4 = r44
            r41 = r51
            r3 = r56
            r7 = 256(0x100, float:3.59E-43)
            goto L_0x03c5
        L_0x0638:
            r44 = r4
            r45 = r5
            r49 = r40
            r51 = r41
            r50 = r42
            int r6 = r6 + 1
            r3 = r56
            goto L_0x0399
        L_0x0648:
            r49 = r40
            r50 = r42
            r2 = r41
            int r3 = r2.size()
            com.google.android.gms.internal.zzcma[] r3 = new com.google.android.gms.internal.zzcma[r3]
            java.util.Set r4 = r2.keySet()
            java.util.Iterator r4 = r4.iterator()
            r7 = 0
        L_0x065d:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x074c
            java.lang.Object r5 = r4.next()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            java.lang.Integer r6 = java.lang.Integer.valueOf(r5)
            boolean r6 = r11.contains(r6)
            if (r6 != 0) goto L_0x065d
            java.lang.Integer r6 = java.lang.Integer.valueOf(r5)
            r8 = r50
            java.lang.Object r6 = r8.get(r6)
            com.google.android.gms.internal.zzcma r6 = (com.google.android.gms.internal.zzcma) r6
            if (r6 != 0) goto L_0x068a
            com.google.android.gms.internal.zzcma r6 = new com.google.android.gms.internal.zzcma
            r6.<init>()
        L_0x068a:
            int r9 = r7 + 1
            r3[r7] = r6
            java.lang.Integer r7 = java.lang.Integer.valueOf(r5)
            r6.zzjjs = r7
            com.google.android.gms.internal.zzcmf r7 = new com.google.android.gms.internal.zzcmf
            r7.<init>()
            r6.zzjld = r7
            com.google.android.gms.internal.zzcmf r7 = r6.zzjld
            java.lang.Integer r12 = java.lang.Integer.valueOf(r5)
            java.lang.Object r12 = r2.get(r12)
            java.util.BitSet r12 = (java.util.BitSet) r12
            long[] r12 = com.google.android.gms.internal.zzclq.zza(r12)
            r7.zzjmq = r12
            com.google.android.gms.internal.zzcmf r7 = r6.zzjld
            java.lang.Integer r12 = java.lang.Integer.valueOf(r5)
            r13 = r49
            java.lang.Object r12 = r13.get(r12)
            java.util.BitSet r12 = (java.util.BitSet) r12
            long[] r12 = com.google.android.gms.internal.zzclq.zza(r12)
            r7.zzjmp = r12
            com.google.android.gms.internal.zzcgo r7 = r53.zzaws()
            com.google.android.gms.internal.zzcmf r6 = r6.zzjld
            r7.zzxf()
            r7.zzve()
            com.google.android.gms.common.internal.zzbq.zzgm(r54)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r6)
            int r12 = r6.zzho()     // Catch:{ IOException -> 0x0731 }
            byte[] r12 = new byte[r12]     // Catch:{ IOException -> 0x0731 }
            int r14 = r12.length     // Catch:{ IOException -> 0x0731 }
            r15 = 0
            com.google.android.gms.internal.zzfjk r14 = com.google.android.gms.internal.zzfjk.zzo(r12, r15, r14)     // Catch:{ IOException -> 0x0731 }
            r6.zza(r14)     // Catch:{ IOException -> 0x0731 }
            r14.zzcwt()     // Catch:{ IOException -> 0x0731 }
            android.content.ContentValues r6 = new android.content.ContentValues
            r6.<init>()
            java.lang.String r14 = "app_id"
            r6.put(r14, r10)
            java.lang.String r14 = "audience_id"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r6.put(r14, r5)
            java.lang.String r5 = "current_results"
            r6.put(r5, r12)
            android.database.sqlite.SQLiteDatabase r5 = r7.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0723 }
            java.lang.String r12 = "audience_filter_values"
            r14 = 5
            r15 = 0
            long r5 = r5.insertWithOnConflict(r12, r15, r6, r14)     // Catch:{ SQLiteException -> 0x0721 }
            r16 = -1
            int r5 = (r5 > r16 ? 1 : (r5 == r16 ? 0 : -1))
            if (r5 != 0) goto L_0x0745
            com.google.android.gms.internal.zzchm r5 = r7.zzawy()     // Catch:{ SQLiteException -> 0x0721 }
            com.google.android.gms.internal.zzcho r5 = r5.zzazd()     // Catch:{ SQLiteException -> 0x0721 }
            java.lang.String r6 = "Failed to insert filter results (got -1). appId"
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r54)     // Catch:{ SQLiteException -> 0x0721 }
            r5.zzj(r6, r12)     // Catch:{ SQLiteException -> 0x0721 }
            goto L_0x0745
        L_0x0721:
            r0 = move-exception
            goto L_0x0725
        L_0x0723:
            r0 = move-exception
            r15 = 0
        L_0x0725:
            r5 = r0
            com.google.android.gms.internal.zzchm r6 = r7.zzawy()
            com.google.android.gms.internal.zzcho r6 = r6.zzazd()
            java.lang.String r7 = "Error storing filter results. appId"
            goto L_0x073e
        L_0x0731:
            r0 = move-exception
            r15 = 0
            r5 = r0
            com.google.android.gms.internal.zzchm r6 = r7.zzawy()
            com.google.android.gms.internal.zzcho r6 = r6.zzazd()
            java.lang.String r7 = "Configuration loss. Failed to serialize filter results. appId"
        L_0x073e:
            java.lang.Object r12 = com.google.android.gms.internal.zzchm.zzjk(r54)
            r6.zze(r7, r12, r5)
        L_0x0745:
            r50 = r8
            r7 = r9
            r49 = r13
            goto L_0x065d
        L_0x074c:
            java.lang.Object[] r2 = java.util.Arrays.copyOf(r3, r7)
            com.google.android.gms.internal.zzcma[] r2 = (com.google.android.gms.internal.zzcma[]) r2
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcgk.zza(java.lang.String, com.google.android.gms.internal.zzcmb[], com.google.android.gms.internal.zzcmg[]):com.google.android.gms.internal.zzcma[]");
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }
}
