package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.SystemClock;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public final class zzj {
    private static IntentFilter zzgel = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    private static long zzgem;
    private static float zzgen = Float.NaN;

    @TargetApi(20)
    public static int zzcw(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }
        Intent registerReceiver = context.getApplicationContext().registerReceiver(null, zzgel);
        int i = 0;
        int i2 = ((registerReceiver == null ? 0 : registerReceiver.getIntExtra("plugged", 0)) & 7) != 0 ? 1 : 0;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            return -1;
        }
        if (zzq.zzamm() ? powerManager.isInteractive() : powerManager.isScreenOn()) {
            i = 1;
        }
        return (i << 1) | i2;
    }

    public static synchronized float zzcx(Context context) {
        synchronized (zzj.class) {
            if (SystemClock.elapsedRealtime() - zzgem >= 60000 || Float.isNaN(zzgen)) {
                Intent registerReceiver = context.getApplicationContext().registerReceiver(null, zzgel);
                if (registerReceiver != null) {
                    zzgen = ((float) registerReceiver.getIntExtra(Param.LEVEL, -1)) / ((float) registerReceiver.getIntExtra("scale", -1));
                }
                zzgem = SystemClock.elapsedRealtime();
                float f = zzgen;
                return f;
            }
            float f2 = zzgen;
            return f2;
        }
    }
}
