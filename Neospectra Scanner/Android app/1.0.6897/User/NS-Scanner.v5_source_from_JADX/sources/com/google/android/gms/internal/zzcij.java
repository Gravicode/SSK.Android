package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbq;
import java.lang.Thread.UncaughtExceptionHandler;

final class zzcij implements UncaughtExceptionHandler {
    private final String zzjep;
    private /* synthetic */ zzcih zzjeq;

    public zzcij(zzcih zzcih, String str) {
        this.zzjeq = zzcih;
        zzbq.checkNotNull(str);
        this.zzjep = str;
    }

    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zzjeq.zzawy().zzazd().zzj(this.zzjep, th);
    }
}
