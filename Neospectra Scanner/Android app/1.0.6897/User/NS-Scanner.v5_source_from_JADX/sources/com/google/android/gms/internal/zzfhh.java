package com.google.android.gms.internal;

import java.util.List;
import org.apache.commons.math3.geometry.VectorFormat;

final class zzfhh {
    static String zza(zzfhe zzfhe, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        zza(zzfhe, sb, 0);
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0198, code lost:
        if (((java.lang.Boolean) r7).booleanValue() == false) goto L_0x019a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x01aa, code lost:
        if (((java.lang.Integer) r7).intValue() == 0) goto L_0x019a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01bb, code lost:
        if (((java.lang.Float) r7).floatValue() == 0.0f) goto L_0x019a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x01cd, code lost:
        if (((java.lang.Double) r7).doubleValue() == 0.0d) goto L_0x019a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0200  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0202  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zza(com.google.android.gms.internal.zzfhe r12, java.lang.StringBuilder r13, int r14) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.util.TreeSet r2 = new java.util.TreeSet
            r2.<init>()
            java.lang.Class r3 = r12.getClass()
            java.lang.reflect.Method[] r3 = r3.getDeclaredMethods()
            int r4 = r3.length
            r5 = 0
            r6 = 0
        L_0x001a:
            if (r6 >= r4) goto L_0x0049
            r7 = r3[r6]
            java.lang.String r8 = r7.getName()
            r1.put(r8, r7)
            java.lang.Class[] r8 = r7.getParameterTypes()
            int r8 = r8.length
            if (r8 != 0) goto L_0x0046
            java.lang.String r8 = r7.getName()
            r0.put(r8, r7)
            java.lang.String r8 = r7.getName()
            java.lang.String r9 = "get"
            boolean r8 = r8.startsWith(r9)
            if (r8 == 0) goto L_0x0046
            java.lang.String r7 = r7.getName()
            r2.add(r7)
        L_0x0046:
            int r6 = r6 + 1
            goto L_0x001a
        L_0x0049:
            java.util.Iterator r2 = r2.iterator()
        L_0x004d:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x021b
            java.lang.Object r3 = r2.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "get"
            java.lang.String r6 = ""
            java.lang.String r3 = r3.replaceFirst(r4, r6)
            java.lang.String r4 = "List"
            boolean r4 = r3.endsWith(r4)
            r6 = 1
            if (r4 == 0) goto L_0x00d7
            java.lang.String r4 = "OrBuilderList"
            boolean r4 = r3.endsWith(r4)
            if (r4 != 0) goto L_0x00d7
            java.lang.String r4 = r3.substring(r5, r6)
            java.lang.String r4 = r4.toLowerCase()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            int r7 = r3.length()
            int r7 = r7 + -4
            java.lang.String r7 = r3.substring(r6, r7)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            int r8 = r7.length()
            if (r8 == 0) goto L_0x0097
            java.lang.String r4 = r4.concat(r7)
            goto L_0x009d
        L_0x0097:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r4)
            r4 = r7
        L_0x009d:
            java.lang.String r7 = "get"
            java.lang.String r8 = java.lang.String.valueOf(r3)
            int r9 = r8.length()
            if (r9 == 0) goto L_0x00ae
            java.lang.String r7 = r7.concat(r8)
            goto L_0x00b4
        L_0x00ae:
            java.lang.String r8 = new java.lang.String
            r8.<init>(r7)
            r7 = r8
        L_0x00b4:
            java.lang.Object r7 = r0.get(r7)
            java.lang.reflect.Method r7 = (java.lang.reflect.Method) r7
            if (r7 == 0) goto L_0x00d7
            java.lang.Class r8 = r7.getReturnType()
            java.lang.Class<java.util.List> r9 = java.util.List.class
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x00d7
            java.lang.String r3 = zztv(r4)
            java.lang.Object[] r4 = new java.lang.Object[r5]
            java.lang.Object r4 = com.google.android.gms.internal.zzffu.zza(r7, r12, r4)
            zzb(r13, r14, r3, r4)
            goto L_0x004d
        L_0x00d7:
            java.lang.String r4 = "set"
            java.lang.String r7 = java.lang.String.valueOf(r3)
            int r8 = r7.length()
            if (r8 == 0) goto L_0x00e8
            java.lang.String r4 = r4.concat(r7)
            goto L_0x00ee
        L_0x00e8:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r4)
            r4 = r7
        L_0x00ee:
            java.lang.Object r4 = r1.get(r4)
            java.lang.reflect.Method r4 = (java.lang.reflect.Method) r4
            if (r4 == 0) goto L_0x004d
            java.lang.String r4 = "Bytes"
            boolean r4 = r3.endsWith(r4)
            if (r4 == 0) goto L_0x0125
            java.lang.String r4 = "get"
            int r7 = r3.length()
            int r7 = r7 + -5
            java.lang.String r7 = r3.substring(r5, r7)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            int r8 = r7.length()
            if (r8 == 0) goto L_0x0119
            java.lang.String r4 = r4.concat(r7)
            goto L_0x011f
        L_0x0119:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r4)
            r4 = r7
        L_0x011f:
            boolean r4 = r0.containsKey(r4)
            if (r4 != 0) goto L_0x004d
        L_0x0125:
            java.lang.String r4 = r3.substring(r5, r6)
            java.lang.String r4 = r4.toLowerCase()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            java.lang.String r7 = r3.substring(r6)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            int r8 = r7.length()
            if (r8 == 0) goto L_0x0144
            java.lang.String r4 = r4.concat(r7)
            goto L_0x014a
        L_0x0144:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r4)
            r4 = r7
        L_0x014a:
            java.lang.String r7 = "get"
            java.lang.String r8 = java.lang.String.valueOf(r3)
            int r9 = r8.length()
            if (r9 == 0) goto L_0x015b
            java.lang.String r7 = r7.concat(r8)
            goto L_0x0161
        L_0x015b:
            java.lang.String r8 = new java.lang.String
            r8.<init>(r7)
            r7 = r8
        L_0x0161:
            java.lang.Object r7 = r0.get(r7)
            java.lang.reflect.Method r7 = (java.lang.reflect.Method) r7
            java.lang.String r8 = "has"
            java.lang.String r3 = java.lang.String.valueOf(r3)
            int r9 = r3.length()
            if (r9 == 0) goto L_0x0178
            java.lang.String r3 = r8.concat(r3)
            goto L_0x017d
        L_0x0178:
            java.lang.String r3 = new java.lang.String
            r3.<init>(r8)
        L_0x017d:
            java.lang.Object r3 = r0.get(r3)
            java.lang.reflect.Method r3 = (java.lang.reflect.Method) r3
            if (r7 == 0) goto L_0x004d
            java.lang.Object[] r8 = new java.lang.Object[r5]
            java.lang.Object r7 = com.google.android.gms.internal.zzffu.zza(r7, r12, r8)
            if (r3 != 0) goto L_0x0204
            boolean r3 = r7 instanceof java.lang.Boolean
            if (r3 == 0) goto L_0x019f
            r3 = r7
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = r3.booleanValue()
            if (r3 != 0) goto L_0x019d
        L_0x019a:
            r3 = 1
            goto L_0x01fe
        L_0x019d:
            r3 = 0
            goto L_0x01fe
        L_0x019f:
            boolean r3 = r7 instanceof java.lang.Integer
            if (r3 == 0) goto L_0x01ad
            r3 = r7
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r3 != 0) goto L_0x019d
            goto L_0x019a
        L_0x01ad:
            boolean r3 = r7 instanceof java.lang.Float
            if (r3 == 0) goto L_0x01be
            r3 = r7
            java.lang.Float r3 = (java.lang.Float) r3
            float r3 = r3.floatValue()
            r8 = 0
            int r3 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
            if (r3 != 0) goto L_0x019d
            goto L_0x019a
        L_0x01be:
            boolean r3 = r7 instanceof java.lang.Double
            if (r3 == 0) goto L_0x01d0
            r3 = r7
            java.lang.Double r3 = (java.lang.Double) r3
            double r8 = r3.doubleValue()
            r10 = 0
            int r3 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r3 != 0) goto L_0x019d
            goto L_0x019a
        L_0x01d0:
            boolean r3 = r7 instanceof java.lang.String
            if (r3 == 0) goto L_0x01db
            java.lang.String r3 = ""
        L_0x01d6:
            boolean r3 = r7.equals(r3)
            goto L_0x01fe
        L_0x01db:
            boolean r3 = r7 instanceof com.google.android.gms.internal.zzfes
            if (r3 == 0) goto L_0x01e2
            com.google.android.gms.internal.zzfes r3 = com.google.android.gms.internal.zzfes.zzpfg
            goto L_0x01d6
        L_0x01e2:
            boolean r3 = r7 instanceof com.google.android.gms.internal.zzfhe
            if (r3 == 0) goto L_0x01f0
            r3 = r7
            com.google.android.gms.internal.zzfhe r3 = (com.google.android.gms.internal.zzfhe) r3
            com.google.android.gms.internal.zzfhe r3 = r3.zzcxq()
            if (r7 != r3) goto L_0x019d
            goto L_0x019a
        L_0x01f0:
            boolean r3 = r7 instanceof java.lang.Enum
            if (r3 == 0) goto L_0x019d
            r3 = r7
            java.lang.Enum r3 = (java.lang.Enum) r3
            int r3 = r3.ordinal()
            if (r3 != 0) goto L_0x019d
            goto L_0x019a
        L_0x01fe:
            if (r3 != 0) goto L_0x0202
            r3 = 1
            goto L_0x0210
        L_0x0202:
            r3 = 0
            goto L_0x0210
        L_0x0204:
            java.lang.Object[] r6 = new java.lang.Object[r5]
            java.lang.Object r3 = com.google.android.gms.internal.zzffu.zza(r3, r12, r6)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = r3.booleanValue()
        L_0x0210:
            if (r3 == 0) goto L_0x004d
            java.lang.String r3 = zztv(r4)
            zzb(r13, r14, r3, r7)
            goto L_0x004d
        L_0x021b:
            boolean r0 = r12 instanceof com.google.android.gms.internal.zzffu.zzd
            if (r0 == 0) goto L_0x023d
            r0 = r12
            com.google.android.gms.internal.zzffu$zzd r0 = (com.google.android.gms.internal.zzffu.zzd) r0
            com.google.android.gms.internal.zzffq<java.lang.Object> r0 = r0.zzpgz
            java.util.Iterator r0 = r0.iterator()
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x023d
            java.lang.Object r12 = r0.next()
            java.util.Map$Entry r12 = (java.util.Map.Entry) r12
            r12.getKey()
            java.lang.NoSuchMethodError r12 = new java.lang.NoSuchMethodError
            r12.<init>()
            throw r12
        L_0x023d:
            com.google.android.gms.internal.zzffu r12 = (com.google.android.gms.internal.zzffu) r12
            com.google.android.gms.internal.zzfio r0 = r12.zzpgr
            if (r0 == 0) goto L_0x0248
            com.google.android.gms.internal.zzfio r12 = r12.zzpgr
            r12.zzd(r13, r14)
        L_0x0248:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzfhh.zza(com.google.android.gms.internal.zzfhe, java.lang.StringBuilder, int):void");
    }

    static final void zzb(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object zzb : (List) obj) {
                zzb(sb, i, str, zzb);
            }
            return;
        }
        sb.append(10);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(' ');
        }
        sb.append(str);
        if (obj instanceof String) {
            sb.append(": \"");
            sb.append(zzfih.zzbc(zzfes.zztr((String) obj)));
            sb.append('\"');
        } else if (obj instanceof zzfes) {
            sb.append(": \"");
            sb.append(zzfih.zzbc((zzfes) obj));
            sb.append('\"');
        } else if (obj instanceof zzffu) {
            sb.append(" {");
            zza((zzffu) obj, sb, i + 2);
            sb.append("\n");
            for (int i3 = 0; i3 < i; i3++) {
                sb.append(' ');
            }
            sb.append(VectorFormat.DEFAULT_SUFFIX);
        } else {
            sb.append(": ");
            sb.append(obj.toString());
        }
    }

    private static final String zztv(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(charAt));
        }
        return sb.toString();
    }
}
