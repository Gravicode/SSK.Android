package com.google.android.gms.internal;

import android.content.Context;
import android.util.Log;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public final class zzbeu implements zzbeb {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final zzcup zzfkh = new zzcup(zzcue.zzks("com.google.android.gms.clearcut.public")).zzku("gms:playlog:service:sampling_").zzkv("LogSampling__");
    private static Map<String, zzcui<String>> zzfki = null;
    private static Boolean zzfkj = null;
    private static Long zzfkk = null;
    private final Context zzair;

    public zzbeu(Context context) {
        this.zzair = context;
        if (zzfki == null) {
            zzfki = new HashMap();
        }
        if (this.zzair != null) {
            zzcui.zzdz(this.zzair);
        }
    }

    private static boolean zzcb(Context context) {
        if (zzfkj == null) {
            zzfkj = Boolean.valueOf(zzbhf.zzdb(context).checkCallingOrSelfPermission("com.google.android.providers.gsf.permission.READ_GSERVICES") == 0);
        }
        return zzfkj.booleanValue();
    }

    private static zzbev zzfw(String str) {
        if (str == null) {
            return null;
        }
        String str2 = "";
        int indexOf = str.indexOf(44);
        int i = 0;
        if (indexOf >= 0) {
            str2 = str.substring(0, indexOf);
            i = indexOf + 1;
        }
        String str3 = str2;
        int indexOf2 = str.indexOf(47, i);
        if (indexOf2 <= 0) {
            String str4 = "LogSamplerImpl";
            String str5 = "Failed to parse the rule: ";
            String valueOf = String.valueOf(str);
            Log.e(str4, valueOf.length() != 0 ? str5.concat(valueOf) : new String(str5));
            return null;
        }
        try {
            long parseLong = Long.parseLong(str.substring(i, indexOf2));
            long parseLong2 = Long.parseLong(str.substring(indexOf2 + 1));
            if (parseLong < 0 || parseLong2 < 0) {
                StringBuilder sb = new StringBuilder(72);
                sb.append("negative values not supported: ");
                sb.append(parseLong);
                sb.append("/");
                sb.append(parseLong2);
                Log.e("LogSamplerImpl", sb.toString());
                return null;
            }
            zzbev zzbev = new zzbev(str3, parseLong, parseLong2);
            return zzbev;
        } catch (NumberFormatException e) {
            String str6 = "LogSamplerImpl";
            String str7 = "parseLong() failed while parsing: ";
            String valueOf2 = String.valueOf(str);
            Log.e(str6, valueOf2.length() != 0 ? str7.concat(valueOf2) : new String(str7), e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzg(java.lang.String r14, int r15) {
        /*
            r13 = this;
            r0 = 0
            if (r14 == 0) goto L_0x000a
            boolean r1 = r14.isEmpty()
            if (r1 != 0) goto L_0x000a
            goto L_0x0012
        L_0x000a:
            if (r15 < 0) goto L_0x0011
            java.lang.String r14 = java.lang.String.valueOf(r15)
            goto L_0x0012
        L_0x0011:
            r14 = r0
        L_0x0012:
            r15 = 1
            if (r14 != 0) goto L_0x0016
            return r15
        L_0x0016:
            android.content.Context r1 = r13.zzair
            if (r1 == 0) goto L_0x003f
            android.content.Context r1 = r13.zzair
            boolean r1 = zzcb(r1)
            if (r1 != 0) goto L_0x0023
            goto L_0x003f
        L_0x0023:
            java.util.Map<java.lang.String, com.google.android.gms.internal.zzcui<java.lang.String>> r1 = zzfki
            java.lang.Object r1 = r1.get(r14)
            com.google.android.gms.internal.zzcui r1 = (com.google.android.gms.internal.zzcui) r1
            if (r1 != 0) goto L_0x0038
            com.google.android.gms.internal.zzcup r1 = zzfkh
            com.google.android.gms.internal.zzcui r1 = r1.zzaw(r14, r0)
            java.util.Map<java.lang.String, com.google.android.gms.internal.zzcui<java.lang.String>> r0 = zzfki
            r0.put(r14, r1)
        L_0x0038:
            java.lang.Object r14 = r1.get()
            r0 = r14
            java.lang.String r0 = (java.lang.String) r0
        L_0x003f:
            com.google.android.gms.internal.zzbev r14 = zzfw(r0)
            if (r14 != 0) goto L_0x0046
            return r15
        L_0x0046:
            java.lang.String r0 = r14.zzfkl
            android.content.Context r1 = r13.zzair
            java.lang.Long r2 = zzfkk
            r3 = 0
            if (r2 != 0) goto L_0x0070
            if (r1 == 0) goto L_0x006e
            boolean r2 = zzcb(r1)
            if (r2 == 0) goto L_0x0069
            android.content.ContentResolver r1 = r1.getContentResolver()
            java.lang.String r2 = "android_id"
            long r1 = com.google.android.gms.internal.zzdmf.getLong(r1, r2, r3)
            java.lang.Long r1 = java.lang.Long.valueOf(r1)
        L_0x0066:
            zzfkk = r1
            goto L_0x0070
        L_0x0069:
            java.lang.Long r1 = java.lang.Long.valueOf(r3)
            goto L_0x0066
        L_0x006e:
            r1 = r3
            goto L_0x0076
        L_0x0070:
            java.lang.Long r1 = zzfkk
            long r1 = r1.longValue()
        L_0x0076:
            r5 = 8
            if (r0 == 0) goto L_0x0098
            boolean r6 = r0.isEmpty()
            if (r6 == 0) goto L_0x0081
            goto L_0x0098
        L_0x0081:
            java.nio.charset.Charset r6 = UTF_8
            byte[] r0 = r0.getBytes(r6)
            int r6 = r0.length
            int r6 = r6 + r5
            java.nio.ByteBuffer r5 = java.nio.ByteBuffer.allocate(r6)
            r5.put(r0)
            r5.putLong(r1)
            byte[] r0 = r5.array()
            goto L_0x00a4
        L_0x0098:
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocate(r5)
            java.nio.ByteBuffer r0 = r0.putLong(r1)
            byte[] r0 = r0.array()
        L_0x00a4:
            long r0 = com.google.android.gms.internal.zzbep.zzj(r0)
            long r5 = r14.zzfkm
            long r7 = r14.zzfkn
            int r14 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r14 < 0) goto L_0x00d3
            int r14 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r14 >= 0) goto L_0x00b5
            goto L_0x00d3
        L_0x00b5:
            if (r14 <= 0) goto L_0x00d1
            int r14 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r14 < 0) goto L_0x00bd
            long r0 = r0 % r7
            goto L_0x00cc
        L_0x00bd:
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            long r9 = r2 % r7
            r11 = 1
            long r9 = r9 + r11
            long r0 = r0 & r2
            long r0 = r0 % r7
            long r9 = r9 + r0
            long r0 = r9 % r7
        L_0x00cc:
            int r14 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r14 >= 0) goto L_0x00d1
            return r15
        L_0x00d1:
            r14 = 0
            return r14
        L_0x00d3:
            java.lang.IllegalArgumentException r14 = new java.lang.IllegalArgumentException
            r15 = 72
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>(r15)
            java.lang.String r15 = "negative values not supported: "
            r0.append(r15)
            r0.append(r5)
            java.lang.String r15 = "/"
            r0.append(r15)
            r0.append(r7)
            java.lang.String r15 = r0.toString()
            r14.<init>(r15)
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbeu.zzg(java.lang.String, int):boolean");
    }
}
