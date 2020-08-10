package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.util.zzq;

public final class zzbhd {
    private static Context zzgfe;
    private static Boolean zzgff;

    public static synchronized boolean zzcz(Context context) {
        Boolean valueOf;
        synchronized (zzbhd.class) {
            Context applicationContext = context.getApplicationContext();
            if (zzgfe == null || zzgff == null || zzgfe != applicationContext) {
                zzgff = null;
                if (zzq.isAtLeastO()) {
                    valueOf = Boolean.valueOf(applicationContext.getPackageManager().isInstantApp());
                } else {
                    try {
                        context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                        zzgff = Boolean.valueOf(true);
                    } catch (ClassNotFoundException e) {
                        valueOf = Boolean.valueOf(false);
                    }
                    zzgfe = applicationContext;
                    boolean booleanValue = zzgff.booleanValue();
                    return booleanValue;
                }
                zzgff = valueOf;
                zzgfe = applicationContext;
                boolean booleanValue2 = zzgff.booleanValue();
                return booleanValue2;
            }
            boolean booleanValue3 = zzgff.booleanValue();
            return booleanValue3;
        }
    }
}
