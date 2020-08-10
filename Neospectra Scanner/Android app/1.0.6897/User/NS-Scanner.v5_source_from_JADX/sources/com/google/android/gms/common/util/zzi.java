package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;

public final class zzi {
    private static Boolean zzgeg;
    private static Boolean zzgeh;
    private static Boolean zzgei;
    private static Boolean zzgej;
    private static Boolean zzgek;

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (zzgeh.booleanValue() != false) goto L_0x003e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean zza(android.content.res.Resources r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.Boolean r1 = zzgeg
            if (r1 != 0) goto L_0x0045
            android.content.res.Configuration r1 = r4.getConfiguration()
            int r1 = r1.screenLayout
            r1 = r1 & 15
            r2 = 3
            r3 = 1
            if (r1 <= r2) goto L_0x0016
            r1 = 1
            goto L_0x0017
        L_0x0016:
            r1 = 0
        L_0x0017:
            if (r1 != 0) goto L_0x003e
            java.lang.Boolean r1 = zzgeh
            if (r1 != 0) goto L_0x0036
            android.content.res.Configuration r4 = r4.getConfiguration()
            int r1 = r4.screenLayout
            r1 = r1 & 15
            if (r1 > r2) goto L_0x002f
            int r4 = r4.smallestScreenWidthDp
            r1 = 600(0x258, float:8.41E-43)
            if (r4 < r1) goto L_0x002f
            r4 = 1
            goto L_0x0030
        L_0x002f:
            r4 = 0
        L_0x0030:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            zzgeh = r4
        L_0x0036:
            java.lang.Boolean r4 = zzgeh
            boolean r4 = r4.booleanValue()
            if (r4 == 0) goto L_0x003f
        L_0x003e:
            r0 = 1
        L_0x003f:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r0)
            zzgeg = r4
        L_0x0045:
            java.lang.Boolean r4 = zzgeg
            boolean r4 = r4.booleanValue()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.zzi.zza(android.content.res.Resources):boolean");
    }

    @TargetApi(20)
    public static boolean zzcs(Context context) {
        if (zzgei == null) {
            zzgei = Boolean.valueOf(zzq.zzamm() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch"));
        }
        return zzgei.booleanValue();
    }

    @TargetApi(24)
    public static boolean zzct(Context context) {
        return (!zzq.isAtLeastN() || zzcu(context)) && zzcs(context);
    }

    @TargetApi(21)
    public static boolean zzcu(Context context) {
        if (zzgej == null) {
            zzgej = Boolean.valueOf(zzq.zzamn() && context.getPackageManager().hasSystemFeature("cn.google"));
        }
        return zzgej.booleanValue();
    }

    public static boolean zzcv(Context context) {
        if (zzgek == null) {
            zzgek = Boolean.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.type.iot") || context.getPackageManager().hasSystemFeature("android.hardware.type.embedded"));
        }
        return zzgek.booleanValue();
    }
}
